package io.github.javajerrat.boost.lang.reflect.beanaccessor;

import io.github.javajerrat.boost.lang.reflect.exception.PropertyNotFoundException;
import java.util.Set;
import javax.annotation.concurrent.ThreadSafe;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/21
 */
@ThreadSafe
public interface BeanAccessor<T> {

    void set(T object, String property, Object value) throws PropertyNotFoundException;

    Object get(T object, String property) throws PropertyNotFoundException;

    Class<?> type(String property) throws PropertyNotFoundException;

    Set<String> properties();
}
