package io.github.javajerrat.boost.lang.reflect.beancopier;

import io.github.javajerrat.boost.lang.reflect.Reflects;
import io.github.javajerrat.boost.lang.reflect.impl.Utils;
import io.github.javajerrat.boost.lang.reflect.impl.Utils.Property;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.concurrent.ThreadSafe;
import lombok.SneakyThrows;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/24
 */
@ThreadSafe
public class JavaReflectBeanCopier<E, T> extends BeanCopier<E, T> {

    private final Map<String, Property> sourceProperties;

    private final Map<String, Property> targetProperties;

    private final Set<String> commonPropertyNames;

    public JavaReflectBeanCopier(Class<E> sourceClass, Class<T> targetClass, List<String> includes, List<String> excludes) {
        super(sourceClass, targetClass);
        if (Map.class.isAssignableFrom(sourceClass)) {
            this.sourceProperties = null;
        }  else {
            this.sourceProperties = Collections.unmodifiableMap(Utils.scanAllProperties(sourceClass));
        }

        if (Map.class.isAssignableFrom(targetClass)) {
            this.targetProperties = null;
        }  else {
            this.targetProperties = Collections.unmodifiableMap(Utils.scanAllProperties(targetClass));
        }

        LinkedHashSet<String> commonSet = editableProperties(
            sourceProperties == null ? null : sourceProperties.keySet(),
            targetProperties == null ? null : targetProperties.keySet(),
            includes,
            excludes,
            (propertyName) -> {
                assert sourceProperties != null;
                assert targetProperties != null;
                Property sourceProperty = sourceProperties.get(propertyName);
                Property targetProperty = targetProperties.get(propertyName);
                return Objects.equals(
                    targetProperty.getter.getReturnType(),
                    sourceProperty.getter.getReturnType());
            });

        this.commonPropertyNames = commonSet == null ? null : Collections.unmodifiableSet(commonSet);
    }

    @Override
    @SneakyThrows
    protected void copyBean(E source, T target) {
        for (String name : this.commonPropertyNames) {
            Property sourceProperty = sourceProperties.get(name);
            Property targetProperty = targetProperties.get(name);
            targetProperty.setter.invoke(target, sourceProperty.getter.invoke(source));
        }
    }

    @Override
    @SneakyThrows
    protected void mapToBean(Map<String, Object> map, Object bean) {
        for (String name : this.commonPropertyNames) {
            Property targetProperty = targetProperties.get(name);
            Object v = map.get(name);
            if (v != null &&
                Objects.equals(v.getClass(),
                    Reflects.primitiveToWrapper(targetProperty.getter.getReturnType()))) {
                targetProperty.setter.invoke(bean, v);
            }
        }
    }

    @Override
    @SneakyThrows
    protected void beanToMap(Object bean, Map<String, Object> map) {
        for (String name : this.commonPropertyNames) {
            Object v = sourceProperties.get(name).getter.invoke(bean);
            if (v != null) {
                map.put(name, v);
            }
        }
    }


}
