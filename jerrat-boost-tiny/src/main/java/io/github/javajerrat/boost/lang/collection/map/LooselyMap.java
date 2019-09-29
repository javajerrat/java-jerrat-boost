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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/3/7
 */
@AllArgsConstructor
public class LooselyMap extends BaseFluentMap<String, Object, LooselyMap> {

    public static LooselyMap of() {
        return new LooselyMap();
    }

    public static LooselyMap of(Map<String, ?> map) {
        LooselyMap looselyMap = new LooselyMap();
        looselyMap.putAll(map);
        return looselyMap;
    }

    public String getAsString(String key) {
        return (String)get(key);
    }

    public Boolean getAsBool(String key) {
        return (Boolean) get(key);
    }

    public Character getAsChar(String key) {
        return (Character) get(key);
    }

    public Byte getAsByte(String key) {
        return (Byte) get(key);
    }

    public Short getAsShort(String key) {
        return (Short) get(key);
    }

    public Integer getAsInt(String key) {
        return (Integer) get(key);
    }

    public Long getAsLong(String key) {
        return (Long) get(key);
    }

    public Double getAsDouble(String key) {
        return (Double) get(key);
    }

    public Float getAsFloat(String key) {
        return (Float) get(key);
    }

    public BigDecimal getAsDecimal(String key) {
        return (BigDecimal) get(key);
    }

    public Date getAsDate(String key) {
        return (Date) get(key);
    }

    @Override
    LooselyMap self() {
        return this;
    }

}
