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


package io.github.javajerrat.boost.basetools.service;

import com.google.common.annotations.Beta;
import com.google.common.base.Suppliers;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.NotImplementedException;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/27
 */
public class ServiceInvokes {

    /**
     * Create a function that can only call supplier once. Repeated calls return the result of the first call.
     * When called for the first time, the supplier function is called to get the value. The cached value is returned when called later.
     *
     * This function is internally implemented using DCL and can be used in multi-threaded scenarios.
     * This function can be regarded as a package for DCL, which is more convenient for implementing DCL.
     *
     * @param supplier original function
     * @return return function
     */
    public static <T> Supplier<T> once(@NotNull Supplier<T> supplier) {
        return Suppliers.memoize(supplier::get);
    }

    /**
     * * Similar to {@link ServiceInvokes#once(Supplier) } , except that the cached value expires after the specified expiration time
     */
    public static <T> Supplier<T> cached(@NotNull Supplier<T> supplier, long duration, @NotNull TimeUnit unit) {
        return Suppliers.memoizeWithExpiration(supplier::get, duration, unit);
    }

    /**
     *
     * Create a throttling function that executes the runnable function at most during the duration time
     *
     * TODO:
     */
    @Beta
    public static <T> Supplier<T> throttle(Supplier<T> supplier, long duration, @NotNull TimeUnit unit) {
        throw new NotImplementedException("");
    }
}
