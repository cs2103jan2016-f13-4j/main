package component.back_end.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import entity.Task;

public class DataStoreTest {
    
    private DataStore dataStore_;
    private RelationInterface tuple_;
    
    @Before
    public void setUp() {
        this.dataStore_ = new DataStore();
        
        // initialize attributes
        String taskName = "homework";
        String description = "cs2103t";
        LocalDateTime taskStart = LocalDateTime.of(2016, 3, 6, 14, 30);
        LocalDateTime taskEnd = LocalDateTime.of(2016, 3, 7, 14, 30);
        
        // create tuple
        this.tuple_ = new Task(taskName, description, taskStart, taskEnd);
    }

    @Test
    public void Add_creates_TreeMap() {
        
        // execute add
        this.dataStore_.add(this.tuple_);
        
        HashMap<Class<? extends RelationInterface>, TreeMap<PrimaryKeyInterface<?>, RelationInterface>> 
            storageMap = this.dataStore_.getStorageMap();
        
        // check that a TreeMap with primary key has been created
        assertNotNull(storageMap.get(this.tuple_.getClass()));
        
    }
    
    @Test
    public void Primary_key_of_TreeMap_leads_to_correct_tuple() {
        // execute add
        this.dataStore_.add(this.tuple_);
        
        HashMap<Class<? extends RelationInterface>, TreeMap<PrimaryKeyInterface<?>, RelationInterface>> 
            storageMap = this.dataStore_.getStorageMap();
        
        // check that the tuples are the same
        assertEquals(this.tuple_, storageMap.get(this.tuple_.getClass()).get(this.tuple_.getPrimaryKey()));
    }
    
    @Test
    public void Different_primary_keys_point_to_different_tuples_in_TreeMap() {
        
        // initialize attributes
        String taskName = "floorball training";
        String description = "in MPSH";
        LocalDateTime taskStart = LocalDateTime.of(2016, 3, 6, 14, 30);
        LocalDateTime taskEnd = LocalDateTime.of(2016, 3, 7, 14, 30);
        
        // create tuple
        RelationInterface tuple2 = new Task(taskName, description, taskStart, taskEnd);
        
        // execute adds
        this.dataStore_.add(this.tuple_);
        this.dataStore_.add(tuple2);
        
        HashMap<Class<? extends RelationInterface>, TreeMap<PrimaryKeyInterface<?>, RelationInterface>> 
            storageMap = this.dataStore_.getStorageMap();
        
        // use primary keys of tuple_ and tuple2 to retrieve them from HashMap
        assertNotEquals(storageMap.get(this.tuple_.getClass()).get(this.tuple_.getPrimaryKey()),
            storageMap.get(tuple2.getClass()).get(tuple2.getPrimaryKey()));
    }
}
