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

package io.github.javajerrat.boost.basetools.datetime;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @see DateUtils
 * @see DateFormatUtils
 *
 */
public class Dates {


    public static Optional<Date> parseDate(String data, String parsePatterns) {
        try {
            Date date = DateUtils.parseDate(data, parsePatterns);
            return Optional.ofNullable(date);
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static Date unixEpoch2Date(long epoch) {
        return new Date(epoch * 1000L);
    }

    public static long unixEpoch() {
        return Instant.now().getEpochSecond();
    }

    public static long timestamp() {
        return System.currentTimeMillis();
    }
}
