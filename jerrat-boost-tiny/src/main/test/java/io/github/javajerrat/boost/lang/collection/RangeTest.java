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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Lists;
import io.github.javajerrat.boost.lang.collection.lazy.Range;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/19
 */
class RangeTest {

    @Test
    void range() {

        assertThrows(IllegalArgumentException.class, () -> {
            Range.range(1, 10, -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Range.range(1, 10, -1);
        });

        {
            Range range = Range.range(1, 1);
            List<Long> excepted = Colls.listOf();
            assertRange(excepted, range);
        }

        {
            Range range = Range.range(1, 10);
            List<Long> excepted = Colls.listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
            assertRange(excepted, range);
        }

        {
            Range range = Range.range(1, 10, 2);
            List<Long> excepted = Colls.listOf(1L, 3L, 5L, 7L, 9L);
            assertRange(excepted, range);
        }

        {
            Range range = Range.range(1, 11, 2);
            List<Long> excepted = Colls.listOf(1L, 3L, 5L, 7L, 9L);
            assertEquals(excepted, range);
        }
    }

    @Test
    void testSize() {
        assertEquals(0, Range.range(0, 0).size());
        assertEquals(1, Range.range(0, 1).size());

        assertEquals(0, Range.range(2, 2).size());
        assertEquals(1, Range.range(2, 3).size());

        assertEquals(0, Range.range(2, 2, 2).size());
        assertEquals(1, Range.range(2, 3, 2).size());
        assertEquals(1, Range.range(2, 4, 2).size());
        assertEquals(2, Range.range(2, 5, 2).size());
        assertEquals(2, Range.range(2, 6, 2).size());
        assertEquals(3, Range.range(2, 7, 2).size());

        assertEquals(0, Range.range(2, 2, -2).size());
        assertEquals(1, Range.range(3, 2, -2).size());
        assertEquals(1, Range.range(4, 2, -2).size());
        assertEquals(2, Range.range(5, 2, -2).size());
        assertEquals(2, Range.range(6, 2, -2).size());
        assertEquals(3, Range.range(7, 2, -2).size());
    }

    @Test
    void testEquals() {
        {
            Range list = Lazys.range(0, 1);
            Range list2 = Lazys.range(0, 1);
            assertTrue(list.equals(list2));
        }

        {
            Range list = Lazys.range(0, 10, 2);
            Range list2 = Lazys.range(0, 9, 2);
            assertTrue(list.equals(list2));
        }
    }

    @Test
    void testHashcode() {
        Range list = Lazys.range(0, 10, 2);
        System.out.print(list.hashCode());
    }

    private void assertRange(List<Long> excepted, Range range) {
        ArrayList<Long> list1 = Lists.newArrayList(range.iterator());

        ArrayList<Long> list2 = new ArrayList<>();
        for (int i = 0; i < range.size(); i++) {
            list2.add(range.get(i));
        }

        assertEquals(excepted.size(), range.size());
        assertEquals(excepted.size(), list1.size());
        assertEquals(excepted.size(), list2.size());

        assertEquals(excepted, list1);
        assertEquals(excepted, list2);
    }
}