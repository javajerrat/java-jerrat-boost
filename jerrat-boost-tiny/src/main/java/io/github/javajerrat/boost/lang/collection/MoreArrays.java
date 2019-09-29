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

import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import java.util.function.IntFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/20
 */
public class MoreArrays extends ArrayUtils {

    public MoreArrays() {
        throw new UnsupportedOperationException();
    }

    public static <T> T[] concat(@NotNull IntFunction<T[]> generator, T[] first, T[] second) {
        T[] result = generator.apply(first.length + second.length);
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    @SafeVarargs
    public static <T> T[] concat(@NotNull IntFunction<T[]> generator, T[]... arrays) {
        int length = 0;
        for (T[] array : arrays) {
            length += array.length;
        }
        T[] result = generator.apply(length);
        int pos = 0;
        for (T[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }


    public static boolean[] concat(boolean[]... arrays) {
        return Booleans.concat(arrays);
    }

    public static byte[] concat(byte[]... arrays) {
        return Bytes.concat(arrays);
    }

    public static char[] concat(char[]... arrays) {
        return Chars.concat(arrays);
    }

    public static short[] concat(short[]... arrays) {
        return Shorts.concat(arrays);
    }

    public static int[] concat(int[]... arrays) {
        return Ints.concat(arrays);
    }

    public static long[] concat(long[]... arrays) {
        return Longs.concat(arrays);
    }

    public static float[] concat(float[]... arrays) {
        return Floats.concat(arrays);
    }

    public static double[] concat(double[]... arrays) {
        return Doubles.concat(arrays);
    }

    public static String join(String separator, boolean... array) {
        return Booleans.join(separator, array);
    }

    public static String join(String separator, char... array) {
        return Chars.join(separator, array);
    }

    public static String join(String separator, int... array) {
        return Ints.join(separator, array);
    }

    public static String join(String separator, short... array) {
        return Shorts.join(separator, array);
    }

    public static String join(String separator, long... array) {
        return Longs.join(separator, array);
    }

    public static String join(String separator, float... array) {
        return Floats.join(separator, array);
    }

    public static String join(String separator, double... array) {
        return Doubles.join(separator, array);
    }
}
