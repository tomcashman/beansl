/**
 * Copyright 2015 Thomas Cashman
 */
package beansl

import java.lang.reflect.Field

/**
 *
 * @author Thomas Cashman
 */
class DslWrapper<T> {
    Class clazz;
    T instance;

    DslWrapper(Class clazz, T instance) {
        this.clazz = clazz;
        this.instance = instance;

        ExpandoMetaClass metaClass = new ExpandoMetaClass(DslWrapper, false, true);
        metaClass.initialize();
        this.metaClass = metaClass;

        createClosureMethodsForProperties();
    }

    def call = { Closure closure ->
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_ONLY)
        closure.call();
    }

    def createClosureMethodsForProperties = {
        Field [] fields = clazz.getDeclaredFields();
        for(int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.accessible = true;
            createProperty(field);
        }
    }

    def createProperty(Field field) {
        def fieldName = field.name;
        def fieldType = field.type;

        if(fieldType.isPrimitive()) {
            if(fieldType.equals(Integer.TYPE)) {
                this.metaClass."${fieldName}" = { int value ->
                    field.setInt(instance, value);
                };
            } else if(fieldType.equals(Long.TYPE)) {
                this.metaClass."${fieldName}" = { long value ->
                    field.setLong(instance, value);
                };
            } else if(fieldType.equals(Short.TYPE)) {
                this.metaClass."${fieldName}" = { short value ->
                    field.setShort(instance, value);
                };
            } else if(fieldType.equals(Byte.TYPE)) {
                this.metaClass."${fieldName}" = { byte value ->
                    field.setByte(instance, value);
                };
            } else if(fieldType.equals(Float.TYPE)) {
                this.metaClass."${fieldName}" = { float value ->
                    field.setFloat(instance, value);
                };
            } else if(fieldType.equals(Character.TYPE)) {
                this.metaClass."${fieldName}" = { char value ->
                    field.setChar(instance, value);
                };
            } else {
                println "Unknown primitive for field {$fieldName}"
            }
        } else if(fieldType.isArray()) {
            if(fieldType.getComponentType().equals(Integer.TYPE)) {
                this.metaClass."${fieldName}" = { int [] value ->
                    field.set(instance, value);
                };
            } else if(fieldType.getComponentType().equals(Long.TYPE)) {
                this.metaClass."${fieldName}" = { long [] value ->
                    field.set(instance, value);
                };
            } else if(fieldType.getComponentType().equals(Short.TYPE)) {
                this.metaClass."${fieldName}" = { short... value ->
                    field.set(instance, value);
                };
            } else if(fieldType.getComponentType().equals(Character.TYPE)) {
                this.metaClass."${fieldName}" = instance."${fieldName}";
                println  this.metaClass."${fieldName}"
            }
        } else if(fieldType.equals(String.class)) {
            this.metaClass."${fieldName}" = { String value ->
                field.set(instance, value);
            };
        } else {
            DslWrapper fieldValue = new DslWrapper(fieldType, fieldType.newInstance());
            this.metaClass."${fieldName}" = { Closure closure ->
                closure.setDelegate(fieldValue);
                closure.setResolveStrategy(Closure.DELEGATE_ONLY);
                closure();
                field.set(instance, fieldValue.instance);
            }
        }
    }
}
