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

import io.github.javajerrat.boost.lang.datatype.interfaces.UncheckedAutoCloseable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/11
 */
class AutoCloseableLockTest {

    @Test
    void withLockInterruptibly() throws InterruptedException {
        AutoCloseableLock lock = new AutoCloseableLock(new ReentrantLock());

        try (UncheckedAutoCloseable ignore = lock.withLockInterruptibly()) {
            lock.unlock();
        }
    }

    @Test
    void withLock() {
        AutoCloseableLock lock = new AutoCloseableLock(new ReentrantLock());

        try (UncheckedAutoCloseable ignore = lock.withLock()) {
        }
    }

    @Test
    void withTryLock() throws TimeoutException, InterruptedException {
        AutoCloseableLock lock = new AutoCloseableLock(new ReentrantLock());

        try (UncheckedAutoCloseable ignore = lock.withTryLock(1, TimeUnit.SECONDS)) {
        }
    }
}