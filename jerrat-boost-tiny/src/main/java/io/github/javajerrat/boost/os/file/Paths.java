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


package io.github.javajerrat.boost.os.file;

import com.google.common.annotations.Beta;
import io.github.javajerrat.boost.lang.string.Strings;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2018/9/18
 *
 * @see FilenameUtils
 */
public class Paths {

    public static final char SEPARATOR = File.separatorChar;

    private static final List<Character> ALL_SEPARATOR = Arrays.asList('/', '\\', SEPARATOR);

    /**
     * If path starts with ~, replace it with home directory.
     * TODO: This function currently cannot handle syntax like {@code ~user}
     *
     *  @param path Replaced path
     * @return A string representing the processed path
     */
    @Beta
    @NotNull
    public static String expanduser(@NotNull String path) {
        String user = System.getProperty("user.home");
        return path.replaceFirst("~", user);
    }

    private static Pattern varPattern = Pattern.compile("\\$(\\{?)([a-zA-Z0-9_]+)(}?)");

    /**
     * This function replaces environment variables that appear in the path.
     * If the relevant environment variable is not found, it will not be replaced.
     * The syntax of the environment variable is {@code $DEMOVAR } or {@code ${DEMOVAR}}.
     *
     * @param path Replace path
     * @return  A string representing the processed path
     */
    @Beta
    @NotNull
    public static String expandvars(@NotNull String path) {
        return Strings.regReplace(path, varPattern, (matches, index) -> {
            String all = matches.group(0);
            String leftBrace = matches.group(1);
            String var = matches.group(2);
            String rightBrace = matches.group(3);
            if (leftBrace.isEmpty() == rightBrace.isEmpty()) {
                String value = System.getenv(var);
                return value != null ? value : all;
            } else if (leftBrace.isEmpty()) {
                String value = System.getenv(var);
                return value != null ? value + rightBrace : all;
            } else {
                return all;
            }
        });
    }


    /**
     *
     * Join two or more pathname components, inserting '/' as needed.
     * @param components pathname components
     * @return path
     */
    public static String join(String... components) {
        if (components == null || components.length == 0) {
            return "";
        }

        StringBuilder path = new StringBuilder();
        for (int i = 0; i < components.length; i++) {
            String s = components[i];
            if (s.length() > 0) {
                int first = 0;
                int end = s.length() - 1;
                if (ALL_SEPARATOR.contains(s.charAt(first))) {
                    first++;
                }
                if (ALL_SEPARATOR.contains(s.charAt(end))) {
                    end--;
                }
                path.append(s, first, end + 1);
                if (i != components.length - 1) {
                    path.append(SEPARATOR);
                }
            }
        }
        return path.toString();
    }

    /**
     * @param path pathname
     * @return Returns the directory component of a pathname
     */
    public static String dirname(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }

        if (ALL_SEPARATOR.contains(path.charAt(path.length() - 1))) {
            path = path.substring(0, path.length() - 1);
        }
        return FilenameUtils.getFullPath(path);
    }

    /**
     * @param path the file path
     * @return the name of the file without the path, or an empty string if none exists.
     * Null bytes inside string will be removed
     */
    public static String basename(String path) {
        return FilenameUtils.getName(path);
    }
}
