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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static io.github.javajerrat.boost.lang.collection.MoreCollections.*;

import io.github.javajerrat.boost.lang.string.exception.StringFormatException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/16
 */
class StringsTest {

    @Test
    void format() {

        {
            String s = Strings.format("", mapOf("name", "tom"));
            assertEquals("", s);
        }

        {
            String s = Strings.format("My {filed} is {name}", mapOf("filed", "name", "name", "tom"));
            assertEquals("My name is tom", s);
        }

        {
            String s = Strings.format("My {filed} is {name}", mapOf("name", "tom"));
            assertEquals("My {filed} is tom", s);
        }

        {
            String s = Strings.format("My {0} is {1}", "name", "tom");
            assertEquals("My name is tom", s);
        }

        {
            String s = Strings.format("My {0} is {1}, {2}", "name", "tom");
            assertEquals("My name is tom, {2}", s);
        }

        {
            String s = Strings.format("My {0} is \\{1}", "name", "tom");
            assertEquals("My name is {1}", s);
        }


        assertThrows(StringFormatException.class, () -> {
            Strings.format("My name is {1}", Strings.FormatConfig.of().throwExIfKeyNotFound(true));
        });

        // Test recursive replacement value
        {
            String s = Strings.format("My name is {name}", mapOf("name", "{name1}", "name1", "tom"));
            assertEquals("My name is {name1}", s);
        }

        {
            String s = Strings.format("My name is {name}", Strings.FormatConfig.of()
                .map(mapOf("name", "{name1}", "name1", "tom"))
                .recursiveInValues(true));
            assertEquals("My name is tom", s);
        }

        // Recursive substitution variable

        {
            String s = Strings.format("My name is {my{n}}", Strings.FormatConfig.of()
                .map(mapOf("name", "name1", "myname", "tom")));
            assertEquals("My name is {my{n}}", s);
        }

        {
            String s = Strings.format("My name is {my{n}}", Strings.FormatConfig.of()
                .map(mapOf("n", "name", "myname", "tom"))
                .recursiveInVariables(true));
            assertEquals("My name is tom", s);
        }
    }

    @Test
    void split() {
        {
            List<String> list = Strings.split("a,b,c", ",").toList();
            assertEquals(listOf("a", "b", "c"), list);
        }

        {
            List<String> list = Strings.split("a ,b , ,,c", ",").toList();
            assertEquals(listOf("a ", "b ", " ", "c"), list);
        }

        {
            List<String> list = Strings.split(" ,,,", ",").toList();
            assertEquals(listOf(" "), list);
        }

        {
            List<String> list = Strings.split("","").toList();
            assertEquals(listOf(), list);
        }


        {
            List<String> list = Strings.split("a , b ,, c",",", Strings.SplitConfig.of().trimResults(true)).toList();
            assertEquals(listOf("a", "b", "c"), list);
        }

        {
            for (String s : Strings.split("a , b ,, c",",")) {
                System.out.println(s);
            }
        }
    }

    @Test
    void substring() {
        assertEquals("b", Strings.slice("abc", 1, 2));
        // Exceed at the end
        assertEquals("bc", Strings.slice("abc", 1, 5));
        assertEquals("bc", Strings.slice("abc", 1, 5));
        assertEquals("bc", Strings.slice("abcde", 1, -2));
        // Exceed at the begin
        assertEquals("", Strings.slice("abcde", 1, -8));
        assertEquals("", Strings.slice("abcde", -1, 0));

        assertEquals("d", Strings.slice("abcde", -2, -1));
    }

    @Test
    void replace() {
        assertEquals("ab123defg", Strings.replace("abcdefg", 2, 3, "123"));
        assertEquals("ab123", Strings.replace("abcdefg", 2, 10, "123"));
        assertEquals("ab123cdefg", Strings.replace("abcdefg", 2, 2, "123"));

        assertEquals("aBc123Abc123", Strings.replace("Abc123Abc123", "abc", "aBc", Strings.ReplaceConfig.of()
            .max(1)
            .ignoreCase(true)
        ));
    }

    @Test
    void encode() {
        String s = "我是一个人";
        assertEquals(s, Strings.encode(Strings.decode(s)));
        assertEquals(s, Strings.encode(Strings.decode(s), StandardCharsets.UTF_8));
        assertEquals(s, Strings.encode(Strings.decode(s, StandardCharsets.UTF_8)));

        assertEquals(s, Strings.encode(Strings.decode(s, Charsets.GBK()), Charsets.GBK()));
    }

