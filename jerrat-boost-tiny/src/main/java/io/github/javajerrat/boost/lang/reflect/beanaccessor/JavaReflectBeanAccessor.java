package io.github.javajerrat.boost.lang.reflect.beanaccessor;

import io.github.javajerrat.boost.lang.reflect.exception.PropertyNotFoundException;
import io.github.javajerrat.boost.lang.reflect.impl.Utils;
import io.github.javajerrat.boost.lang.reflect.impl.Utils.Property;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.concurrent.ThreadSafe;
import lombok.SneakyThrows;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/22
 */
@ThreadSafe
public class JavaReflectBeanAccessor<T> implements BeanAccessor<T> {


    private final Map<String, Property> properties;

    public JavaReflectBeanAccessor(Class<T> clazz) {
        this.properties = Collections.unmodifiableMap(Utils.scanAllProperties(clazz));
    }

    @Override
    @SneakyThrows
    public void set(T object, String property, Object value) throws PropertyNotFoundException {
        locate(property).setter.invoke(object, value);
    }

    @Override
    @SneakyThrows
    public Object get(T object, String property) throws PropertyNotFoundException {
        return locate(property).getter.invoke(object);
    }

    @Override
    public Class<?> type(String property) throws PropertyNotFoundException {
        return locate(property).getter.getReturnType();
    }

    private Property locate(String propertyName) throws PropertyNotFoundException {
        Property property = properties.get(propertyName);
        if (property == null) {
            throw new PropertyNotFoundException(propertyName);
        }
        return property;
    }

    @Override
    public Set<String> properties() {
        return this.properties.keySet();
    }

}
