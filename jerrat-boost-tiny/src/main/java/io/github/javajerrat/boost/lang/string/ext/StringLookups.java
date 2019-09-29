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


package io.github.javajerrat.boost.lang.string.ext;

import io.github.javajerrat.boost.lang.string.exception.StringFormatException;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/16
 */
public class StringLookups {

    @AllArgsConstructor
    @ToString
    static final class ListStringLookup<V> implements StringLookup {

        private final @NonNull List<V> list;

        @Override
        public String lookup(final String key) {
            String result;
            try {
                int i = Integer.parseInt(key);
                result = !(i >= 0 && i < list.size()) || list.get(i) == null ? null : list.get(i).toString();
            } catch (NumberFormatException nfe) {
                result = null;
            }
            return result;
        }

    }

    @AllArgsConstructor
    @ToString
    static final class ThrowExStringLookup implements StringLookup {
        private StringLookup stringLookup;
        private final boolean throwEx;

        @Override
        public String lookup(String key) {
            String result = stringLookup.lookup(key);
            if (throwEx) {
                throw new StringFormatException("Formatting string is wrong, the key does not exist: " + key);
            }
            return result;
        }
    }

    public static <T> StringLookup fromMap(Map<String, T> map) {
        return StringLookupFactory.INSTANCE.mapStringLookup(map);
    }

    public static <T> StringLookup fromList(List<T> list) {
        return new ListStringLookup<>(list);
    }

    public static ThrowExStringLookup throwExStringLookup(StringLookup stringLookup, boolean throwEx) {
        return new ThrowExStringLookup(stringLookup, throwEx);
    }

    public static <T> StringLookup emptyStringLookup() {
        return StringLookupFactory.INSTANCE.nullStringLookup();
    }
}