    @Test
    void regMatch() {
        {
            List<MatchResult> list = Strings.regMatch("abc", Pattern.compile("\\d"));
            assertEquals(0, list.size());
        }

        {
            List<MatchResult> list = Strings.regMatch("123", Pattern.compile("\\d+"));
            assertEquals(1, list.size());
            assertEquals(0, list.get(0).groupCount());
            assertEquals("123", list.get(0).group(0));
        }

        {
            List<MatchResult> list = Strings.regMatch("123", Pattern.compile("(\\d+)"));
            assertEquals(1, list.size());
            assertEquals(1, list.get(0).groupCount());
            assertEquals("123", list.get(0).group(0));
            assertEquals("123", list.get(0).group(1));
        }

        {
            List<MatchResult> list = Strings.regMatch("123 456", Pattern.compile("\\d+"));
            assertEquals(2, list.size());
            assertEquals("123", list.get(0).group(0));
            assertEquals("456", list.get(1).group(0));
        }

        {
            List<MatchResult> list = Strings.regMatch("123 456", Pattern.compile("^\\d+$"));
            assertEquals(0, list.size());
        }

    }

    @Test
    void regSerach() {
        assertEquals(-1, Strings.regSearch("jFdk", Pattern.compile("fd")));
        assertEquals(1, Strings.regSearch("jfdk", Pattern.compile("fd")));
        assertEquals(-1, Strings.regSearch("jfdk", Pattern.compile("^fd$")));
    }

    @Test
    void regSplit() {
        {
            List<String> list = Strings.regSplit("abc123def456", Pattern.compile("\\d+")).toList();
            assertEquals(listOf("abc", "def"), list);
        }

        {
            List<String> list = Strings.regSplit("abc123def456ghi", Pattern.compile("\\d+"), 2).toList();
            assertEquals(listOf("abc", "def456ghi"), list);
        }
    }

    @Test
    void regReplace() {
        // Test on a single line string
        {
            Pattern re = Pattern.compile( "(\\w+)\\s(\\w+)");
            String str = "John Smith";

            {
                String newStr = Strings.regReplace(str, re, "$2, $1");
                assertEquals("Smith, John", newStr);
            }

            assertThrows(IndexOutOfBoundsException.class, () -> {
                Strings.regReplace(str, re, "$2,$1 $3");
            });

            {
                String newStr = Strings.regReplace(str, re, ((matches, index) -> {
                    return matches.group(2) + ", " + matches.group(1);
                }));
                assertEquals("Smith, John", newStr);
            }

            {
                System.out.println(Strings.regReplace("pretty_girl.jpg", Pattern.compile("^(.*).jpg$"), "$1"));
                System.out.println(Strings.regReplace("pretty.girl.jpg", Pattern.compile("^(.*).jpg$"), "$1"));
            }
        }

        // Testing for multi-line string
        {
            Pattern re = Pattern.compile( "^(\\w+)\\s(\\w+)$", Pattern.MULTILINE);
            String str = "John Smith\nTom Alice\n";

            {
                String newStr = Strings.regReplace(str, re, "$2, $1");
                assertEquals("Smith, John\nAlice, Tom\n", newStr);
            }

            assertThrows(IndexOutOfBoundsException.class, () -> {
                Strings.regReplace(str, re, "$2,$1 $3");
            });

            {
                String newStr = Strings.regReplace(str, re, ((matcher, index) -> {
                    return matcher.group(2) + ", " + matcher.group(1);
                }));
                assertEquals("Smith, John\nAlice, Tom\n", newStr);
            }
        }
    }

    @Test
    void regex() {
        assertThrows(PatternSyntaxException.class, () -> {
            Strings.regex("/f/g");
        });

        assertEquals("/f", Strings.regex("/f").toString());

        assertEquals(-1, Strings.regSearch("jFdk", Strings.regex("/fd/")));
        assertEquals(1, Strings.regSearch("jFdk", Strings.regex("/fd/i")));

        assertEquals(-1, Strings.regSearch("jFdk\njFdk", Strings.regex("/^fd$/i")));
        assertEquals(-1, Strings.regSearch("jFdk\njFdk", Strings.regex("/^fd$/im")));

        assertEquals(-1, Strings.regSearch("jFdk\nabc", Strings.regex("/^j.*c$/im")));
        assertEquals(0, Strings.regSearch("jFdk\nabc", Strings.regex("/^j.*c$/ims")));
    }

}