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



package io.github.javajerrat.boost.lang.io;

import com.google.common.collect.Iterators;
import io.github.javajerrat.boost.lang.functions.IntFunction2;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.NullWriter;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/26
 */
public class IOs extends IOUtils {


    /**
     * Traversing line by line from the reader, calling the processor callback function for each row in the traversal.
     * @param reader input character stream
     * @param processor handles the callback function for each line.
     * The first parameter is the content of the line, excluding the line terminator.
     * The second parameter is the line number, starting at 0.
     * If the function returns false, the traversal is terminated.
     */
    public static void foreachLines(@NonNull Reader reader, @NonNull IntFunction2<String, Boolean> processor) throws IOException {
        final BufferedReader bufferedReader = toBufferedReader(reader);
        String line = bufferedReader.readLine();
        int i = 0;
        while (line != null) {
            if (!processor.apply(line, i)) {
                return;
            }
            line = bufferedReader.readLine();
            i++;
        }
    }

    public static void writeLines(Writer writer, Iterable<?> lines) throws IOException {
        writeLines(writer, lines, null);
    }

    public static void writeLines(Writer writer, Iterable<?> lines, String lineEnding) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = IOUtils.LINE_SEPARATOR;
        }
        for (final Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    public static InputStream concat(InputStream... inputStreams) {
        return concatInputStream(Arrays.asList(inputStreams));
    }

    public static InputStream concatInputStream(Iterable<? extends InputStream> inputStreams) {
        return new SequenceInputStream(Iterators.asEnumeration(inputStreams.iterator()));
    }

    public static Writer nullWriter() {
        return NullWriter.NULL_WRITER;
    }

    public static OutputStream nullOutputStream() {
        return NullOutputStream.NULL_OUTPUT_STREAM;
    }

    public static Reader toReader(CharSequence cs) {
        return new StringReader(cs.toString());
    }

    public static Reader toReader(char[] cs) {
        return new CharArrayReader(cs);
    }

    public static Reader toReader(byte[] cs, Charset charset) {
        return new InputStreamReader(new ByteArrayInputStream(cs), charset);
    }

    public static InputStream toInputStream(byte[] cs) {
        return new ByteArrayInputStream(cs);
    }

}
