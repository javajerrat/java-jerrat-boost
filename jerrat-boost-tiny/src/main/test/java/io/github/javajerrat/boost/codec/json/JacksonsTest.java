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

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.github.javajerrat.boost.basetools.datetime.DateFormats;
import io.github.javajerrat.boost.lang.collection.Colls;
import io.github.javajerrat.boost.lang.string.Strings;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/20
 */
class JacksonsTest {

    @Data
    public static class TimeBean {
        @JsonFormat(pattern = DateFormats.HH_MM_SS)
        Date time;
    }

    @SneakyThrows
    @Test
    public void testDate() {
        ObjectMapper jacksonMapper = Jacksons.jacksonObjectMapper();

        // Test time zone settings
        {
            String json = "{\"time\": \"11:12:00\"}";
            TimeBean bean = jacksonMapper.readValue(json, TimeBean.class);
            Date expected = DateUtils.parseDate("1970-01-01 11:12:00", DateFormats.YYYY_MM_DD_HH_MM_SS);
            assertEquals(bean.getTime(), expected);
        }

        {
            Map<String, String> map = Colls.mapOf("time", "11:12:00");
            TimeBean bean = jacksonMapper.convertValue(map, TimeBean.class);
            Date expected = DateUtils.parseDate("1970-01-01 11:12:00", DateFormats.YYYY_MM_DD_HH_MM_SS);
            assertEquals(bean.getTime(), expected);
        }

        // Test support for multiple time formats
        {
            Map<String, String> map = Colls.mapOf("time", "2019-01-01 11:12:00");
            TimeBean bean = jacksonMapper.convertValue(map, TimeBean.class);
            Date expected = DateUtils.parseDate("2019-01-01 11:12:00", DateFormats.YYYY_MM_DD_HH_MM_SS);
            assertEquals(bean.getTime(), expected);
        }

        {
            Map<String, String> map = Colls.mapOf("time", "2019-01-01");
            TimeBean bean = jacksonMapper.convertValue(map, TimeBean.class);
            Date expected = DateUtils.parseDate("2019-01-01 00:00:00", DateFormats.YYYY_MM_DD_HH_MM_SS);
            assertEquals(bean.getTime(), expected);
        }
    }

    @SneakyThrows
    @Test
    void testSerializeNumberAsString() {
        Map<String, Object> map = new TreeMap<>(Colls.mapOf(
            "bigDecimal", new BigDecimal("12759475434294793793435"),
            "bigInteger", new BigInteger("12749795495795794795779"),
            "integer", Integer.MAX_VALUE,
            "long", Long.MAX_VALUE,
            "double", 1.12E+12
        ));


        {
            String json = Jacksons.DEFAULT_OBJECT_MAPPER.writeValueAsString(map);
            @Language("json") String excepted = "{\n"
                + "  \"bigDecimal\" : 12759475434294793793435,\n"
                + "  \"bigInteger\" : 12749795495795794795779,\n"
                + "  \"double\" : 1.12E12,\n"
                + "  \"integer\" : 2147483647,\n"
                + "  \"long\" : 9223372036854775807\n"
                + "}";
            assertEquals(excepted, Strings.replace(json, "\r\n", "\n"));
        }

        {
            String json = Jacksons.jacksonObjectMapper(true, true)
                .writeValueAsString(map);
            @Language("json") String excepted = "{\n"
                + "  \"bigDecimal\" : \"12759475434294793793435\",\n"
                + "  \"bigInteger\" : \"12749795495795794795779\",\n"
                + "  \"double\" : 1.12E12,\n"
                + "  \"integer\" : 2147483647,\n"
                + "  \"long\" : \"9223372036854775807\"\n"
                + "}";
            assertEquals(excepted, Strings.replace(json, "\r\n", "\n"));
        }
    }

}