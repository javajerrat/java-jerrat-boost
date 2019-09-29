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


package io.github.javajerrat.boost.lang.collection.map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2018/12/3
 *
 * Thanks for https://stackoverflow.com/questions/23731270/fluent-api-with-inheritance-and-generics
 */
public abstract class BaseFluentMap<K, V, THIS extends BaseFluentMap<K, V, THIS>> extends LinkedHashMap<K, V> {

    public THIS fluentPut(K key, V value) {
        this.put(adviceKeyOnPut(key), value);
        return self();
    }

    public THIS fluentPutAll(Map<? extends K, ? extends V> m) {
        // this.putAll(m);
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
        return self();
    }

    public THIS fluentClear() {
        this.clear();
        return self();
    }

    public THIS fluentRemove(Object key) {
        this.remove(key);
        return self();
    }

    public THIS fPut(K key, V value) {
        return this.fluentPut(key, value);
    }

    public THIS fPutAll(Map<? extends K, ? extends V> m) {
        return this.fluentPutAll(m);
    }

    public THIS fClear() {
        return fluentClear();
    }

    public THIS fRemove(Object key) {
        return this.fluentRemove(key);
    }

    abstract THIS self();

    protected K adviceKeyOnPut(K key) {
        return key;
    }
}
