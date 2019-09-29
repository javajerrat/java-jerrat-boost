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



package io.github.javajerrat.boost.lang.string;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/16
 *
 * Although jdk and guava preset some default character sets, they lack common character sets such as GBK.
 *
 * This class provides two sets of preset character sets, the constant is a string type, and the function is a {@link Charset } type.
 * Designing a function is a compromise because the jdk standard only describes five character sets that must be supported.
 * If the non-standard character set is designed to be a constant, it will report an error directly when the class is initialized.
 *
 * @see org.apache.commons.io.Charsets
 * @see com.google.common.base.Charsets
 * @see java.nio.charset.StandardCharsets
 */
public class Charsets {

    public Charsets() {
        throw new UnsupportedOperationException();
    }

    /**
     * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the
     * Unicode character set
     */
    public static Charset US_ASCII() {
        return StandardCharsets.US_ASCII;
    }

    /**
     * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
     */
    public static Charset ISO_8859_1() {
        return StandardCharsets.ISO_8859_1;
    }

    /**
     * Eight-bit UCS Transformation Format
     */
    public static Charset UTF_8()  {
        return StandardCharsets.UTF_8;
    }
    /**
     * Sixteen-bit UCS Transformation Format, big-endian byte order
     */
    public static Charset UTF_16BE() {
        return StandardCharsets.UTF_16BE;
    }

    /**
     * Sixteen-bit UCS Transformation Format, little-endian byte order
     */
    public static Charset UTF_16LE()  {
        return StandardCharsets.UTF_16LE;
    }

    /**
     * Sixteen-bit UCS Transformation Format, byte order identified by an
     * optional byte-order mark
     */
    public static Charset UTF_16()  {
        return StandardCharsets.UTF_16;
    }

    private static final AtomicReference<Charset> cacheGbk = new AtomicReference<>();
    private static final AtomicReference<Charset> cacheGb2312 = new AtomicReference<>();
    private static final AtomicReference<Charset> cacheCp936 = new AtomicReference<>();
    private static final AtomicReference<Charset> cacheGB18030 = new AtomicReference<>();
    private static final AtomicReference<Charset> cacheBig5 = new AtomicReference<>();

    public static Charset GBK() {
        return dcl(cacheGbk, GBK);
    }

    public static Charset GB2312() {
        return dcl(cacheGb2312, GB2312);
    }


    public static Charset CP936() {
        return dcl(cacheCp936, CP936);
    }


    public static Charset GB18030() {
        return dcl(cacheGB18030, GB18030);
    }

    public static Charset BIG5() {
        return dcl(cacheBig5, BIG5);
    }

    private static Charset dcl(AtomicReference<Charset> cache, String name) {
        if (cache.get() == null) {
            synchronized (Charsets.class) {
                if (cache.get() == null) {
                    cache.set(Charset.forName(name));
                }
            }
        }
        return cache.get();
    }

    public static final String US_ASCII = "US-ASCII";

    public static final String ISO_8859_1 = "ISO-8859-1";

    public static final String UTF_8 = "UTF-8";

    public static final String UTF_16BE = "UTF-16BE";

    public static final String UTF_16LE = "UTF-16LE";

    public static final String UTF_16 = "UTF-16";

    public static final String GBK = "GBK";

    public static final String GB2312 = "GB2312";

    public static final String CP936 = "CP936";

    public static final String GB18030 = "GB18030";

    public static final String BIG5 = "BIG5";

    public static Charset defaultCharset() {
        return Charset.defaultCharset();
    }

    public static SortedMap<String, Charset> availableCharsets() {
        return Charset.availableCharsets();
    }

    public static boolean isSupported(String charsetName) {
        return Charset.isSupported(charsetName);
    }

    public static Charset forName(String charsetName) {
        return Charset.forName(charsetName);
    }
}
