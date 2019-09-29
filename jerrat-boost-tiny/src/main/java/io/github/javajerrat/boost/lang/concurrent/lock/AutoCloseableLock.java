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
import java.util.concurrent.locks.Lock;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/9
 *
 * Thanks for: https://stackoverflow.com/questions/6965731/are-locks-autocloseable
 */

public class AutoCloseableLock extends ForwardingLock {


    private Lock lock;
    private UncheckedAutoCloseable autoCloseable;

    AutoCloseableLock(Lock lock) {
        this.lock = lock;
        this.autoCloseable = this.lock::unlock;
    }

    @Override
    Lock delegate() {
        return lock;
    }

    public UncheckedAutoCloseable withLockInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
        return autoCloseable;
    }

    public UncheckedAutoCloseable withLock() {
        this.lock.lock();
        return autoCloseable;
    }

    public UncheckedAutoCloseable withTryLock(long time, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (!this.lock.tryLock(time, unit)) {
            throw new TimeoutException();
        }
        return autoCloseable;
    }
}
