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

package io.github.javajerrat.boost.lang.debug;

import lombok.SneakyThrows;
import org.jooq.lambda.fi.util.function.CheckedSupplier;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/6/24
 */
public class Slf4jLogs {

    /**
     * Similar to {@link Logger#error(String)}, {@link Logger#warn(String)} and so on.
     * According to the log level, it dispatches to a matching function.
     * @param log logger
     * @param level log level
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    public static void log(Logger log, Level level, String format, Object... arguments) {
        switch (level) {
            case ERROR:
                log.error(format, arguments);
                break;
            case WARN:
                log.warn(format, arguments);
                break;
            case INFO:
                log.info(format, arguments);
                break;
            case DEBUG:
                log.debug(format, arguments);
                break;
            case TRACE:
                log.trace(format, arguments);
                break;
            default:
                throw new IllegalStateException("Illegal state");
        }
    }

    /**
     * Similar to {@link Logger#isErrorEnabled()}, {@link Logger#isWarnEnabled()} and so on.
     * According to the log level, it dispatches to a matching function.
     * @param log logger
     * @param level log level
     */
    public static boolean isEnable(Logger log, Level level) {
        switch (level) {
            case ERROR:
                return log.isErrorEnabled();
            case WARN:
                return log.isWarnEnabled();
            case INFO:
                return log.isInfoEnabled();
            case DEBUG:
                return log.isDebugEnabled();
            case TRACE:
                return log.isTraceEnabled();
            default:
                throw new IllegalStateException("Illegal state");
        }
    }

    /**
     * Format string in slf4j style.
     *  @param message String format
     * @param args args
     * @return Formatted string
     */
    public static String format(String message, Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }


    /**
     * A helper function to implement delayed log to improve performance.
     * @param checkedSupplier Log logic.
     * @return An object that triggers log logic when it's toString method is called.
     */
    public static LazyString lazy(CheckedSupplier<?> checkedSupplier) {
        return new LazyString(checkedSupplier);
    }

    public static class LazyString {
        private final CheckedSupplier<?> stringSupplier;


        LazyString(final CheckedSupplier<?> stringSupplier) {
            this.stringSupplier = stringSupplier;
        }

        @SneakyThrows
        @Override
        public String toString() {
            return String.valueOf(stringSupplier.get());
        }
    }
}
