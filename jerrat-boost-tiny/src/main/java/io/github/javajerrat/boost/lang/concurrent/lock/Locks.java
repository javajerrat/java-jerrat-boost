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


package io.github.javajerrat.boost.lang.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import lombok.Lombok;
import org.jooq.lambda.fi.util.function.CheckedSupplier;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/3/7
 */
public class Locks {

    public Locks() {
        throw new UnsupportedOperationException();
    }

    public static void withSynchronized(Lock lock, int time, TimeUnit timeUnit, Runnable continuation) throws InterruptedException, TimeoutException {
        if (!lock.tryLock(time, timeUnit)) {
            throw new TimeoutException();
        }
        try {
            continuation.run();
        } finally {
            lock.unlock();
        }
    }

    public static <T> T withSynchronized(
        Lock lock, int time, TimeUnit timeUnit, CheckedSupplier<T> continuation) throws InterruptedException, TimeoutException {
        if (!lock.tryLock(time, timeUnit)) {
            throw new TimeoutException();
        }
        try {
            try {
                return continuation.get();
            } catch (Throwable throwable) {
                throw Lombok.sneakyThrow(throwable);
            }
        } finally {
            lock.unlock();
        }
    }

    public static AutoCloseableLock autoCloseableLock(Lock lock) {
        return new AutoCloseableLock(lock);
    }
}
