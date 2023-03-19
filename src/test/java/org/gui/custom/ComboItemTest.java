package org.gui.custom;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComboItemTest {

    @Test
    public void testToString() {
        ComboItem comboItem = new ComboItem("key", "value");
        assertEquals("key", comboItem.toString());
    }

    @Test
    public void testGetKey() {
        ComboItem comboItem = new ComboItem("key", "value");
        assertEquals("key", comboItem.getKey());
    }

    @Test
    public void testGetValue() {
        ComboItem comboItem = new ComboItem("key", "value");
        assertEquals("value", comboItem.getValue());
    }

}