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


package io.github.javajerrat.boost.basetools.file;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2018/9/18
 *
 * @see FilenameUtils
 */
public class Paths {

    public static final char SEP = System.getProperty("file.separator").charAt(0);

    private static final List<Character> ALL_SEP = Arrays.asList('/', '\\', SEP);


    public static String join(String... seg) {
        if (seg == null || seg.length == 0) {
            return "";
        }

        StringBuilder path = new StringBuilder();
        for (int i = 0; i < seg.length; i++) {
            String s = seg[i];
            if (s.length() > 0) {
                int first = 0;
                int end = s.length() - 1;
                if (ALL_SEP.contains(s.charAt(first))) {
                    first++;
                }
                if (ALL_SEP.contains(s.charAt(end))) {
                    end--;
                }
                path.append(s, first, end + 1);
                if (i != seg.length - 1) {
                    path.append(SEP);
                }
            }
        }
        return path.toString();
    }

    public static String dirname(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }

        if (ALL_SEP.contains(path.charAt(path.length() - 1))) {
            path = path.substring(0, path.length() - 1);
        }
        return FilenameUtils.getFullPath(path);
    }

    public static String basename(String path) {
        return FilenameUtils.getName(path);
    }
}
