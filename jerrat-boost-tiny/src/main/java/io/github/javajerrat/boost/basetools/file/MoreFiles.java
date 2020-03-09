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

package io.github.javajerrat.boost.basetools.file;

import com.google.common.annotations.Beta;
import com.google.common.collect.AbstractIterator;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/11/18
 *
 * @see java.nio.file.Files
 * @see org.apache.commons.io.FileUtils
 * @see com.google.common.io.Files
 */
public class MoreFiles {

    /**
     * Finds files within a given directory (and optionally its
     * subdirectories). All files found are filtered by an IOFileFilter.
     * <p>
     * The resulting collection includes the starting directory and
     * any subdirectories that match the directory filter.
     * <p>
     *
     * @param directory  the directory to search in
     * @param fileFilter filter to apply when finding files.
     * @param dirFilter  optional filter to apply when finding subdirectories.
     *                   If this parameter is {@code null}, subdirectories will not be included in the
     *                   search. Use TrueFileFilter.INSTANCE to match all directories.
     * @param includeSubDirectories indicates if will include the subdirectories themselves
     * @return a collection of java.io.File with the matching files
     */
    @NotNull
    public static Collection<File> listFiles(@NotNull File directory, @NotNull FileFilter fileFilter, @NotNull FileFilter dirFilter, boolean includeSubDirectories) {
        IOFileFilter fileIOFileFilter = asFileFilter(fileFilter);
        IOFileFilter dirIOFileFilter = asFileFilter(dirFilter);
        if (includeSubDirectories) {
            return FileUtils.listFilesAndDirs(directory, fileIOFileFilter, dirIOFileFilter);
        } else {
            return FileUtils.listFiles(directory, fileIOFileFilter, dirIOFileFilter);
        }
    }

    /**
     * Similar to a {@link MoreFiles#listFiles(File, IOFileFilter, IOFileFilter, boolean) },
     * except that the function use lazy loading without loading all data into memory at once.
     * So if you predict that the amount of data is too large, you can use this function.
     *
     * If you only want specific file suffixes, use {@link org.apache.commons.io.filefilter.SuffixFileFilter}
     *
     * @param directory  the directory to search in
     * @param fileFilter filter to apply when finding files.
     * @param dirFilter  optional filter to apply when finding subdirectories.
     *                   If this parameter is {@code null}, subdirectories will not be included in the
     *                   search. Use TrueFileFilter.INSTANCE to match all directories.
     * @param includeSubDirectories indicates if will include the subdirectories themselves
     *
     * @return Returns an iterable object that triggers the execution of real logic when iterating.
     */
    @Beta
    @NotNull
    public static Iterable<File> files(File directory, @NotNull FileFilter fileFilter, @NotNull FileFilter dirFilter, boolean includeSubDirectories) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter 'directory' is not a directory: " + directory);
        }
        if (fileFilter == null) {
            throw new NullPointerException("Parameter 'fileFilter' is null");
        }

        IOFileFilter fileIOFileFilter = asFileFilter(fileFilter);
        IOFileFilter dirIOFileFilter = asFileFilter(dirFilter);

        final IOFileFilter effFileFilter = FileFilterUtils.and(fileIOFileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE));
        final IOFileFilter effDirFilter = dirIOFileFilter == null ?
            FalseFileFilter.INSTANCE :
            FileFilterUtils.and(dirIOFileFilter, DirectoryFileFilter.INSTANCE);
        return innerFiles(directory, FileFilterUtils.or(effFileFilter, effDirFilter), includeSubDirectories);
    }

    /**
     * The function behaves the same as {@link FileUtils#innerListFiles(Collection, File, IOFileFilter, boolean)}, except that the function implements lazy loading.
     * This function is implemented using a stack, referring to the recursive implementation of the above function.
     *
     * @return Returns an iterable object that triggers the execution of real logic when iterating.
     */
    @Beta
    private static Iterable<File> innerFiles(File directory, IOFileFilter filter, boolean includeSubDirectories) {
        final int STEP_START = 0;
        final int STEP_DIRECTORY_RE = 1;
        final int STEP_END = 2;

        @AllArgsConstructor
        class Item {
            File[] file;
            int index;
            int step;
        }

        return () -> new AbstractIterator<File>() {
            Deque<Item> stack = new ArrayDeque<>();
            {
                stack.push(new Item(new File[] {directory}, 0, STEP_START));
            }

            @Override
            protected File computeNext() {
                Item item = stack.getFirst();
                switch (item.step) {
                    case STEP_START: {
                        File file = item.file[item.index];
                        if (file.isDirectory()) {
                            item.step = STEP_DIRECTORY_RE;
                            if (includeSubDirectories) {
                                return file;
                            } else {
                                return computeNext();
                            }
                        } else {
                            item.step = STEP_END;
                            return file;
                        }
                    }

                    case STEP_DIRECTORY_RE: {
                        File file = item.file[item.index];
                        final File[] found = file.listFiles((FileFilter) filter);
                        if (found != null) {
                            stack.push(new Item(found, 0, STEP_START));
                        } else {
                            item.step = STEP_END;
                        }
                        return computeNext();
                    }

                    case STEP_END: {
                        if (item.index + 1 < item.file.length) {
                            item.index++;
                        } else {
                            stack.pop();
                            if (stack.isEmpty()) {
                                return endOfData();
                            }
                        }
                        return computeNext();
                    }

                    default: {
                        throw new IllegalStateException();
                    }
                }
            }
        };
    }

    @Nullable
    @Contract("null -> null")
    private static IOFileFilter asFileFilter(@Nullable FileFilter fileFilter) {
        if (fileFilter == null) {
            return null;
        }
        if (fileFilter instanceof IOFileFilter) {
            return (IOFileFilter) fileFilter;
        } else {
            return FileFilterUtils.asFileFilter(fileFilter);
        }
    }

}
