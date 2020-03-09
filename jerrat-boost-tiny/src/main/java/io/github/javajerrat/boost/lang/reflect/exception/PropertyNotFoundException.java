package io.github.javajerrat.boost.lang.reflect.exception;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/25
 */

public class PropertyNotFoundException extends Exception {

    public PropertyNotFoundException(Throwable e) {
        super(e);
    }

    public PropertyNotFoundException(String propertyName) {
        super(message(propertyName));
    }

    public PropertyNotFoundException(String propertyName, NoSuchMethodException e) {
        super(message(propertyName), e);
    }

    private static String message(String propertyName) {
        return String.format("Property named %s not found", propertyName);
    }
}
