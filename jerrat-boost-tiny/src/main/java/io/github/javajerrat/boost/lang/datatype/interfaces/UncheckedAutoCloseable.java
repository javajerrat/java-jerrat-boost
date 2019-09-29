package io.github.javajerrat.boost.lang.datatype.interfaces;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/11
 */
public interface UncheckedAutoCloseable extends AutoCloseable {

    @Override
    void close() throws RuntimeException;
}

