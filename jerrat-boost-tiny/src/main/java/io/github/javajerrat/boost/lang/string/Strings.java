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

import com.google.common.annotations.Beta;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.github.javajerrat.boost.lang.collection.iterable.FIterable;
import io.github.javajerrat.boost.lang.functions.IntFunction2;
import io.github.javajerrat.boost.lang.string.ext.StringLookups;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2018/12/24
 */
public class Strings {

    public Strings() {
        throw new UnsupportedOperationException();
    }

    /**
     * Eg: format("My name is {name}", Maps.of("name", "tom")) -> "My name is tom"
     *
     * If the corresponding attribute is not found in the given map, the format is returned as it is:
     * format("My name is {name}", Maps.of()) -> "My name is {name}"
     *
     * @param s String format
     * @param values values
     * @return Formatted string
     */
    public static String format(@NotNull String s, @NotNull Map<String, Object> values) {
        return format(s, FormatConfig.of().map(values));
    }

    /**
     * Eg: format("My name is {0} and my job is {1}", "tom", "teacher") -> "My name is tom and my job is teacher"
     *
     * If the corresponding attribute is not found in the given map, the format is returned as it is:
     * format("My name is {0}") -> "My name is {0}"
     *
     * @param s String format
     * @param args values
     * @return Formatted string
     */
    public static String format(@NotNull String s, Object... args) {
        return format(s, FormatConfig.of().stringLookup(StringLookups.fromList(Arrays.asList(args))));
    }

