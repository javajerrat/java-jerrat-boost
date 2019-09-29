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


package io.github.javajerrat.boost.basetools.rand;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import io.github.javajerrat.boost.lang.collection.MoreArrays;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.Nullable;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/27
 */
public class RandomHelper {

    /**
     * @see Random#BadBound
     * @see Random#BadRange
     * @see Random#BadSize
     */
    static final String BadBound = "bound must be positive";
    static final String BadRange = "bound must be greater than origin";
    static final String BadSize = "size must be non-negative";

    private static RandomHelper DEFAULT_INSTANCE = new RandomHelper(ThreadLocalRandom::current);
    private static RandomHelper DEFAULT_INSTANCE_WITH_SECURE_RANDOM = RandomHelper.of(new SecureRandom());

    public static RandomHelper of() {
        return DEFAULT_INSTANCE;
    }

    public static RandomHelper ofSecure() {
        return DEFAULT_INSTANCE_WITH_SECURE_RANDOM;
    }

    public static RandomHelper of(Random random) {
        return new RandomHelper(() -> random);
    }

    public static RandomHelper of(Supplier<Random> randomSupplier) {
        return new RandomHelper(randomSupplier);
    }

    private Supplier<Random> randomSupplier;

    private RandomHelper(Supplier<Random> randomSupplier) {
        this.randomSupplier = randomSupplier;
    }


    private Random random() {
        return randomSupplier.get();
    }

    public boolean nextBoolean() {
        return random().nextBoolean();
    }

    public byte[] nextBytes(int count) {
        Preconditions.checkArgument(count >= 0, BadSize);
        final byte[] result = new byte[count];
        random().nextBytes(result);
        return result;
    }

    public int nextInt(int startInclusive, int endExclusive) {
        Preconditions.checkArgument(endExclusive >= startInclusive, BadRange);
        Preconditions.checkArgument(startInclusive >= 0, BadBound);

        if (startInclusive == endExclusive) {
            return startInclusive;
        }
        return startInclusive + random().nextInt(endExclusive - startInclusive);
    }

    public int nextInt() {
        return random().nextInt();
    }

    public long nextLong(long startInclusive, long endExclusive) {
        Preconditions.checkArgument(endExclusive >= startInclusive, BadRange);
        Preconditions.checkArgument(startInclusive >= 0, BadBound);

        return random().nextLong() % (endExclusive - startInclusive) + startInclusive;
    }

    public long nextLong() {
        return random().nextLong();
    }

    public double nextDouble(double startInclusive, double endInclusive) {
        Preconditions.checkArgument(endInclusive >= startInclusive, BadRange);
        Preconditions.checkArgument(startInclusive >= 0, BadBound);

        return RandomUtils.nextDouble(startInclusive, endInclusive);
    }


    public double nextDouble() {
        return random().nextDouble();
    }

    public float nextFloat(float startInclusive, float endInclusive) {
        Preconditions.checkArgument(endInclusive >= startInclusive, BadRange);
        Preconditions.checkArgument(startInclusive >= 0, BadBound);

        return RandomUtils.nextFloat(startInclusive, endInclusive);
    }

    public float nextFloat() {
        return random().nextFloat();
    }


    public <T> void shuffle(List<T> list) {
        Collections.shuffle(list, random());
    }

    /**
     * Get a random element from the List.
     */
    public <T> @Nullable T sample(List<T> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            int index = random().nextInt(list.size());
            return list.get(index);
        }
    }

    /**
     * @return 十分不稳定。不稳定的因素参见 {@link RandomHelper#nextUniqueInts }
     */
    @Beta
    public <T> List<T> sampleSize(List<T> list, int size) {
        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        List<Integer> sampleIndexes = nextUniqueInts(list.size(), size);
        for (int index : sampleIndexes) {
            result.add(list.get(index));
        }
        return result;
    }

    /**
     * TODO: Pending test
     * 十分的不稳定。自创的算法，正确性有待验证，还有些许问题，可能不满足某些概率性质
     *
     */
    @Beta
    public List<Integer> nextUniqueInts(int bound, int size) {
        int[] result = new int[size];
        nextUniqueIntsImpl(random(), bound, size, result, 0);
        return Ints.asList(result);
    }

    private void nextUniqueIntsImpl(Random random, int bound, int size, int[] result, int startPos) {
        if (bound < 32) {
            nextUniqueIntsImplForLess(random, bound, size, result, startPos);
        } else {
            int boundLeft = bound / 2;
            // 理论上这里要算概率
            int sizeLeft = size / 2;
            int sizeRight = size - sizeLeft;
            nextUniqueIntsImpl(random, boundLeft, sizeLeft, result, startPos);
            nextUniqueIntsImpl(random, boundLeft, bound, sizeRight, result, startPos + sizeLeft);
        }
    }

    private void nextUniqueIntsImpl(Random random, int start, int bound, int size, int[] result, int startPos) {
        nextUniqueIntsImpl(random, bound - start, size, result, startPos);
        for (int i = startPos; i < startPos + size; i++) {
            result[i] += start;
        }
    }

    private void nextUniqueIntsImplForLess(Random random, int bound, int size, int[] result, int startPos) {
        int[] numbers = new int[bound];
        for (int i = 0; i <= bound; i++) {
            numbers[i] = i;
        }
        for (int i = 0; i < size; i++) {
            int n = nextInt(random, i + 1, bound);
            MoreArrays.swap(numbers, i, n);
        }
        System.arraycopy(numbers, 0, result, startPos, size);
    }

    private int nextInt(Random random, int start, int bound) {
        return random.nextInt(bound - start) + start;
    }
}

