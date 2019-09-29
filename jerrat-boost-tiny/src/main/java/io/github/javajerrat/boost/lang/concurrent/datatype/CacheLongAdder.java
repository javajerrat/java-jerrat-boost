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


package io.github.javajerrat.boost.lang.concurrent.datatype;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/4/2
 */

public class CacheLongAdder extends LongAdder {

    /**
     * Last written time
     */
    private volatile long lastModify;
    /**
     * Last read time
     */
    private volatile long lastRead;

    /**
     * Cached long value
     */
    private Long cacheSum = null;

    public CacheLongAdder() {
        this.lastModify = System.currentTimeMillis();
        this.lastRead = -1L;
    }

    @Override
    public void add(long x) {
        super.add(x);
        lastModify = System.currentTimeMillis();
    }

    /**
     * Get the accumulated value, because the cache, the performance is higher,
     * but there is a deviation between the value and the actual value
     */
    @Override
    public long sum() {
        long lastRead = this.lastRead;
        long lastModify = this.lastModify;
        if (cacheSum == null || lastRead < lastModify) {
            cacheSum = super.sum();
            this.lastRead = System.currentTimeMillis();
        }
        return cacheSum;
    }
}
