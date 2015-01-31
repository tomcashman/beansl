/**
 * Copyright 2015 Thomas Cashman
 */
package beansl.util;

import java.util.ArrayList;

/**
 *
 * @author Thomas Cashman
 */
public class TestDslParent {
    private String str;
    
    private ArrayList<String> items = new ArrayList<String>();
    
    private int value;
    
    private TestDslChild child;
    
    public TestDslParent() {
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TestDslChild getChild() {
        return child;
    }

    public void setChild(TestDslChild child) {
        this.child = child;
    }
}
