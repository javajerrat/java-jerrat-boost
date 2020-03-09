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
import io.github.javajerrat.boost.basetools.datetime.DateFormats;
import io.github.javajerrat.boost.lang.collection.Colls;
import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
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
    public void test() {
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

}