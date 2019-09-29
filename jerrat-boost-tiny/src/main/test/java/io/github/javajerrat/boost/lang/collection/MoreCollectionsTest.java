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

import com.google.common.collect.Iterables;
import io.github.javajerrat.boost.lang.collection.iterable.FIterable;
import io.github.javajerrat.boost.lang.collection.lazy.Range;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/19
 */
class MoreCollectionsTest {

    @Test
    void fixedListOf() {
        List<Integer> fixed0 = Colls.fixedListOf(1, 2, 3);
        fixed0.set(0, 10);

        List<Integer> fixed1 = Colls.fixedListOfPrimitive(1, 2, 3);
        fixed1.set(0, 10);

        List<Integer> fixed2 = Colls.fixedListOfPrimitive(1, 2, 3);
        fixed2.set(0, 10);

        Assertions.assertEquals(fixed0, fixed1);
        Assertions.assertEquals(fixed1, fixed2);
    }

    @Test
    void repeatListOf() {
        List<Integer> list = Colls.repeatListOf(1, 10);
        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> list.set(1, 2));

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> list.add(1));

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> list.remove(1));
        List<Integer> list1 = Colls.listOf(1, 2, 3);
    }

    @Test
    void mapOf() {
        Map<String, Integer> map = Colls.mapOfEntries(
            Pair.of("a", 1),
            Pair.of("b", 2),
            Pair.of("c", 3)
        );

        Assertions.assertEquals(3, map.size());
        Assertions.assertEquals(1, map.get("a"));
        Assertions.assertEquals(2, map.get("b"));
        Assertions.assertEquals(3, map.get("c"));
    }

    @Test
    void invert() {
        Map<String, Integer> map = Colls.mapOf(
            "a", 1,
            "b", 2,
            "c", 1);

        Map<Integer, String> expected = Colls.mapOf(
            1, "c",
            2, "b");

        {
            Map<Integer, String> invertedMap = Colls.invert(map);
            Assertions.assertEquals(HashMap.class, invertedMap.getClass());
            Assertions.assertEquals(expected, invertedMap);
            System.out.println(expected);
        }

        {
            Map<Integer, String> invertedMap = Colls.invert(map, new TreeMap<>());
            Assertions.assertEquals(TreeMap.class, invertedMap.getClass());
            Assertions.assertEquals(new TreeMap<>(expected), invertedMap);
        }
    }

    @Test
    void invertBy() {
        Map<String, Integer> map = new TreeMap<>(Colls.mapOfEntries(
            Pair.of("a", 1),
            Pair.of("b", 2),
            Pair.of("c", 1)
        ));

        Map<Integer, List<String>> expected = Colls.mapOfEntries(
            Pair.of(2, Colls.listOf("a", "c")),
            Pair.of(3, Colls.listOf("b"))
        );

        {
            Map<Integer, List<String>> invertedMap = Colls.invertBy(map, (it) -> it + 1);
            Assertions.assertEquals(HashMap.class, invertedMap.getClass());
            Assertions.assertEquals(expected, invertedMap);
            System.out.println(expected);
        }

        {
            TreeMap<Integer, List<String>> invertedMap = Colls.invertBy(map, (it) -> it + 1, new TreeMap<>());
            Assertions.assertEquals(TreeMap.class, invertedMap.getClass());
            Assertions.assertEquals(new TreeMap<>(expected), invertedMap);
        }
    }

    @Test
    void some() {
        {
            boolean r = Colls.some(Colls.listOf(1, 2, 3), (it) -> it >= 3);
            Assertions.assertTrue(r);
        }

        {
            boolean r = Colls.some(Colls.<Integer>listOf(), (it) -> it >= 3);
            Assertions.assertFalse(r);
        }
    }

    @Test
    void every() {
        {
            boolean r = Colls.every(Colls.listOf(1, 2, 3), (it) -> it >= 3);
            Assertions.assertFalse(r);
        }

        {
            boolean r = Colls.every(Colls.listOf(1, 2, 3), (it) -> it >= 1);
            Assertions.assertTrue(r);
        }

        {
            boolean r = Colls.every(Colls.<Integer>listOf(), (it) -> it >= 3);
            Assertions.assertTrue(r);
        }
    }

    @Test
    void chunkView() {
        TreeSet<Integer> set = Colls.treeSetOf(8, 7, 6, 5, 4, 3, 2, 1);
        Iterable<List<Integer>> result = Colls.chunkView(set, 3);
        System.out.println(Iterables.toString(result));
    }

    @Test
    void reverse() {
        {
            ArrayList<Integer> list = Colls.arrayListOf(1, 2, 3, 4);
            Colls.reverse(list);
            System.out.println(list);
        }

        {
            TreeSet<Integer> set = Colls.treeSetOf(4, 3, 2, 1);
            List<Integer> list = Colls.reversed(set);
            System.out.println(list);
        }
        {
            List<Integer> list = Colls.listOf(1, 2, 3, 4);
            List<Integer> reversedList = Colls.reverseView(list);
            System.out.println(reversedList);
        }

    }

    @Test
    void setOperation() {
        Set<Integer> set1 = Colls.setOf(1, 2, 3);
        Set<Integer> set2 = Colls.setOf(3, 4, 5);
        Set<Integer> setUnion = Colls.union(set1, set2);
        System.out.println(setUnion);
        Set<Integer> setIntersection = Colls.intersection(set1, set2);
        System.out.println(setIntersection);
        Set<Integer> setDiff = Colls.difference(set1, set2);
        System.out.println(setDiff);
        Set<Integer> setSymDiff = Colls.symmetricDifference(set1, set2);
        System.out.println(setSymDiff);
    }

    @Test
    void skippedSort() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            Colls.skippedSort(Colls.listOf(1, 2, 3), it -> true, Comparator.comparingInt(a -> a));
        });
    }

    @Test void union() {
        {
            Set<Double> result = Colls.union(Colls.setOf(1.1, 2.1, 2.2), Colls.setOf(1.3, 4.1, 5.2));
            Assertions.assertEquals(Colls.setOf(1.1, 2.1, 2.2, 1.3, 4.1, 5.2), result);

        }
        {
            Comparator<Double> comparator = Comparator.comparing(Double::intValue);
            Set<Double> result = Colls.union(Colls.treeSetOf(comparator, 1.1, 2.1, 2.2), Colls.treeSetOf(comparator, 1.3, 4.1, 5.2));
            Assertions.assertEquals(Colls.setOf(1.1, 2.1, 4.1, 5.2), result);
        }

    }

    @Test
    void count() {
        int count0 = Colls.count(Colls.listOf(1, 2, 3, 4, 5, 5), 5);
        Assertions.assertEquals(2, count0);
        int count1 = Colls.count(Colls.listOf(1, 2, 3, 4, 5, 5), it -> it > 3);
        Assertions.assertEquals(3, count1);
    }

    @Test
    void countBy() {
        Map<Double, Integer> count0 = Colls.countBy(Colls.listOf(1.1, 1.2, 2.3, 2.8, 4.5), Math::floor);
        System.out.println(count0);
    }

    @Test
    void groupBy() {
        Map<Double, List<Double>> groups = Colls.groupBy(Colls.listOf(1.1, 1.2, 2.3, 2.8, 4.5), Math::floor);
        System.out.println(groups);
    }


    @Test
    void isDistinct() {
        Assertions.assertTrue(Colls.isDistinct(Colls.listOf()));
        Assertions.assertTrue(Colls.isDistinct(Colls.listOf(1, 2, 3)));
        Assertions.assertFalse(Colls.isDistinct(Colls.listOf(1, 1, 2, 3)));

        Assertions.assertTrue(Colls.isDistinct(Colls.listOf(), Function.identity()));
        Assertions.assertTrue(Colls.isDistinct(Colls.listOf(1, 2, 3), Function.identity()));
        Assertions.assertFalse(Colls.isDistinct(Colls.listOf(1, 1, 2, 3), Function.identity()));

        Assertions.assertFalse(Colls.isDistinct(Colls.listOf(1, 2, 3, 13), i -> i / 10));
        Assertions.assertTrue(Colls.isDistinct(Colls.listOf(1, 13), i -> i / 10));

    }

    @Data
    @AllArgsConstructor
    static class PointEntity {
        private int id;
        private int x;
        private int y;
    }

    @Test
    void keyBy() {
        List<PointEntity> points = Colls.listOf(
            new PointEntity(1, 0, 0),
            new PointEntity(2, 0, 0),
            new PointEntity(3, 0, 0),
            new PointEntity(4, 0, 0),
            new PointEntity(5, 0, 0)
        );
        Map<Integer, PointEntity> pointMaps = Colls.keyBy(points, PointEntity::getId);
        System.out.println(pointMaps);
    }

    @Test
    void indexOf() {
        int index0 = Colls.indexOf(Colls.listOf(1, 3, 3, 5), (it) -> it <= 3);
        int index1 = Colls.lastIndexOf(Colls.listOf(1, 3, 3, 5), (it) -> it <= 3);
        Assertions.assertEquals(0, index0);
        Assertions.assertEquals(2, index1);
    }


    @Test
    void zip() {
        List<Long> list = Range.range(1, 9, 1);
        Seq.zip(list, Lazys.transform(list, i -> i + 1), Lazys.transform(list, i -> i + 2), Lazys.transform(list, i -> i + 3))
            .forEach(Tuple.consumer((x1, x2, x3, x4) -> {
                System.out.printf("%d %d %d %d\n", x1, x2, x3, x4);
            }));
    }

    @Test
    void concat() {
        FIterable<Long> r = Colls.concatView(
            Lazys.range(1, 10),
            Lazys.range(10, 20),
            Lazys.range(20, 30));
        Assertions.assertEquals(Lazys.range(1, 30), r.toList());
    }

    @Test
    void copy() {
        {
            ArrayList<Integer> list = Colls.arrayListOf(10, 20, 30, 40);
            Colls.copy(Colls.listOf(1, 2, 3, 4), 1, list, 1, 2);
            Assertions.assertEquals(Colls.listOf(10, 2, 3, 40), list);
        }

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            ArrayList<Integer> list = Colls.arrayListOf(10, 20, 30, 40);
            Colls.copy(Colls.listOf(1, 2, 3, 4), 1, list, 1, 5);
        });
    }

    @Test
    void flatten() {
        List<List<Integer>> list = Colls.listOf(
            Colls.listOf(1, 2, 3),
            Colls.listOf(4, 5),
            Colls.listOf(6),
            Colls.listOf()
            );
        Assertions.assertEquals(Colls.listOf(1, 2, 3, 4, 5, 6), Colls.flatten(list));
    }

    @Test
    void replace() {
        {
            List<Integer> list = Colls.arrayListOf(1, 2, 3);
            Colls.replace(list, 1, 5);
            Assertions.assertEquals(Colls.listOf(5, 2, 3), list);
        }
        {
            List<Integer> list = Colls.arrayListOf(1, 2, 3);
            Colls.replaceWhile(list, (it) -> it >= 2, 5);
            Assertions.assertEquals(Colls.listOf(1, 5, 5), list);
        }

        {
            List<Integer> list = Colls.arrayListOf(1, 2, 3);
            Colls.replaceBy(list, 1, (it, index) -> it + index);
            Assertions.assertEquals(Colls.listOf(1, 2, 3), list);
        }

        {
            List<Integer> list = Colls.arrayListOf(1, 2, 3);
            Colls.replaceWhileBy(list, (it) -> it >= 2, (it, index) -> it + 1);
            Assertions.assertEquals(Colls.listOf(1, 3, 4), list);
        }
    }

    @Test
    void concatView() {
        Set<Integer> set = Colls.setOf(1, 2, 3);
        List<Integer> list = Colls.listOf(4, 5, 6);
        List<Integer> result = Colls.arrayListOf(Colls.concatView(set, list));
        Assertions.assertEquals(Colls.listOf(1, 2, 3, 4, 5, 6), result);
    }

    @Test
    void fill() {
        ArrayList<Integer> list = Colls.arrayListOf(1, 2, 3);
        Colls.fill(list, 1);
        Assertions.assertEquals(Colls.listOf(1, 1, 1), list);
        Colls.fill(list, list.size(), 0);
        Assertions.assertEquals(Colls.listOf(0, 0, 0), list);
        Colls.fill(list, list.size() + 3, 0);
        Assertions.assertEquals(Colls.listOf(0, 0, 0, 0, 0, 0), list);
        Colls.fill(list, list.size() - 1, 0);
        Assertions.assertEquals(Colls.listOf(0, 0, 0, 0, 0), list);
        Colls.fill(list, 0, 0);
        Assertions.assertEquals(Colls.listOf(), list);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Colls.fill(list, -1, 0);
        });
    }

}