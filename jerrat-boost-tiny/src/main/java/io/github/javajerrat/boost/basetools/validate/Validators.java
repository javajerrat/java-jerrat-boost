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


package io.github.javajerrat.boost.basetools.validate;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/6/24
 */

public class Validators {

    public static <T> T validateOrThrow(Validator validator, T bean) {
        validateOrThrow(validator, bean, ValidationException::new);
        return bean;
    }

    public static String validateAsErrorString(Validator validator, Object bean) {
        Collection<?> collection;
        if (bean instanceof Collection<?>) {
            collection = (Collection<?>)bean;
        } else if (bean instanceof Map<?, ?>) {
            collection = ((Map<?, ?>) bean).values();
        } else {
            collection = Collections.singletonList(bean);
        }

        for (Object item : collection) {
            Set<ConstraintViolation<Object>> result = validator.validate(item);
            if (result.size() > 0) {
                return result.stream().map(e -> e.getPropertyPath() + e.getMessage())
                    .collect(Collectors.joining(";"));
            }
        }
        return null;
    }

    public static <T extends RuntimeException> void validateOrThrow(Validator validator, Object bean, Function<String, ? extends T> function) throws T {
        String error = validateAsErrorString(validator, bean);
        if (error != null) {
            throw function.apply(error);
        }
    }
}
