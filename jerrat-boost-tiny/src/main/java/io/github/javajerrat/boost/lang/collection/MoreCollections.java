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
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import io.github.javajerrat.boost.lang.collection.iterable.FIterable;
import io.github.javajerrat.boost.lang.functions.IntFunction2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/19
 */
public class MoreCollections {

    public MoreCollections() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param args List of elements
     * @return  Returns a read-only List.
     * throwing a {@link UnsupportedOperationException } exception if an attempt is made to call a method that modifies the List.
     * The returned List is {@link java.util.RandomAccess }
     */
    @SafeVarargs
    @UnmodifiableView
    public static <T> List<T> listOf(T... args) {
        switch (args.length) {
            case 0:
                return Collections.emptyList();
            case 1:
                return Collections.singletonList(args[0]);
            default:
                return Collections.unmodifiableList(Arrays.asList(args));
        }
    }

    @Unmodifiable
    public static <T> List<T> listOf() {
        return Collections.emptyList();
    }

    /**
     * Wraps args and returns a fixed-size view of the list.
     * @param args  List of elements
     * @return The returned array is a view, meaning that if args is passed into the array,
     * the array is modified, the list is modified; the list is modified, and args is modified.
     */
    @SafeVarargs
    public static <T> List<T> fixedListOf(T... args) {
        if (args.length == 0) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(args);
        }
    }

    /**
     * The fixedListOfPrimitive family of functions is similar to {@link MoreCollections#fixedListOf(Object[])}.
     * The difference is that the argument to the function is an array of primitive types, avoiding boxing overhead.
     */
    public static List<Boolean> fixedListOfPrimitive(boolean... args) {
        return Booleans.asList(args);
    }

    public static List<Byte> fixedListOfPrimitive(byte... args) {
        return Bytes.asList(args);
    }

    public static List<Character> fixedListOfPrimitive(char... args) {
        return Chars.asList(args);
    }

    public static List<Short> fixedListOfPrimitive(short... args) {
        return Shorts.asList(args);
    }

    public static List<Integer> fixedListOfPrimitive(int... args) {
        return Ints.asList(args);
    }

    public static List<Long> fixedListOfPrimitive(long... args) {
        return Longs.asList(args);
    }

    public static List<Float> fixedListOfPrimitive(float... args) {
        return Floats.asList(args);
    }

    public static List<Double> fixedListOfPrimitive(double... args) {
        return Doubles.asList(args);
    }

    @Unmodifiable
    public static <T> List<T> repeatListOf(T o, int n) {
        return Collections.nCopies(n, o);
    }

    @SafeVarargs
    public static <T> ArrayList<T> arrayListOf(T... args) {
        return new ArrayList<>(listOf(args));
    }

    public static <T> ArrayList<T> arrayListOf(Iterable<? extends T> iterable) {
        ArrayList<T> list = new ArrayList<>();
        Iterables.addAll(list, iterable);
        return list;
    }

    @SafeVarargs
    public static <T> LinkedList<T> linkedListOf(T... args) {
        return new LinkedList<>(listOf(args));
    }

    public static <T> LinkedList<T> linkedListOf(Iterable<? extends T> iterable) {
        LinkedList<T> list = new LinkedList<>();
        Iterables.addAll(list, iterable);
        return list;
    }

    @SafeVarargs
    public static <T> CopyOnWriteArrayList<T> copyOnWriteArrayListOf(T... args) {
        return new CopyOnWriteArrayList<>(listOf(args));

    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteArrayListOf(Iterable<? extends T> iterable) {
        CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList<>();
        Iterables.addAll(list, iterable);
        return list;
    }

    @SafeVarargs
    @UnmodifiableView
    public static <T> Set<T> setOf(T... args) {
        switch (args.length) {
            case 0:
                return Collections.emptySet();
            case 1:
                return Collections.singleton(args[0]);
            default:
                // jdk11： https://github.com/AdoptOpenJDK/openjdk-jdk11/blob/master/src/java.base/share/classes/java/util/ImmutableCollections.java
                // Can consider copy over, here is the first simple implementation
                HashSet<T> set = new HashSet<>(args.length);
                set.addAll(listOf(args));
                return Collections.unmodifiableSet(set);
        }
    }

    @Unmodifiable
    public static <T> Set<T> setOf() {
        return Collections.emptySet();
    }

    @SafeVarargs
    public static <E> HashSet<E> hashSetOf(E... elements) {
        HashSet<E> set = new HashSet<>(elements.length / 2);
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> HashSet<E> hashSetOf(Iterable<? extends E> elements) {
        HashSet<E> set = new HashSet<>();
        Iterables.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E> LinkedHashSet<E> linkedHashSetOf(E... elements) {
        LinkedHashSet<E> set = new LinkedHashSet<>();
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> LinkedHashSet<E> linkedHashSetOf(Iterable<? extends E> elements) {
        LinkedHashSet<E> set = new LinkedHashSet<>();
        Iterables.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E extends Comparable> TreeSet<E> treeSetOf(E... elements) {
        TreeSet<E> set = new TreeSet<>();
        Collections.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E> TreeSet<E> treeSetOf(Comparator<? super E> comparator, E... elements) {
        TreeSet<E> set = new TreeSet<>(comparator);
        Collections.addAll(set, elements);
        return set;
    }

    public static <E extends Comparable> TreeSet<E> treeSetOf(Iterable<? extends E> elements) {
        TreeSet<E> set = new TreeSet<>();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E> TreeSet<E> treeSetOf(Iterable<? extends E> elements, Comparator<? super E> comparator) {
        TreeSet<E> set = new TreeSet<>(comparator);
        Iterables.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E> Set<E> identityHashSetOf(E... elements) {
        Set<E> set = Collections.newSetFromMap(new IdentityHashMap<>());
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> Set<E> identityHashSetOf(Iterable<E> elements) {
        Set<E> set = Collections.newSetFromMap(new IdentityHashMap<>());
        Iterables.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E> Set<E> concurrentHashSetOf(E... elements) {
        Set<E> set = ConcurrentHashMap.newKeySet();
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> Set<E> concurrentHashSetOf(Iterable<E> elements) {
        Set<E> set = ConcurrentHashMap.newKeySet();
        Iterables.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E extends Comparable> ConcurrentSkipListSet<E> concurrentSkipSetOf(E... elements) {
        ConcurrentSkipListSet<E> set = new ConcurrentSkipListSet<>();
        Collections.addAll(set, elements);
        return set;
    }

    @SafeVarargs
    public static <E> ConcurrentSkipListSet<E> concurrentSkipSetOf(Comparator<E> comparator, E... elements) {
        ConcurrentSkipListSet<E> set = new ConcurrentSkipListSet<>(comparator);
        Collections.addAll(set, elements);
        return set;
    }

    public static <E extends Comparable> ConcurrentSkipListSet<E> concurrentSkipSetOf(Iterable<E> elements) {
        ConcurrentSkipListSet<E> set = new ConcurrentSkipListSet<>();
        Iterables.addAll(set, elements);
        return set;
    }

    public static <E extends Comparable> ConcurrentSkipListSet<E> concurrentSkipSetOf(Iterable<E> elements, Comparator<E> comparator) {
        ConcurrentSkipListSet<E> set = new ConcurrentSkipListSet<>(comparator);
        Iterables.addAll(set, elements);
        return set;
    }

    @Unmodifiable
    @NotNull
    public static <K, V> Map<K, V> mapOf() {
        return Collections.emptyMap();
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1) {
        return Collections.singletonMap(k1, v1);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> map = new HashMap<>(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> map = new HashMap<>(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> map = new HashMap<>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Map<K, V> map = new HashMap<>(7);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        Map<K, V> map = new HashMap<>(8);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8
        , K k9, V v9) {
        Map<K, V> map = new HashMap<>(9);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return Collections.unmodifiableMap(map);
    }

    @Unmodifiable
    public static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
        K k9, V v9, K k10, V v10) {
        Map<K, V> map = new HashMap<>(9);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        return Collections.unmodifiableMap(map);
    }

    @Nullable
    public static <E> E first(Iterable<E> iterable) {
        Iterator<E> iterator = iterable.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Nullable
    public static <E> E last(Iterable<E> iterable) {
        if (iterable instanceof List) {
            List<E> list = (List<E>) iterable;
            return list.isEmpty() ? null : list.get(list.size() - 1);
        } else if (iterable instanceof SortedMap) {
            return  ((SortedSet<E>) iterable).last();
        } else {
            return Iterators.getLast(iterable.iterator());
        }
    }

    /**
     * Create an immutable Map
     * @param entries key-value pairs.
     * Construct with {@link MoreCollections#mapEntry(Object, Object)} or {@link Pair#of(Object, Object)}.
     * @return returns an immutable Map, Modifying it will throw a {@link UnsupportedOperationException } exception.
     */
    @Unmodifiable
    @SafeVarargs
    @NotNull
    public static <K, V> Map<K, V> mapOfEntries(Map.Entry<? extends K, ? extends V>... entries) {
        HashMap<K, V> map = new HashMap<>(entries.length);
        putEntries(map, entries);
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Entry<K, V> mapEntry(K key, V value) {
        return Pair.of(key, value);
    }

    private static <K, V> void putEntries(Map<K, V> map, Map.Entry<? extends K, ? extends V>[] entries) {
        for (Map.Entry<? extends K, ? extends V> e : entries) {
            map.put(e.getKey(), e.getValue());
        }
    }

    /* -------------------------------------------------------------------- */

    /**
     * The indexOf, find functions find elements from the collection that satisfy the given predicate predicate.
     * Consider those functions from two dimensions:
     * 1. There may be more than one element. Which one is returning to the front or which one is returning to the back?
     * The behavior of find and indexOf returns to the top; the behavior of findLast and lastIndexOf is the most backward.
     * 2. After finding the element, is it returning the element itself or returning the integer index of the element?
     * find, findLast is the return element itself, can not find return null; indexOf, lastIndexOf returns an integer index, can not find -1.
     */

    public static <T> int indexOf(@NotNull Iterable<T> iterable, @NotNull Predicate<? super T> predicate) {
        Iterator<T> iterator = iterable.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            T current = iterator.next();
            if (predicate.test(current)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(@NotNull List<T> list, @NotNull Predicate<? super T> predicate) {
        ListIterator<T> it = list.listIterator(list.size());
        int i = list.size();
        while (it.hasPrevious()) {
            T item = it.previous();
            i--;
            if (predicate.test(item)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(@NotNull Iterable<T> iterable, @NotNull Predicate<? super T> predicate) {
        if (iterable instanceof List) {
            return lastIndexOf((List<T>)iterable, predicate);
        }
        Iterator<T> iterator = iterable.iterator();
        int index = -1;
        for (int i = 0; iterator.hasNext(); i++) {
            T current = iterator.next();
            if (predicate.test(current)) {
                index = i;
            }
        }
        return index;
    }

    public static <T> @Nullable T find(@NotNull Iterable<T> iterable, @NotNull Predicate<? super T> predicate) {
        for (T t : iterable) {
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> @Nullable T findLast(@NotNull List<T> list, @NotNull Predicate<? super T> predicate) {
        ListIterator<T> it = list.listIterator(list.size());
        while (it.hasPrevious()) {
            T item = it.previous();
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public static <T> @Nullable T findLast(@NotNull Iterable<T> iterable, @NotNull Predicate<? super T> predicate) {
        if (iterable instanceof List) {
            return findLast((List<T>) iterable, predicate);
        } else {
            T r = null;
            for (T t : iterable) {
                if (predicate.test(t)) {
                    r = t;
                }
            }
            return r;
        }
    }

    /**
     * The number of times the statistical object o appears in the iterable object c, use .equals() to compare whether the two objects are equal.
     *
     * The functionality and even implementation is identical to {@link Collections#frequency(Collection, Object)}, and it supports Iterable.
     */
    public static int count(@NotNull Iterable<?> c, @Nullable Object o) {
        int result = 0;
        if (o == null) {
            for (Object e : c) {
                if (e == null) {
                    result++;
                }
            }
        } else {
            for (Object e : c) {
                if (o.equals(e)) {
                    result++;
                }
            }
        }
        return result;
    }

    public static <T> int count(@NotNull Iterable<T> iterable, @NotNull Predicate<? super T> predicate) {
        int cnt = 0;
        for (T t : iterable) {
            if (predicate.test(t)) {
                cnt++;
            }
        }
        return cnt;
    }

    @Contract(pure = true)
    public static <T> Map<T, Integer> countBy(@NotNull Iterable<T> iterable) {
        return countBy(iterable, Function.identity());
    }

    public static <T, U> Map<U, Integer> countBy(@NotNull Iterable<T> iterable, @NotNull Function<T, U> iteratee) {
        HashMap<U, Integer> counter = new HashMap<>();
        for (T item : iterable) {
            U key = iteratee.apply(item);
            int count = counter.getOrDefault(key, 0);
            count++;
            counter.put(key, count);
        }
        return counter;
    }

    /**
     * Whether there are not duplicates in the elements in a given collection.
     * @param collection Collection
     * @return Returns False if there is a duplicate, otherwise returns True
     */
    public static <T> boolean isDistinct(Collection<T> collection) {
        if (collection instanceof HashSet<?>) {
            return true;
        }
        HashSet<T> set = new HashSet<>(collection);
        return set.size() == collection.size();
    }

    @Beta
    public static <T> boolean isDistinct(@NotNull Iterable<T> iterable) {
        if (iterable instanceof Collection<?>) {
            return isDistinct((Collection<?>) iterable);
        } else {
            return isDistinct(iterable, Function.identity());
        }
    }


    @Beta
    public static <T, U> boolean isDistinct(@NotNull Iterable<T> iterable, @NotNull Function<T, U> iteratee) {
        HashMap<U, Integer> counter = new HashMap<>();
        for (T item : iterable) {
            U key = iteratee.apply(item);
            int count = counter.getOrDefault(key, 0);
            if (count >= 1) {
                return false;
            }
            counter.put(key, count + 1);
        }
        return true;
    }

    public static <T, U> Map<U, List<T>> groupBy(@NotNull Iterable<T> iterable, @NotNull Function<T, U> iteratee) {
        HashMap<U, List<T>> group = new LinkedHashMap<>();
        for (T item : iterable) {
            U key = iteratee.apply(item);
            List<T> matches = group.get(key);
            if (matches == null) {
                matches = new ArrayList<>();
                group.put(key, matches);
            }
            matches.add(item);
        }
        return group;
    }

    public static <T, U, V> Map<U, List<V>> groupBy(@NotNull Iterable<T> iterable, @NotNull Function<T, U> iteratee, @NotNull Function<T, V> valueFunction) {
        HashMap<U, List<V>> group = new LinkedHashMap<>();
        for (T item : iterable) {
            U key = iteratee.apply(item);
            List<V> matches = group.get(key);
            if (matches == null) {
                matches =  new ArrayList<>();
                group.put(key, matches);
            }
            matches.add(valueFunction.apply(item));
        }
        return group;
    }

    /**
     * keyBy function accepts an iterable object, computes each element using iteratee function,
     * and the result is treated as key, with the element as value, placed in the returned map.
     * @param iterable
     * @param keyFunction
     * @param <E>
     * @param <T>
     * @return
     */
    public static <E, T> Map<E, T> keyBy(@NotNull Iterable<? extends T> iterable, @NotNull Function<? super T, ? extends E> keyFunction) {
        LinkedHashMap<E, T> map = new LinkedHashMap<>();
        for (T item : iterable) {
            map.put(keyFunction.apply(item), item);
        }
        return map;
    }

    public static <E, T, V> Map<E, V> keyBy(@NotNull Iterable<? extends T> iterable,
        @NotNull Function<? super T, ? extends E> keyFunction,
        @NotNull Function<? super T, ? extends V> valueFunction) {
        LinkedHashMap<E, V> map = new LinkedHashMap<>();
        for (T item : iterable) {
            map.put(keyFunction.apply(item), valueFunction.apply(item));
        }
        return map;
    }


    /**
     * Create a Map with the key value inverted.
     * If there is a duplicate value in the source map, the latter value will overwrite the previous value.
     * @param map Map
     * @return Returns the new Map
     */
    public static <K, V> Map<V, K> invert(@NotNull Map<? extends K, ? extends V> map) {
        return invert(map, new HashMap<>());
    }

    /**
     * Invert the Map key value and put it in newMap.
     * If there is a duplicate value in the source map, the latter value will overwrite the previous value.
     * @param map Source map
     * @param newMap Destination map
     * @return Return parameter newMap
     */
    @Contract(mutates = "param2")
    public static <K, V, M extends Map<V, K>> M invert(@NotNull Map<? extends K, ? extends V> map, @NotNull M newMap) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            newMap.put(value, key);
        }
        return newMap;
    }

    public static <K, V, E> Map<E, List<K>> invertBy(@NotNull Map<? extends K, ? extends V> map, @NotNull Function<V, E> valueFunction) {
        return invertBy(map, valueFunction, new HashMap<>());
    }

    /**
     * Similar {@link MoreCollections#invert(Map, Map)}, But all values ​​in the source Map will be mapped by function valueFunction
     *
     * @param map Source map
     * @param valueFunction value function
     * @param newMap Destination map
     * @return Return parameter newMap
     */
    @Contract(mutates = "param3")
    public static <K, V, E, M extends Map<E, List<K>>> M invertBy(@NotNull Map<? extends K, ? extends V> map, @NotNull Function<? super V, ? extends E> valueFunction, @NotNull M newMap) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            K key = entry.getKey();
            E value = valueFunction.apply(entry.getValue());
            List<? super K> list = newMap.get(value);
            if (list == null) {
                ArrayList<K> newList = new ArrayList<>();
                newList.add(key);
                newMap.put(value, newList);
            } else {
                list.add(key);
            }
        }
        return newMap;
    }

    /**
     * Use the predicate predicate for each element in the iterable object iterable.
     *
     * This function is consistent with the stream operation {@link java.util.stream.Stream#anyMatch(Predicate)}.
     *
     * @param iterable iterable
     * @param predicate predicate
     * @return Returns true if there is an element that uses the predicate test to be true. Otherwise it returns false.
     * If the iterable object is empty, false is returned and the predicate is not executed.
     */
    public static <T> boolean some(Iterable<T> iterable, Predicate<? super T> predicate) {
        for (T item : iterable) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Use the predicate predicate for each element in the iterable object iterable.
     *
     * This function is consistent with the stream operation {@link java.util.stream.Stream#allMatch(Predicate)}.
     *
     * @param iterable iterable object
     * @param predicate predicate
     * @return Returns true only if all elements use the predicate test with a true result. Otherwise it returns false.
     * If the iterable object is empty, {@code true} is returned and the predicate is not executed.
     */
    public static <T> boolean every(Iterable<T> iterable, Predicate<? super T> predicate) {
        for (T item : iterable) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Split the array into multiple size-sized blocks and group them into a new array.
     * If array cannot be split into blocks of equal length, the last remaining elements will form a block.
     * @param list array to be processed
     * @param size The length of each array block
     * @return Please note that the return is a read-only view, dependent on the original list,
     * if you modify the original list will cause it to change.
     */
    @UnmodifiableView
    public static <T> List<List<T>> chunkView(@NotNull List<T> list, int size) {
        return Lists.partition(list, size);
    }

    @UnmodifiableView
    public static <T> Iterable<List<T>> chunkView(@NotNull Iterable<T> iterable, int size) {
        return Iterables.partition(iterable, size);
    }

    /**
     * Return a reversed list, and the returned list is the view of the original list,
     * and any one of the returned list and the original list is modified, causing another change.
     *
     * @param list original list
     * @return returns the inverted list
     */
    public static <T> List<T> reverseView(@NotNull List<T> list) {
        return Lists.reverse(list);
    }


    /**
     * Reverse the list, the function directly inverts the original list
     */
    @Contract(mutates = "param1")
    public static <T> void reverse(@NotNull List<T> list) {
        Collections.reverse(list);
    }

    /**
     * Reverse the list, except that the function does not modify the original list, nor does it rely on the original list,
     * but instead returns a new list.
     */
    @Contract(pure = true)
    public static <T> List<T> reversed(@NotNull Iterable<T> iterable) {
        ArrayList<T> newList = arrayListOf(iterable);
        reverse(newList);
        return newList;
    }

    public static <T> int binarySearch(@NotNull List<? extends T> list, @Nullable T key, @NotNull Comparator<? super T> c) {
        return Collections.binarySearch(list, key, c);
    }

    public static <T> int binarySearch(@NotNull List<? extends Comparable<? super T>> list, @Nullable T key) {
        return Collections.binarySearch(list, key);
    }

    /* -------------------------------------------------------------------- */
    /**
     * Set operations are divided into intersections, unions, difference sets, and symmetric difference sets.
     * All set operations do not change the original collection, nor rely on the original collection, but return a new collection
     */
    @Contract(pure = true)
    public static <E> Set<E> union(@NotNull Set<? extends E> set1, @NotNull Set<? extends E> set2) {
        return hashSetOf(Sets.union(set1, set2));
    }

    @Contract(pure = true)
    public static <E> Set<E> intersection(@NotNull Set<? extends E> set1, @NotNull Set<? extends E> set2) {
        return hashSetOf(Sets.intersection(set1, set2));
    }

    @Contract(pure = true)
    public static <E> Set<E> difference(@NotNull Set<? extends E> set1, @NotNull Set<? extends E> set2) {
        return hashSetOf(Sets.difference(set1, set2));
    }

    @Contract(pure = true)
    public static <E> Set<E> symmetricDifference(@NotNull Set<? extends E> set1, @NotNull Set<? extends E> set2) {
        return hashSetOf(Sets.symmetricDifference(set1, set2));
    }

    public static <E> Set<E> unionView(@NotNull Set<? extends E> set1, @NotNull Set<? extends E> set2) {
        return Sets.union(set1, set2);
    }

    public static <E> Set<E> intersectionView(@NotNull Set<E> set1, @NotNull Set<? extends E> set2) {
        return Sets.intersection(set1, set2);
    }

    public static <E> Set<E> differenceView(@NotNull Set<E> set1, @NotNull Set<? extends E> set2) {
        return Sets.difference(set1, set2);
    }

    public static <E> Set<E> symmetricDifferenceView(@NotNull Set<? extends E> set1, @NotNull Set<? extends E> set2) {
        return Sets.symmetricDifference(set1, set2);
    }


    /**
     * Concat a set of iterable objects
     * @param inputs a set of iterable objects
     * @return Concatenated iterable
     */
    @SafeVarargs
    @Contract(pure = true)
    public static <T> FIterable<T> concatView(Iterable<? extends T>... inputs) {
        return FIterable.from(FluentIterable.concat(inputs));
    }

    @Contract(pure = true)
    public static <T> FIterable<T> concatView(@NotNull Iterable<? extends T> a, @NotNull Iterable<? extends T> b) {
        return FIterable.from(FluentIterable.concat(a, b));
    }


    @Contract(pure = true)
    public static <T> FIterable<T> concatView(@NotNull Iterable<? extends Iterable<? extends T>> inputs) {
        return FIterable.from(FluentIterable.concat(inputs));
    }

    @Beta
    public static <T> List<T> mergeSorted(@NotNull Iterable<? extends Iterable<? extends T>> iterables, Comparator<? super T> comparator) {
        return mergeSortedView(iterables, comparator).toList();
    }

    @Beta
    public static <T> FIterable<T> mergeSortedView(@NotNull Iterable<? extends Iterable<? extends T>> iterables, Comparator<? super T> comparator) {
        return FIterable.from(Iterables.mergeSorted(iterables, comparator));
    }

    /**
     * Sorts only some of the elements in the list that satisfy the predicate condition.
     * For elements that do not satisfy the predicate condition, keep the original position.
     *
     * @param list Pending list
     * @param predicate Predicate
     * @param comparator Comparator
     */
    @Contract(mutates = "param1")
    public static <T> void skippedSort(@NotNull List<T> list, @NotNull Predicate<T> predicate, @NotNull Comparator<? super T> comparator) {
        List<Integer> itemIndex = new ArrayList<>();
        List<T> sortedItems = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                itemIndex.add(i);
                sortedItems.add(list.get(i));
            }
        }
        sortedItems.sort(comparator);
        for (int i = 0; i < sortedItems.size(); i++) {
            list.set(itemIndex.get(i), sortedItems.get(i));
        }
    }

    @Contract(mutates = "param3")
    public static <T> void copy(List<T> src, int srcPos, List<T> dest, int destPos, int length) {
        ListIterator<T> srcIt = src.listIterator(srcPos);
        ListIterator<T> destIt = dest.listIterator(destPos);

        for (int i = 0; i < length; i++) {
            T v = srcIt.next();
            destIt.next();
            destIt.set(v);
        }
    }

    @Contract(mutates = "param1")
    public static <T> void fill(@NotNull List<? super T> list, T obj) {
        Collections.fill(list, obj);
    }

    @Contract(mutates = "param1")
    @Beta
    public static <T> void fill(@NotNull List<? super T> list, int size, T obj) {
        Preconditions.checkArgument(size >= 0);
        int listSize = list.size();
        if (listSize < size) {
            Collections.fill(list, obj);
            list.addAll(repeatListOf(obj, size - listSize));
        } else if (listSize > size) {
            list.subList(size, list.size()).clear();
            Collections.fill(list, obj);
        } else {
            Collections.fill(list, obj);
        }
    }

    @Contract(mutates = "param1")
    public static <T> boolean replace(@NotNull List<T> list, @Nullable T oldVal, @Nullable T newVal) {
        return Collections.replaceAll(list, oldVal, newVal);
    }

    @Contract(mutates = "param1")
    public static <T> boolean replaceBy(@NotNull List<T> list, @Nullable T oldVal, @NotNull IntFunction2<? super T, ? extends T> converter) {
        return replaceWhileBy(list, (item) -> Objects.equals(item, oldVal), converter);
    }

    @Contract(mutates = "param1")
    public static <T> boolean replaceWhile(@NotNull List<T> list, @NotNull Predicate<? super T> predicate, @Nullable T newVal) {
        return replaceWhileBy(list, predicate, (ignored, ignoredIndex) -> newVal);
    }

    @Contract(mutates = "param1")
    public static <T> boolean replaceWhileBy(@NotNull List<T> list, @NotNull Predicate<? super T> predicate, @NotNull IntFunction2<? super T, ? extends T> converter) {
        boolean result = false;
        int i = 0;
        for (ListIterator<T> iterator = list.listIterator(); iterator.hasNext(); i++) {
            T item = iterator.next();
            if (predicate.test(item)) {
                iterator.set(converter.apply(item, i));
                result = true;
            }
        }
        return result;
    }

    @Contract(mutates = "param1")
    public static <T> boolean addAll(@NotNull Collection<T> addTo, @NotNull Iterable<? extends T> elementsToAdd) {
        return Iterables.addAll(addTo, elementsToAdd);
    }

    @Contract(pure = true)
    public static <T> @NotNull List<T> flatten(@NotNull List<? extends List<T>> list) {
        List<T> flatList = new ArrayList<>();
        for (List<T> itemList : list) {
            flatList.addAll(itemList);
        }
        return flatList;
    }

    /* -------------------------------------------------------------------- */

    /**
     * Each of the unmodifiable* functions is a decorator that decorates the incoming collection.
     * And its modification method throws a {@link UnsupportedOperationException } exception.
     * This function returns a read-only collection.
     * Note the difference between a read-only collection and an immutable collection.
     * If someone changes the data of the internal collection, the data obtained from the read-only collection will also change.
     */

    @UnmodifiableView
    public static <T> Iterator<T> unmodifiableIterator(@NotNull Iterator<? extends T> iterator) {
        return Iterators.unmodifiableIterator(iterator);
    }


    @UnmodifiableView
    public static <T> Iterable<T> unmodifiableIterable(@NotNull Iterable<? extends T> iterable) {
        return Iterables.unmodifiableIterable(iterable);
    }

    @UnmodifiableView
    public static <T> Collection<T> unmodifiableCollection(@NotNull Collection<? extends T> c) {
        return Collections.unmodifiableCollection(c);
    }

    @UnmodifiableView
    public static <T> List<T> unmodifiableList(@NotNull List<? extends T> list) {
        return Collections.unmodifiableList(list);
    }

    @UnmodifiableView
    public static <T> Set<T> unmodifiableSet(@NotNull Set<? extends T> set) {
        return Collections.unmodifiableSet(set);
    }

    @UnmodifiableView
    public static <T> SortedSet<T> unmodifiableSet(@NotNull SortedSet<T> s) {
        return Collections.unmodifiableSortedSet(s);
    }

    @UnmodifiableView
    public static <T> NavigableSet<T> unmodifiableSet(@NotNull NavigableSet<T> s) {
        return Collections.unmodifiableNavigableSet(s);
    }

    @UnmodifiableView
    public static <K, V> Map<K, V> unmodifiableMap(@NotNull Map<? extends K, ? extends V> m) {
        return Collections.unmodifiableMap(m);
    }

    @UnmodifiableView
    public static <K, V> SortedMap<K, V> unmodifiableMap(@NotNull SortedMap<K, ? extends V> m) {
        return Collections.unmodifiableSortedMap(m);
    }

    @UnmodifiableView
    public static <K, V> NavigableMap<K, V> unmodifiableMap(@NotNull NavigableMap<K, ? extends V> m) {
        return Collections.unmodifiableNavigableMap(m);
    }

    /**
     *
     * Each of the synchronized* functions is a decorator that decorates incoming collections so that each method is synchronized via synchronized.
     */
    /* -------------------------------------------------------------------- */

    public static <E> List<E> synchronizedList(@NotNull List<E> list) {
        return Collections.synchronizedList(list);
    }


    public static <E> Set<E> synchronizedSet(@NotNull Set<E> set) {
        return Collections.synchronizedSet(set);
    }

    public static <T> SortedSet<T> synchronizedSet(@NotNull SortedSet<T> s) {
        return Collections.synchronizedSortedSet(s);
    }

    public static <T> NavigableSet<T> synchronizedSet(@NotNull NavigableSet<T> s) {
        return Collections.synchronizedNavigableSet(s);
    }

    public static <K, V> Map<K, V> synchronizedMap(@NotNull Map<K, V> m) {
        return Collections.synchronizedMap(m);
    }

    public static <K, V> SortedMap<K, V> synchronizedMap(@NotNull SortedMap<K, V> m) {
        return Collections.synchronizedSortedMap(m);
    }

    public static <K, V> NavigableMap<K, V> synchronizedMap(@NotNull NavigableMap<K, V> m) {
        return Collections.synchronizedNavigableMap(m);
    }

    /* -------------------------------------------------------------------- */
    /**
     * Each of the checked* functions is a decorator that decorates the incoming collection.
     * The decorated collection will check its type at runtime when the element is inserted.
     * If not, throw {@link ClassCastException}.
     * Useful when you are unable to use a static type system to ensure type safety or to prevent type safety from being circumvented.
     */
    public static <E> Collection<E> checkedCollection(@NotNull Collection<E> c, @NotNull Class<E> type) {
        return Collections.checkedCollection(c, type);
    }

    public static <E> Queue<E> checkedQueue(@NotNull Queue<E> queue, @NotNull Class<E> type) {
        return Collections.checkedQueue(queue, type);
    }


    public static <E> List<E> checkedList(@NotNull List<E> list, @NotNull Class<E> type) {
        return Collections.checkedList(list, type);
    }

    public static <E> Set<E> checkedSet(@NotNull Set<E> s, @NotNull Class<E> type) {
        return Collections.checkedSet(s, type);
    }

    public static <E> SortedSet<E> checkedSet(@NotNull SortedSet<E> s, @NotNull Class<E> type) {
        return Collections.checkedSortedSet(s, type);
    }

    public static <E> NavigableSet<E> checkedSet(@NotNull NavigableSet<E> s, @NotNull Class<E> type) {
        return Collections.checkedNavigableSet(s, type);
    }

    public static <K, V> Map<K, V> checkedMap(@NotNull Map<K, V> m, @NotNull Class<K> keyType, @NotNull Class<V> valueType) {
        return Collections.checkedMap(m, keyType, valueType);
    }

    public static <K, V> SortedMap<K, V> checkedMap(@NotNull SortedMap<K, V> m, @NotNull Class<K> keyType, @NotNull Class<V> valueType) {
        return Collections.checkedSortedMap(m, keyType, valueType);
    }

    public static <K, V> NavigableMap<K, V> checkedMap(@NotNull NavigableMap<K, V> m, @NotNull Class<K> keyType, @NotNull Class<V> valueType) {
        return Collections.checkedNavigableMap(m, keyType, valueType);
    }

}

