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


package io.github.javajerrat.boost.lang.reflect;

import static org.junit.jupiter.api.Assertions.*;

import io.github.javajerrat.boost.lang.reflect.Reflects.MethodReferenceTuple;
import java.awt.Point;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * @author Frapples <isfrapples@outlook.com>
 * @date 2019/12/21
 */
class ReflectsTest {

    @Test
    void getLambda() {
        {
            @Nullable MethodReferenceTuple vo = Reflects.getLambda(Point::getX);
            assertEquals(vo,
                new MethodReferenceTuple("java.awt.Point", "getX"));
        }

        {
            @Nullable MethodReferenceTuple vo = Reflects.getLambda(Object::hashCode);
            assertEquals(vo,
                new MethodReferenceTuple("java.lang.Object", "hashCode"));
        }
    }

    private static class TestMap extends HashMap<String, Object> {

    }

    private interface List1 extends List<String> {

    }

    private static class TestList1 extends ArrayList<String> implements List1 {

    }

    private static class TestList2 extends TestList1 implements List1 {
    }

    private static class TestList3 extends ArrayList<String> {
    }

    private static class TestList4 extends ArrayList<Map<String, Object>> {
    }

    private static class TestList5<T> extends ArrayList<T> {
    }

    @Test
    void getGenericSuperType() {
        {

            ParameterizedType type = Reflects.getGenericSuperType(TestMap.class, HashMap.class);
            System.out.println(type);
            assertNotNull(type);
            assertEquals(type.getRawType(), HashMap.class);
        }
        {
            ParameterizedType type = Reflects.getGenericSuperType(TestMap.class, List.class);
            System.out.println(type);
            assertNull(type);
        }

        {
            ParameterizedType type = Reflects.getGenericSuperType(TestList1.class, List.class);
            System.out.println(type);
            assertNotNull(type);
            assertEquals(type.getRawType(), ArrayList.class);
        }

        {
            ParameterizedType type = Reflects.getGenericSuperType(List1.class, List.class);
            System.out.println(type);
            assertNotNull(type);
            assertEquals(type.getRawType(), List.class);
        }

        {
            ParameterizedType type = Reflects.getGenericSuperType(TestList2.class, List.class);
            System.out.println(type);
            assertNotNull(type);
            assertEquals(type.getRawType(), ArrayList.class);
        }

        {
            ParameterizedType type = Reflects.getGenericSuperType(TestList3.class, List.class);
            System.out.println(type);
            assertNotNull(type);
            assertEquals(type.getRawType(), ArrayList.class);
        }

        {
            ParameterizedType type = Reflects.getGenericSuperType(TestList4.class, List.class);
            System.out.println(type);
            assertNotNull(type);
            assertEquals(type.getRawType(), ArrayList.class);
        }

        {
            ParameterizedType type = Reflects.getGenericSuperType(TestList5.class, List.class);
            System.out.println(type);
            assertNotNull(type);
            assertEquals(type.getRawType(), ArrayList.class);
        }
    }
}