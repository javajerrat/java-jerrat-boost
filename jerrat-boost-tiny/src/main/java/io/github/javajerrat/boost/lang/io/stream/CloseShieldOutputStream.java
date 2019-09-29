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


package io.github.javajerrat.boost.lang.io.stream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import org.apache.commons.io.output.ClosedOutputStream;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/26
 */
public class CloseShieldOutputStream extends FilterOutputStream {

    /**
     * Creates an output stream filter built on top of the specified underlying output stream.
     *
     * @param out the underlying output stream to be assigned to the field <tt>this.out</tt> for later use, or
     * <code>null</code> if this instance is to be
     * created without an underlying stream.
     */
    public CloseShieldOutputStream(@NotNull OutputStream out) {
        super(out);
    }

    @Override
    public void close() throws IOException {
        if (out instanceof DeflaterOutputStream) {
            ((DeflaterOutputStream) out).finish();
        } else {
            super.flush();
        }
        out = new ClosedOutputStream();
    }
}
