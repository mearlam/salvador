package com.salvador;

import com.salvador.common.annotations.AutoTest;
import com.salvador.common.annotations.SkipAutoTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.reflections.ReflectionUtils.forNames;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 18/11/11
 * Time: 12:39
 *
 * Automatically does basic tests for pojo's so it keeps the unit
 * test coverage up on method we don't really need to test
 */
public class PojoTest {

    static Logger log = LoggerFactory.getLogger(PojoTest.class);

    static List pojoClasses = new ArrayList();

    private static final Byte BYTE_VALUE = 42;
    private static final Character CHAR_VALUE = '4';
    private static final String STRING_VALUE = "42";
    private static final Integer INTEGER_VALUE = 4242;
    private static final Float FLOAT_VALUE = 42.42f;
    private static final Double DOUBLE_VALUE = 4242.4242d;
    private static final Boolean BOOLEAN_VALUE = true;
    private static final Long LONG_VALUE = 42424242l;
    private static final Date DATE_VALUE = new Date();
    private static final BigDecimal BIG_DECIMAL_VALUE = new BigDecimal("424242.424242");

    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setUpClass() {

        Reflections reflections = new Reflections("com.salvador");
        Set<String> classes = reflections.getStore().getTypesAnnotatedWith(AutoTest.class.getName());
        pojoClasses = forNames(classes);
    }

    @Test
    public void testSettersAndGetters() throws IllegalAccessException,
            InstantiationException,
            InvocationTargetException {

        Map<Class, Object> mocks = new HashMap<Class, Object>();
        // grab all the methods on each class
        for (Object clazzObject : pojoClasses) {

            Class clazz = (Class) clazzObject;
            Map<String, Method> setMethods = new HashMap<String, Method>();
            Map<String, Method> getMethods = new HashMap<String, Method>();
            Method equalsMethod = null;
            Method hashCodeMethod = null;
            Method toStringMethod = null;

            Method[] methods = clazz.getDeclaredMethods();

            // load the getters and setters
            for (Method method : methods) {
                SkipAutoTest autoTest = method.getAnnotation(SkipAutoTest.class);

                // skip anything with skipautotest annotated on it
                if (autoTest == null && Modifier.isPublic(method.getModifiers())) {
                    if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
                        final Class<?> paramType = method.getParameterTypes()[0];
                        if (!(
                                mocks.containsKey(paramType)
                                        // skip Lists, Sets, Maps, etc
                                        || Modifier.isInterface(paramType.getModifiers())
                                        // Mockito can't mock final or anonymous classes or primitives
                                        || Modifier.isFinal(paramType.getModifiers())
                                        || paramType.isAnonymousClass()
                                        || paramType.isPrimitive())
                                ) {
                            mocks.put(paramType, Mockito.mock(paramType));
                        }
                        setMethods.put(method.getName(), method);
                    } else if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                        getMethods.put(method.getName(), method);
//maybe one day we can test the reverse of a boolean setter, but need to alter a number of is methods that are derived and should be marked SkipAutoTest
//                    } else if (method.getName().startsWith("is") && method.getParameterTypes().length == 0) {
//                        getMethods.put(method.getName(), method);
                    } else if (method.getName().startsWith("equals")) {
                        equalsMethod = method;
                    } else if (method.getName().startsWith("hashCode")) {
                        hashCodeMethod = method;
                    }
                    else if (method.getName().startsWith("toString")) {
                        toStringMethod = method;
                    }
                } else {
                    log.debug("Skipping: " + method.getName());
                }
            }

