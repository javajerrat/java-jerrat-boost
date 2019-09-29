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

package io.github.javajerrat.boost.lang.collection.iterable;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.github.javajerrat.boost.lang.collection.MoreCollections;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/8/8
 *
 * FIterable is designed to be the return value of a lazy algorithm.
 * Similar to {@link com.google.common.collect.FluentIterable}, but this class removes most of the O(N) operations.
 *
 * If the developer needs to use the O(N) function, it needs to call {@link Iterables }. In this way, developers will be more aware of what they are doing O(N) operations now.
 *
 * If you need to do a complex one-time operation, you need {@link FIterable#toList()} to trigger the lazy algorithm to put the data into the list for subsequent operations.
 * If the operation can be done in one pass, either traverse the iterable object directly or use complex stream operations by using {@link FIterable#stream()}.

 */
public class FIterable<E> implements Iterable<E> {

    private final Iterable<E> iterableDelegate;

    /** Constructor for use by subclasses. */
    protected FIterable() {
        this.iterableDelegate = Collections.emptyList();
    }

    FIterable(@NotNull @NonNull Iterable<E> iterable) {
        this.iterableDelegate = iterable;
    }

    public static <E> FIterable<E> of() {
        return new FIterable<>();
    }

    private Iterable<E> getDelegate() {
        return iterableDelegate;
    }

    public static <E> FIterable<E> from(final Iterable<E> iterable) {
        return (iterable instanceof FIterable) ? (FIterable<E>)iterable : new FIterable<>(iterable);
    }

    @Override
    public String toString() {
        return iterableDelegate.toString();
    }

    public final Stream<E> stream() {
        return Streams.stream(getDelegate());
    }

    public final boolean isEmpty() {
        return !getDelegate().iterator().hasNext();
    }

    public final List<E> toList() {
        return MoreCollections.arrayListOf(getDelegate());
    }

    public final Set<E> toSet() {
        return MoreCollections.linkedHashSetOf(getDelegate());
    }

    public final E[] toArray(Class<E> type) {
        return Iterables.toArray(getDelegate(), type);
    }

    public final void copyInto(Collection<? super E> collection) {
        Iterables.addAll(collection, getDelegate());
    }

    public final FIterable<E> filter(Predicate<? super E> predicate) {
        return from(Iterables.filter(getDelegate(), predicate::test));
    }

    public final <T> FIterable<T> transform(Function<? super E, T> function) {
        return from(Iterables.transform(getDelegate(), function::apply));
    }

    public final E reduce(E identity, BinaryOperator<E> accumulator) {
        return stream().reduce(identity, accumulator);
    }

    public final @Nullable E first() {
        return Iterables.getFirst(getDelegate(), null);
    }

    public final @Nullable E last() {
        return MoreCollections.last(getDelegate());
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return getDelegate().iterator();
    }
}
