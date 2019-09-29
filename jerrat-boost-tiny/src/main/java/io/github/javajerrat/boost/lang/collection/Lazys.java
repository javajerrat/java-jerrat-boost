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

import com.google.common.annotations.Beta;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import io.github.javajerrat.boost.lang.collection.lazy.Range;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.validation.constraints.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/18
 *
 * This involves a Lazy Load concept, which is very common in Guava.
 * Note that Lazy Load means that the code call does not actually execute the actual code logic, and the processing logic is executed when the data is actually fetched. Therefore, it is important to pay attention to these points:
 * 1. Each time the lazy collection is called to get the data, the logic of the anonymous function is executed once. Therefore, anonymous functions should not put too complicated logic.
 * 2. The logic that is encapsulated in the lambda at the code call is delayed until it is executed. It is important to note whether the closures carry resources, shared variables, etc., causing potential bugs. The lambda definition and the call location are as controllable as possible.
 * 3. The time complexity of some functions is O(n). If the Set is filtered, its size() function is O(n). Avoid calling these methods.

 */
public class Lazys {

    public static <F, T> Iterator<T> transform(@NotNull Iterator<F> fromIterator, @NotNull Function<? super F, ? extends T> function) {
        return Iterators.transform(fromIterator, function::apply);
    }

    public static <F, T> Iterable<T> transform(@NotNull Iterable<F> fromIterable, @NotNull Function<? super F, T> function) {
        return Iterables.transform(fromIterable, function::apply);
    }

    public static <F, T> Collection<T> transform(@NotNull Collection<F> fromCollection, @NotNull Function<? super F, T> function) {
        return Collections2.transform(fromCollection, function::apply);
    }

    public static <F, T> List<T> transform(@NotNull List<F> fromList, @NotNull Function<? super F, T> function) {
        return Lists.transform(fromList, function::apply);
    }

    public static <K, V1, V2> Map<K, V2> transform(@NotNull Map<K, V1> fromMap, @NotNull BiFunction<? super K, ? super V1, V2> transformer) {
        return Maps.transformEntries(fromMap, transformer::apply);
    }

    public static <K, V1, V2> NavigableMap<K, V2> transform(@NotNull NavigableMap<K, V1> fromMap, @NotNull BiFunction<? super K, ? super V1, V2> transformer) {
        return Maps.transformEntries(fromMap, transformer::apply);
    }

    public static <K, V1, V2> SortedMap<K, V2> transform(@NotNull SortedMap<K, V1> fromMap, @NotNull BiFunction<? super K, ? super V1, V2> transformer) {
        return Maps.transformEntries(fromMap, transformer::apply);
    }

    public static <K, V1, V2> Map<K, V2> transform(@NotNull Map<K, V1> fromMap, @NotNull Function<? super V1, V2> function) {
        return transform(fromMap, (k, v) -> function.apply(v));
    }

    public static <K, V1, V2> NavigableMap<K, V2> transform(@NotNull NavigableMap<K, V1> fromMap, @NotNull Function<? super V1, V2> function) {
        return transform(fromMap, (k, v) -> function.apply(v));
    }

    public static <K, V1, V2> SortedMap<K, V2> transform(@NotNull SortedMap<K, V1> fromMap, @NotNull Function<? super V1, V2> function) {
        return transform(fromMap, (k, v) -> function.apply(v));
    }

    public static <T> UnmodifiableIterator<T> filter(@NotNull Iterator<T> unfiltered, @NotNull Predicate<? super T> retainIfTrue) {
        return Iterators.filter(unfiltered, retainIfTrue::test);
    }

    public static <T> Iterable<T> filter(@NotNull Iterable<T> unfiltered, @NotNull Predicate<? super T> retainIfTrue) {
        return Iterables.filter(unfiltered, retainIfTrue::test);
    }

    public static <E> Set<E> filter(@NotNull Set<E> unfiltered, @NotNull Predicate<? super E> predicate) {
        return Sets.filter(unfiltered, predicate::test);
    }

    public static <E> SortedSet<E> filter(@NotNull SortedSet<E> unfiltered, @NotNull Predicate<? super E> predicate) {
        return Sets.filter(unfiltered, predicate::test);
    }

    public static <E> NavigableSet<E> filter(@NotNull NavigableSet<E> unfiltered, @NotNull Predicate<? super E> predicate) {
        return Sets.filter(unfiltered, predicate::test);
    }

    public static <K, V> Map<K, V> filterValues(@NotNull Map<K, V> unfiltered, @NotNull Predicate<? super V> valuePredicate) {
        return Maps.filterValues(unfiltered, valuePredicate::test);
    }

    public static <K, V> NavigableMap<K, V> filterValues(@NotNull NavigableMap<K, V> unfiltered, @NotNull Predicate<? super V> valuePredicate) {
        return Maps.filterValues(unfiltered, valuePredicate::test);
    }

    public static <K, V> SortedMap<K, V> filterValues(@NotNull SortedMap<K, V> unfiltered, @NotNull Predicate<? super V> valuePredicate) {
        return Maps.filterValues(unfiltered, valuePredicate::test);
    }

    public static <K, V> Map<K, V> filterKeys(@NotNull Map<K, V> unfiltered, @NotNull Predicate<? super K> keyPredicate) {
        return Maps.filterKeys(unfiltered, keyPredicate::test);
    }

    public static <K, V> NavigableMap<K, V> filterKeys(@NotNull NavigableMap<K, V> unfiltered, @NotNull Predicate<? super K> keyPredicate) {
        return Maps.filterKeys(unfiltered, keyPredicate::test);
    }

    public static <K, V> SortedMap<K, V> filterKeys(@NotNull SortedMap<K, V> unfiltered, @NotNull Predicate<? super K> keyPredicate) {
        return Maps.filterKeys(unfiltered, keyPredicate::test);
    }

    /**
     *
     * This feature is still unstable, use with caution
     *
     * @return returns an immutable List, where is the [start, end) growth sequence of the left closed right open interval
     */
    @Beta
    public static Range range(long start, long end) {
        return Range.range(start, end, 1);
    }

    /**
     * This feature is still unstable, use with caution
     *
     * Returns an immutable List. The number in the List is an arithmetic progression, starting at start (inclusive), incrementing step each time, and growing to end (not including).
     * step supports negative numbers, which will result in a decreasing number of arithmetic progressions.
     * If step is a positive number, the parameter start <= end is required, otherwise {@link IllegalArgumentException} is thrown
     * If step is negative, the parameter start >= end is required, otherwise {@link IllegalArgumentException} is thrown
     * @param start The first item of the arithmetic progression
     * @param end The last item of the arithmetic progression is the number less than and closest to the end
     * @param step step value

     */
    @Beta
    public static Range range(long start, long end, int step) {
        return Range.range(start, end, step);
    }

}
