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

import org.jooq.lambda.fi.util.function.CheckedSupplier;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/6/24
 */
public class Slf4jLogs {

    public static void log(Logger log, Level level, String format, Object... args) {
        switch (level) {
            case ERROR:
                log.error(format, args);
                break;
            case WARN:
                log.warn(format, args);
                break;
            case INFO:
                log.info(format, args);
                break;
            case DEBUG:
                log.debug(format, args);
                break;
            case TRACE:
                log.trace(format, args);
                break;
            default:
                throw new IllegalStateException("Illegal state");
        }
    }

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

    public static LazyString lazy(CheckedSupplier<?> checkedSupplier) {
        return new LazyString(checkedSupplier);
    }

}
