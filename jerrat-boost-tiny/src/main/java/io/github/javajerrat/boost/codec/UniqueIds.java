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


package io.github.javajerrat.boost.codec;

import com.google.common.annotations.Beta;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.UUID;
import lombok.SneakyThrows;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2018/12/3
 */
public class UniqueIds {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return * Returns the 32-bit UUID.
     * Compared to the standard 36-bit UUID, the 32-bit UUID is removed - and consists of only 15 characters of 0-9, a-f.
     */
    public static String uuidAs32() {
        return uuid().replace("-", "");
    }

    /**
     * @return * Returns the 26-bit UUID.
     * Compared to the standard 36-bit UUID, 26 bits consist of only 31 characters of 0-9, a-v.
     */
    @Beta
    public static String uuidAs26() {
        UUID uuid = UUID.randomUUID();
        long high = uuid.getMostSignificantBits();
        long low = uuid.getLeastSignificantBits();

        return Long.toUnsignedString(high, 32) + Long.toUnsignedString(low, 32);
    }

    /**
     * @return *Returns the 22-bit UUID.
     * Compared to the standard 36-bit UUID, 22 bits are consistent with the characters used by Base64Url encoding.
     * It consists of all numbers, lowercase letters, uppercase letters, and _ and - symbols for a total of 64 symbols.
     */
    @SneakyThrows
    @Beta
    public static String uuidAs22() {
        UUID uuid = UUID.randomUUID();
        long high = uuid.getMostSignificantBits();
        long low = uuid.getLeastSignificantBits();

        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(bas);
        os.writeLong(high);
        os.writeLong(low);
        os.flush();
        byte[] bytes = bas.toByteArray();
        return BaseEncoding.base64Url().encode(bytes).replace("=", "");
    }
}
