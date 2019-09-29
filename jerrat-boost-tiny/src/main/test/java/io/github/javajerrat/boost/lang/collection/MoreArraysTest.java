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

package io.github.javajerrat.boost.lang.collection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/20
 */
class MoreArraysTest {

    @Test
    void concat() {
        Integer[] a1 = MoreArrays.concat(Integer[]::new, new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6});
        System.out.print(Arrays.toString(a1));
        Integer[] a2 = MoreArrays.concat(Integer[]::new, new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6}, new Integer[]{7, 8, 9});
        System.out.print(Arrays.toString(a2));
    }
}