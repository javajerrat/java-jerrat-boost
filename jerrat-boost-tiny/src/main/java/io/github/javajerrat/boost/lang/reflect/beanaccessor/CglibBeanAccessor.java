package io.github.javajerrat.boost.lang.reflect.beanaccessor;

import com.esotericsoftware.reflectasm.MethodAccess;
import io.github.javajerrat.boost.lang.reflect.exception.PropertyNotFoundException;
import io.github.javajerrat.boost.lang.reflect.impl.Utils;
import io.github.javajerrat.boost.lang.reflect.impl.Utils.PropertyIndexTuple;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.concurrent.ThreadSafe;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/22
 */
@ThreadSafe
public class CglibBeanAccessor<T> implements BeanAccessor<T> {

    private final MethodAccess access;
    private final Map<String, PropertyIndexTuple> properties;

    public CglibBeanAccessor(Class<?> clazz) {
        this.access = MethodAccess.get(clazz);
        this.properties = Collections.unmodifiableMap(Utils.scanAllProperties(access));
    }

    @Override
    public void set(T object, String property, Object value) throws PropertyNotFoundException {
        access.invoke(object, locate(property).setterIndex, value);
    }

    @Override
    public Object get(T object, String property) throws PropertyNotFoundException {
        return access.invoke(object, locate(property).getterIndex);
    }

    @Override
    public Class<?> type(String property) throws PropertyNotFoundException {
        return access.getReturnTypes()[locate(property).getterIndex];
    }

    private PropertyIndexTuple locate(String property) throws PropertyNotFoundException {
        PropertyIndexTuple propertyIndex = properties.get(property);
        if (propertyIndex == null) {
            throw new PropertyNotFoundException(property);
        }
        return propertyIndex;
    }

    @Override
    public Set<String> properties() {
        return properties.keySet();
    }
}
