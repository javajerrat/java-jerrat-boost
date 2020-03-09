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

import com.google.common.collect.AbstractIterator;
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
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.validation.constraints.NotNull;
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

    /**
     * Returns a lazy iterable. Foreach this iteration object to sequentially get each row in the stream.
     * {@link BufferedReader#lines()}
     *
     * @param bufferedReader bufferedReader
     * @return Returns a lazy iterable.
     */
    public static Iterable<String> lines(@NotNull BufferedReader bufferedReader) {
        return () -> new AbstractIterator<String>() {
            @Override
            protected String computeNext() {
                try {
                    String line = bufferedReader.readLine();
                    return line != null ? line : endOfData();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };
    }

    /**
     * Write multiple lines of text to the writer, with system line separator.
     * @param writer the writer
     * @param lines the lines
     */
    public static void writeLines(Writer writer, Iterable<String> lines) throws IOException {
        writeLines(writer, lines, IOUtils.LINE_SEPARATOR);
    }

    /**
     *  Write multiple lines of text to the writer, with custom line separator.
     * @param writer the writer
     * @param lines the lines
     * @param lineEnding line separator
     */
    public static void writeLines(Writer writer, Iterable<String> lines, @NotNull String lineEnding) throws IOException {
        if (lines == null) {
            return;
        }
        for (String line : lines) {
            if (line != null) {
                writer.write(line);
            }
            writer.write(lineEnding);
        }
    }

    /**
     * Concat two or more inputStream.
     * @param inputStreams A set of inputStreams
     * @return A decorative InputStream object
     */
    public static InputStream concat(InputStream... inputStreams) {
        return concatInputStream(Arrays.asList(inputStreams));
    }

    /**
     * Concat two or more inputStream.
     * @param inputStreams A set of inputStreams
     * @return A decorative InputStream object
     */
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
