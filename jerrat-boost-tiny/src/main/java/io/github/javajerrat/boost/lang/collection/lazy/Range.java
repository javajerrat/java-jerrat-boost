package io.github.javajerrat.boost.lang.collection.lazy;

import com.google.common.annotations.Beta;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.Objects;
import java.util.RandomAccess;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 18-9-28
 *
 * Warning: This feature is still unstable
 */

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Beta
public class Range extends AbstractList<Long> implements RandomAccess {

    public static Range range(long start, long end) {
        return new Range(start, end, 1);
    }

    public static Range range(long start, long end, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("The step cannot be 0");
        }

        if (end != start &&
            (end - start > 0 != step > 0)) {
            throw new IllegalArgumentException("The step given will result in infinite flow");
        }
        return new Range(start, end, step);
    }

    private long start;
    private long end;
    private int step;

    @Override
    public Long get(int index) {
        return start + index * step;
    }

    @NotNull
    @Override
    public Iterator<Long> iterator() {
        return new RangeIterator();
    }

    @Override
    public int size() {
        long diff = Math.abs(end - start);
        int step = Math.abs(this.step);
        return (int) (diff % step == 0 ? (diff / step) : (diff / step + 1));
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Range) {
            Range other = (Range) o;
            if (isEmpty() && other.isEmpty()) {
                return true;
            }

            int last = size() - 1;
            return size() == other.size() &&
                start == other.start &&
                get(last).equals(other.get(last)) &&
                step == other.step;
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, step);
    }

    class RangeIterator implements Iterator<Long> {

        private long next = start;

        @Override
        public boolean hasNext() {
            return next < end;
        }

        @Override
        public Long next() {
            long next = this.next;
            this.next += step;
            return next;
        }
    }
}
