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

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.base.CaseFormat;
import io.github.javajerrat.boost.lang.string.enums.SimilarityAlgorithmEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/17
 */
class TextsTest {

    @Test
    void convertNamedStyle() {
        String name = Texts.convertNamedStyle("get_name", CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_CAMEL);
        Assertions.assertEquals("getName", name);
        System.out.println(name);
    }

    @Test
    void similarity() {
        int similar = Texts.similarity("abcdef", "acdf", SimilarityAlgorithmEnum.LEVENSHTEIN_DISTANCE);
        System.out.println(similar);
    }

    @Test
    void capitalize() {
        {
            String words = Texts.capitalize("hello world");
            Assertions.assertEquals("Hello World", words);
        }

        {
            String words = Texts.uncapitalize("Hello World");
            Assertions.assertEquals("hello world", words);
        }
    }

    @Test
    void escapeJava() {
    }

    @Test
    void unescapeJava() {
    }


    @Test
    void diff() {
    }
}