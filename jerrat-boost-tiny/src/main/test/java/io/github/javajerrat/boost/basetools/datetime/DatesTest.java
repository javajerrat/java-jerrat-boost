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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2020/3/9
 */
class DatesTest {

    @Test
    void setTime() {
        Date date = new Date();
        date = Dates.setTime(date, 8, 0 ,0);
        System.out.println(Dates.format(date, DateFormats.YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println(Dates.getDateTuple(date));
    }

    void setDate() {
        Date date = new Date();
        date = Dates.setDate(date, 2000, 2 ,28);
        System.out.println(Dates.format(date, DateFormats.YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println(Dates.getDateTuple(date));
    }
}