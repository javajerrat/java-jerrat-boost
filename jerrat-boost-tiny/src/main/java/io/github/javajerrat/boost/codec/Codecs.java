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

import com.google.common.io.BaseEncoding;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/25
 */
public class Codecs {

    /**
     * Convert byte[] to a string
     * @param srcBytes Source bytes
     * @return String
     */
    public static String byte2Hex(byte[] srcBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : srcBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            sb.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return sb.toString();
    }

    /**
     * Decode a hex string to a string
     * @param source hex string
     * @return Decoded String
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            try {
                sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return sourceBytes;
    }

    public static byte[] base64Decode(CharSequence cs) {
        return BaseEncoding.base64().decode(cs);
    }

    public static String base64Encode(byte[] bytes) {
        return BaseEncoding.base64().encode(bytes);
    }

    public static OutputStream base64Encode(Writer writer) {
        return BaseEncoding.base64().encodingStream(writer);
    }

}
