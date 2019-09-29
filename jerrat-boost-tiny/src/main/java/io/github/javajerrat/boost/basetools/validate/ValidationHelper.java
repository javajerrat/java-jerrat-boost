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

import com.google.common.base.Suppliers;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/7/25
 */
public class ValidationHelper {

    private static final Supplier<ValidationHelper> DEFAULT_INSTANCE = Suppliers.memoize(ValidationHelper::new);

    public static ValidationHelper of() {
        return DEFAULT_INSTANCE.get();
    }

    private Validator validator;

    private Function<String, ? extends RuntimeException> exceptionSupplier;

    private ValidationHelper() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.exceptionSupplier = ValidationException::new;
    }

    private ValidationHelper(Validator validator, Function<String, ? extends RuntimeException> exceptionSupplier) {
        this.validator = validator;
        this.exceptionSupplier = exceptionSupplier;
    }


    public ValidationHelper withExceptionSupplier(Function<String, ? extends RuntimeException> exceptionSupplier) {
        return new ValidationHelper(validator, exceptionSupplier);
    }

    public ValidationHelper withValidator(Validator validator) {
        return new ValidationHelper(validator, exceptionSupplier);
    }

    public <T> T validateOrThrow(T bean) {
        Validators.validateOrThrow(validator, bean, exceptionSupplier);
        return bean;
    }

    public String validateAsErrorString(Object bean) {
        return Validators.validateAsErrorString(validator, bean);
    }
}
