/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.javajerrat.boost.codec.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/20
 */

public class Jacksons {

    public Jacksons () {
        throw new UnsupportedOperationException();
    }

    /**
     * Be careful not to modify it!!!
     */
    public static ObjectMapper DEFAULT_OBJECT_MAPPER = jacksonObjectMapper();

    public static ObjectMapper jacksonObjectMapper() {
        return jacksonObjectMapper(true, false);
    }

    /**
     * Provide a mapper with common configuration
     * @param pretty If true, The mapper will output pretty json.
     * @param serializeNumberAsString If true,
     *
     * @return A new ObjectMapper object.
     */
    public static ObjectMapper jacksonObjectMapper(boolean pretty, boolean serializeNumberAsString) {

        ObjectMapper mapper = new ObjectMapper();
        configureJavaTime(mapper);
        configureBase(mapper);
        if (pretty) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        if (serializeNumberAsString) {
            configureSerializeNumberAsString(mapper);
        }
        return mapper;
    }

    private static void configureBase(ObjectMapper mapper) {
        mapper
            .configure(MapperFeature.USE_STD_BEAN_NAMING, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setTimeZone(TimeZone.getDefault());
    }

    /**
     * Serialize BigDecimal type, BigInteger type, and Long type to string type to prevent loss of precision
     *
     * @param mapper mapper
     */
    private static void configureSerializeNumberAsString(ObjectMapper mapper) {
        mapper.registerModule(new SimpleModule()
            .addSerializer(BigDecimal.class, ToStringSerializer.instance)
            .addSerializer(BigInteger.class, ToStringSerializer.instance)
            .addSerializer(Long.class, ToStringSerializer.instance));
    }

    public static void configureJavaTime(ObjectMapper jackson2ObjectMapper) {
        jackson2ObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class,
            new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        timeModule.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        timeModule.addSerializer(LocalDate.class,
            new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        timeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        jackson2ObjectMapper.registerModule(new SimpleModule()
            .addDeserializer(Date.class, new MultiDateDeserializer(
                Arrays.asList(
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy-MM-dd"
                ))));

        jackson2ObjectMapper
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            // Please note that if not set, jackson will use UTC instead of java default time zone
            // This feature is easy to make mistakes
            .setTimeZone(TimeZone.getDefault())
            .registerModule(timeModule)
            .registerModule(new ParameterNamesModule(JsonCreator.Mode.DELEGATING))
            .registerModule(new Jdk8Module());
    }

    /**
     * Supports multiple formats when deserializing dates
     */
    public static class MultiDateDeserializer extends DateDeserializers.DateDeserializer {
        private static final long serialVersionUID = 1L;

        private final List<String> dateFormats;

        private final TimeZone timeZone;

        public MultiDateDeserializer(List<String> dataFormats) {
            this.dateFormats = dataFormats;
            this.timeZone = TimeZone.getDefault();
        }

        public MultiDateDeserializer(List<String> dataFormats, TimeZone timeZone) {
            this.dateFormats = dataFormats;
            this.timeZone = timeZone;
        }


        @Override
        protected DateDeserializers.DateDeserializer withDateFormat(DateFormat df, String formatString) {
            List<String> dateFormats = new ArrayList<>();
            dateFormats.add(formatString);
            dateFormats.addAll(this.dateFormats);
            return new MultiDateDeserializer(dateFormats, df.getTimeZone());
        }

        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);
            final String date = node.textValue();

            for (String dateFormat : dateFormats) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                    df.setTimeZone(this.timeZone);
                    return df.parse(date);
                } catch (ParseException ignored) {
                }
            }
            if (date == null || date.isEmpty()) {
                return null;
            }
            throw new JsonParseException(jp, "Unparseable date: \"" + date + "\". Supported formats: " + dateFormats);
        }
    }
}
