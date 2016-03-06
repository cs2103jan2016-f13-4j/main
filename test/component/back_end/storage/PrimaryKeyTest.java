package component.back_end.storage;

import static org.junit.Assert.*;
import org.junit.Test;

import component.back_end.storage.PrimaryKey;

/**
 * 
 * @author Huiyie
 *
 */

public class PrimaryKeyTest {
    
    @Test
    public void Same_string_primary_keys_are_equal() {
        PrimaryKey<String> pKey1 = new PrimaryKey<String>("value");
        PrimaryKey<String> pKey2 = new PrimaryKey<String>("value");
        assertEquals(0, pKey1.compareTo(pKey2));
    }
    
    @Test
    public void Same_integer_primary_keys_are_equal() {
        PrimaryKey<Integer> pKey3 = new PrimaryKey<Integer>(13);
        PrimaryKey<Integer> pKey4 = new PrimaryKey<Integer>(13);
        assertEquals(0, pKey3.compareTo(pKey4));
    }
    
    @Test
    public void Different_string_primary_keys_are_not_equal() {
        PrimaryKey<String> pKey5 = new PrimaryKey<String>("A");
        PrimaryKey<String> pKey6 = new PrimaryKey<String>("B");
        assertNotEquals(0, pKey5.compareTo(pKey6));
    }
    
    @Test
    public void Different_integer_primary_keys_are_not_equal() {
        PrimaryKey<Integer> pKey7 = new PrimaryKey<Integer>(7);
        PrimaryKey<Integer> pKey8 = new PrimaryKey<Integer>(8);
        assertNotEquals(0, pKey7.compareTo(pKey8));
    }
    
}