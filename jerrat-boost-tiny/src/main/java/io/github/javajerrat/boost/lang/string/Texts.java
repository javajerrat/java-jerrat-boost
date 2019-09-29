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

import com.google.common.base.CaseFormat;
import io.github.javajerrat.boost.lang.string.enums.SimilarityAlgorithmEnum;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.diff.StringsComparator;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/17
 */
public class Texts {

    public Texts() {
        throw new UnsupportedOperationException();
    }


    /**
     * @see com.google.common.base.CaseFormat
     */
    public static String convertNamedStyle(String str, CaseFormat from, CaseFormat to) {
        return from.to(to, str);
    }

    public static String capitalize(String str) {
        return WordUtils.capitalize(str);
    }

    public static String uncapitalize(String str) {
        return WordUtils.uncapitalize(str);
    }

    public static String escapeJava(String java) {
        return StringEscapeUtils.escapeJava(java);
    }

    public static String unescapeJava(String java) {
        return StringEscapeUtils.unescapeJava(java);
    }

    /**
     * Calculate the similarity of two strings.
     * Preset a large number of mainstream algorithms, go to {@link SimilarityAlgorithmEnum } and choose the appropriate algorithm
     * @param left string A
     * @param right string B
     * @param algorithm similarity algorithm
     * @return return The similarity between string A and string B
     */
    public static <T> T similarity(@NotNull String left, @NotNull String right, @NotNull SimilarityAlgorithmEnum<T> algorithm) {
        return algorithm.create().apply(left, right);
    }

    /**
     * TODO: To be implemented, refer to {@link StringsComparator}
     * This feature of apache looks strange and the completion is not high. So plan to reimplement
     */
    @SneakyThrows
    public static void diff(@NotNull String left, @NotNull String right) {
        throw new NotImplementedException("");
    }

}
