/**
 * Copyright 2015 Thomas Cashman
 */
package beansl.util;

/**
 *
 * @author Thomas Cashman
 */
public class StringUtils  {

    public static String toCamelCase(String value) {
        if(!Character.isLowerCase(value.charAt(0))) {
            value = value.substring(0, 1).toLowerCase() + value.substring(1);
        }
        return value;
    }
}