            // create a new class for testing
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Object pojo = clazz.newInstance();
                setDataOnPojo(setMethods, pojo, mocks);
                checkGetMethods(getMethods, pojo, mocks);
                if (equalsMethod != null) {
                    checkEqualsMethod(pojo, equalsMethod);
                }
                if (hashCodeMethod != null) {
                    testToStringMethod(pojo, hashCodeMethod);
                }
                if (toStringMethod != null) {
                    checkHashCodeMethod(pojo, toStringMethod);
                }
            }
        }
    }

    private void testToStringMethod(Object pojo, Method toStringMethod) {
        try {
            String result = (String) toStringMethod.invoke(pojo);
            // this is not a real test but we have no idea what the result will be,
            // just make sure it is not empty
            assertNotSame("", result);
        } catch (Exception e) {
            log.warn("Could not test toString for {}", pojo.getClass());
        }
    }

    private void checkHashCodeMethod(Object pojo, Method hashCodeMethod) {
        try {
            int result = (Integer) hashCodeMethod.invoke(pojo);
            assertNotSame(0, result);
        } catch (Exception e) {
            log.warn("Could not test hashCode for {}", pojo.getClass());
        }
    }

    @SuppressWarnings("NullArgumentToVariableArgMethod")
    private void checkEqualsMethod(Object pojo,Method equalsMethod) {

        try {
            Object testObject = pojo.getClass().newInstance();

            // always false test
            boolean result = (Boolean) equalsMethod.invoke(pojo, testObject);
            assertFalse(result);

            // always true test
            result = (Boolean) equalsMethod.invoke(pojo, pojo);
            assertTrue(result);

        } catch (IllegalAccessException e) {
            log.warn("Could not test equals for {}", pojo.getClass());
        } catch (InvocationTargetException e) {
            log.warn("Could not test equals for {}", pojo.getClass());
        } catch (InstantiationException e) {
            log.warn("Could not test equals for {}", pojo.getClass());
        }
    }

    /**
     * Sets basic data into the pojo using the method given
     *
     * @param setMethods The list of set methods for this class
     * @param pojo       The actual object we are setting data on
     * @param mocks      Mock values for non-simple types
     * @throws IllegalAccessException    IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException InvocationTargetException
     */
    private void setDataOnPojo(Map<String, Method> setMethods, Object pojo, Map<Class, Object> mocks) throws IllegalAccessException, InvocationTargetException {
        for (String key : setMethods.keySet()) {
            Method method = setMethods.get(key);
            Class paramTypes[] = method.getParameterTypes();

            // anything with more than one param we ignore
            // set defaults for the values
            if (paramTypes.length == 1) {
                if (paramTypes[0] == String.class) {
                    method.invoke(pojo, STRING_VALUE);
                } else if (paramTypes[0] == Integer.TYPE || paramTypes[0] == Integer.class) {
                    method.invoke(pojo, INTEGER_VALUE);
                } else if (paramTypes[0] == Character.TYPE || paramTypes[0] == Character.class) {
                    method.invoke(pojo, CHAR_VALUE);
                } else if (paramTypes[0] == Byte.TYPE || paramTypes[0] == Byte.class) {
                    method.invoke(pojo, BYTE_VALUE);
                } else if (paramTypes[0] == Long.TYPE || paramTypes[0] == Long.class) {
                    method.invoke(pojo, LONG_VALUE);
                } else if (paramTypes[0] == Float.TYPE || paramTypes[0] == Float.class) {
                    method.invoke(pojo, FLOAT_VALUE);
                } else if (paramTypes[0] == Double.TYPE || paramTypes[0] == Double.class) {
                    method.invoke(pojo, DOUBLE_VALUE);
                } else if (paramTypes[0] == Date.class) {
                    method.invoke(pojo, DATE_VALUE);
                } else if (paramTypes[0] == Boolean.TYPE || paramTypes[0] == Boolean.class) {
                    method.invoke(pojo, BOOLEAN_VALUE);
                } else if (paramTypes[0] == BigDecimal.class) {
                    method.invoke(pojo, BIG_DECIMAL_VALUE);
                } else {
                    if (mocks.containsKey(paramTypes[0])) {
                        method.invoke(pojo, mocks.get(paramTypes[0]));
                    } else {
                        log.info("Missing type for testing setter '{}'", paramTypes[0].getName());
                    }
                }
            }
        }
    }

    /**
     * Gets the data from the pojo
     *
     * @param getMethods The get methods for this class
     * @param pojo       The object we are getting data from
     * @param mocks      Mock values for non-simple types
     * @throws IllegalAccessException    IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException InvocationTargetException
     */
    private void checkGetMethods(Map<String, Method> getMethods, Object pojo, Map<Class, Object> mocks) throws IllegalAccessException, InvocationTargetException {
        // now we call all the get methods and check the value
        for (String key : getMethods.keySet()) {
            Method method = getMethods.get(key);

            log.debug("Checking " + pojo.getClass().getName() + ":" + method.getName());
            Object value = method.invoke(pojo);
            final String failureMessage = "Getter is not returning what was set: " + pojo.getClass().getName() + '.' + method.getName();

            if (value instanceof String) {
                assertEquals(failureMessage, STRING_VALUE, value);
            } else if (value instanceof Integer) {
                assertEquals(failureMessage, INTEGER_VALUE, value);
            } else if (value instanceof Character) {
                assertEquals(failureMessage, CHAR_VALUE, value);
            } else if (value instanceof Byte) {
                assertEquals(failureMessage, BYTE_VALUE, value);
            } else if (value instanceof Long) {
                assertEquals(failureMessage, LONG_VALUE, value);
            } else if (value instanceof Float) {
                assertEquals(failureMessage, FLOAT_VALUE, value);
            } else if (value instanceof Double) {
                assertEquals(failureMessage, DOUBLE_VALUE, value);
            } else if (value instanceof Date) {
                assertEquals(failureMessage, DATE_VALUE, value);
            } else if (value instanceof Boolean) {
                assertEquals(failureMessage, BOOLEAN_VALUE, value);
            } else if (value instanceof BigDecimal) {
                assertEquals(failureMessage, BIG_DECIMAL_VALUE, value);
            } else {
                Class returnType = method.getReturnType();
                if (mocks.containsKey(returnType)) {
                    assertSame(failureMessage, mocks.get(returnType), value);
                }
            }
        }
    }
}