    @Data
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "of")
    public static class FormatConfig {

        /**
         * The left boundary character used to define the variable
         */
        String leftPrefix = "{";

        /**
         * The right boundary character used to define the variable
         */
        String rightPrefix = "}";

        /**
         * The escape character
         */
        char escape = '\\';

        /**
         * Whether to recursively replace the value after the variable is replaced again
         */
        boolean recursiveInValues = false;

        /**
         * Whether to recursively replace the variable name after the variable is replaced again
         */
        boolean recursiveInVariables = false;

        /**
         * When the placeholder does not exist, if this setting is turned on, an exception is thrown, otherwise it remains as it is.
         */
        boolean throwExIfKeyNotFound = false;

        StringLookup stringLookup = StringLookups.emptyStringLookup();

        public FormatConfig map(Map<String, Object> values) {
            this.stringLookup = StringLookups.fromMap(values);
            return this;
        }
    }

    public static String format(@NotNull String s, FormatConfig config) {
        StringLookup stringLookup = StringLookups.throwExStringLookup(config.stringLookup, config.throwExIfKeyNotFound);
        StringSubstitutor sub = new StringSubstitutor(stringLookup, config.leftPrefix, config.rightPrefix, config.escape)
            .setEnableSubstitutionInVariables(config.recursiveInVariables)
            .setDisableSubstitutionInValues(!config.recursiveInValues);
        return sub.replace(s);
    }


    public static String replace(@NotNull CharSequence str, int start, int end, @NotNull String replacement) {
        return new StringBuilder(str)
            .replace(start, end, replacement)
            .toString();
    }

    public static String replace(@NotNull String text, @NotNull String searchString, @NotNull String replacement) {
        return replace(text, searchString, replacement, ReplaceConfig.of());
    }

    @Data
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "of")
    public static class ReplaceConfig {

        private int max = -1;

        private boolean ignoreCase = false;
    }

    public static String replace(@NotNull String text, @NotNull String searchString, @NotNull String replacement, @NotNull ReplaceConfig config) {
        if (config.ignoreCase) {
            return StringUtils.replaceIgnoreCase(text, searchString, replacement, config.max);
        } else {
            return StringUtils.replace(text, searchString, replacement, config.max);
        }
    }

    public static String replace(@NotNull String text, @NotNull String[] searchList, @NotNull String[] replacementList) {
        return StringUtils.replaceEach(text, searchList, replacementList);
    }


    public static FIterable<String> split(@NotNull String string, @NotNull String sep) {
        return split(string, sep, SplitConfig.of());
    }

    @Data
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "of")
    public static class SplitConfig {

        /**
         * Whether to make a {@link String#trim() }
         */
        boolean trimResults = false;

        /**
         * Whether to filter empty strings
         */
        boolean filterEmpty = true;

        /**
         * Only the limit slices are retained in the result, and 0 means no setting.
         */
        int limit = 0;
    }


    public static FIterable<String> split(@NonNull String str, @NonNull String sep, @NonNull SplitConfig config) {
        if (str.isEmpty()) {
            return FIterable.of();
        }

        Splitter splitter = Splitter.on(sep);
        if (config.trimResults) {
            splitter = splitter.trimResults();
        }
        if (config.filterEmpty) {
            splitter = splitter.omitEmptyStrings();
        }
        if (config.limit > 0) {
            splitter = splitter.limit(config.limit);
        }
        return FIterable.from(splitter.split(str));
    }


    public static String join(@NotNull CharSequence delimiter, CharSequence... elements) {
        return String.join(delimiter, elements);
    }

    public static String join(@NotNull CharSequence delimiter, @NotNull Iterable<? extends CharSequence> elements) {
        return String.join(delimiter, elements);
    }

    /**
     * @see Strings#slice(String, int, int)
     */
    public static String slice(@NotNull String str, int start) {
        return StringUtils.substring(str, start);
    }

    /**
     *
     * This substring function differs from {@link String#substring } in that:
     * 1. If it is exceeded, it is processed as the end of the string instead of throwing an exception
     * 2. Negative values ​​can be used, indicating the number from the end of the string
     *
     * @param str string
     * @param start Start of string slice
     * @param end End of string slice
     * @return String slice
     */
    public static String slice(@NotNull String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    /**
     * Encode, decode series, byte string and string conversion.
     * Jdk related APIs, using different default values ​​on different platforms, which causes the same code to behave differently on different platforms.
     * But this function always uses UTF-8 as the default.
     *
     * @param bytes Bytes
     * @return String
     */
    public static String encode(@NotNull byte[] bytes) {
        return new String(bytes, Charsets.UTF_8());
    }

    public static String encode(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }

    public static byte[] decode(@NotNull String str) {
        return str.getBytes(Charsets.UTF_8());
    }

    public static byte[] decode(@NotNull String str, @NotNull Charset charset) {
        return str.getBytes(charset);
    }


    /**
     * Compile regular expressions and add support for PCRE-style pattern modifiers:
     * i 对应 {@link Pattern.CASE_INSENSITIVE }
     * d 对应 {@link Pattern.UNIX_LINES }
     * u 对应 {@link Pattern.UNICODE_CASE }
     * x 对应 {@link Pattern.COMMENTS }
     * m 对应 {@link Pattern.MULTILINE }
     * s 对应 {@link Pattern.DOTALL }
     *
     * @param reg Regular expression

     * @return * Compiled {@link Pattern} object
     */
    @Beta
    public static Pattern regex(@NotNull String reg) {
        if (reg.isEmpty()) {
            return Pattern.compile(reg);
        }

        char delimiter = reg.charAt(0);
        int end = reg.lastIndexOf(delimiter);
        if (end <= 0) {
            return Pattern.compile(reg);
        }

        String realReg = reg.substring(1, end);
        String flags = reg.substring(end + 1);

        int bits = 0;
        for (int i = 0; i < flags.length(); i++) {
            char c = flags.charAt(i);
            switch (c) {
                case 'i':
                    bits |= Pattern.CASE_INSENSITIVE; break;
                case 'd':
                    bits |= Pattern.UNIX_LINES; break;
                case 'u':
                    bits |= Pattern.UNICODE_CASE; break;
                case 'x':
                    bits |= Pattern.COMMENTS; break;
                case 'm':
                    bits |= Pattern.MULTILINE; break;
                case 's':
                    bits |= Pattern.DOTALL; break;
                default:
                    throw new PatternSyntaxException("Unknown pattern modifier: " + c, reg, end + 1 + i);

            }
        }
        return Pattern.compile(realReg, bits);
    }

    /**
     * Match regular expressions.
     *
     * There may be multiple fragments in a string that are matched, so the function returns a list.
     * For each segment on the match, the MatchResult in the corresponding list can be used to obtain each packet within the segment, and the packet content and packet offset can be obtained.
     *
     * @param str requires a matching string
     * @param pattern regular expression
     * @return matching list
     */
    public static List<MatchResult> regMatch(@NotNull String str, @NotNull Pattern pattern) {
        Matcher m = pattern.matcher(str);
        ArrayList<MatchResult> list = new ArrayList<>();
        if (m.find()) {
            do {
                list.add(m.toMatchResult());
            } while (m.find());
            return list;
        } else {
            return list;
        }
    }

    /**
     * Returns the position of the first matching result that satisfies the condition in the entire string
     * If there is no match, -1 is returned.
     *
     * @param str requires a matching string
     * @param pattern regular expression
     * @return The first match results in the offset of the string. If there is no matching substring, return -1
     */
    public static int regSearch(@NotNull String str, @NotNull Pattern pattern) {
        Matcher match = pattern.matcher(str);
        return match.find() ? match.start(0) : -1;
    }

    @Beta
    public static FIterable<String> regSplit(@NotNull String str, @NotNull Pattern pattern) {
        return regSplit(str, pattern, 0);
    }

    /**
     * FIXME: This function should have a feature that returns an offset
     * FIXME: This function should be implemented as lazy parsing, you need to rewrite it yourself, the implementation is a bit difficult, here is the first implementation
     * @param input string that needs to match
     * @param pattern regular expression
     * @param limit Maximum number of cuts
     * @return The lazy result of the segmentation
     */
    @Beta
    public static FIterable<String> regSplit(@NotNull CharSequence input, @NotNull Pattern pattern, int limit) {
        String[] segments = pattern.split(input, limit);
        return FIterable.from(Arrays.asList(segments));
    }


    /**
     * Regular expression substitution. Behavior and consistency {@link String#replaceAll(String, String)}
     */
    public static String regReplace(@NotNull String str, @NotNull Pattern pattern, @NotNull String replacement) {
        return pattern.matcher(str).replaceAll(replacement);
    }


    /**
     * Regular expression substitution, similar to {@link Strings#regReplace(String, Pattern, String)}
     * Use the callback function to replace the original string fragment with the desired string, which is more flexible and powerful
     *
     * Thanks for https://my.oschina.net/jsan/blog/185189
     *
     * @param string string to be replaced
     * @param pattern regular expression
     * @param replacement callback function
     * @return returns the replaced string
     */
    @Beta
    public static String regReplace(@NotNull String string, @NotNull Pattern pattern, @NotNull IntFunction2<MatchResult, String> replacement) {
        Matcher m = pattern.matcher(string);
        if (m.find()) {
            StringBuffer sb = new StringBuffer();
            int index = 0;
            do {
                m.appendReplacement(sb, replacement.apply(m.toMatchResult(), index++));
            } while (m.find());
            m.appendTail(sb);
            return sb.toString();
        } else {
            return string;
        }
    }

    public static int count(@NotNull CharSequence str, @NotNull CharSequence sub) {
        return StringUtils.countMatches(str, sub);
    }

    public static String reverse(@NotNull String str) {
        return new StringBuilder(str).reverse().toString();
    }

    public static String repeat(@NotNull String string, int count) {
        return com.google.common.base.Strings.repeat(string, count);
    }

    public static String padStart(@NotNull String string, int minLength, char padChar) {
        return com.google.common.base.Strings.padStart(string, minLength, padChar);
    }


    public static String padEnd(String string, int minLength, char padChar) {
        return com.google.common.base.Strings.padEnd(string, minLength, padChar);
    }

    enum StripMode {

        /**
         * Clear only the white space at the front of the string
         */
        START,

        /**
         * Clear only whitespace characters at the end of the string
         */
        END,

        /**
         * Clear both the whitespace characters at the beginning and end of the string
         */
        ALL
    }

    public static String strip(@NotNull String str) {
        return strip(str, StripMode.ALL, null);
    }

    public static String strip(@NotNull String str, @NotNull StripMode mode) {
        return strip(str, mode, null);
    }

    public static String strip(@NotNull String str, @NotNull StripMode mode, @Nullable String stripChars) {
        switch (mode) {
            case ALL:
                str = StringUtils.stripStart(str, stripChars);
                return StringUtils.stripEnd(str, stripChars);
            case START:
                return StringUtils.stripStart(str, stripChars);
            case END:
                return StringUtils.stripEnd(str, stripChars);
            default:
                throw new AssertionError();
        }
    }

    public static String commonPrefix(CharSequence a, CharSequence b) {
        return com.google.common.base.Strings.commonPrefix(a, b);
    }

    public static String commonSuffix(CharSequence a, CharSequence b) {
        return com.google.common.base.Strings.commonSuffix(a, b);
    }

    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    @UnmodifiableView
    public static List<Character> list(CharSequence sequence) {
        return Lists.charactersOf(sequence);
    }
}
