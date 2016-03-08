package component.back_end.storage.rdbs;

/**
 * 
 * @author Huiyie
 *
 */

public class DataStoreTest {
    
//    private DataStore dataStore_;
//
//    @Before
//    public void setUp() {
//        this.dataStore_ = new DataStore();
//        this.dataStore_.createCollectionFor(TestRelation.class);
//    }
//
//    @Test
//    public void DataStore_stores_element_after_adding() throws PrimaryKeyAlreadyExistsException {
//        TestRelation tuple = new TestRelation("randomKey");
//        this.dataStore_.add(tuple);
//
//        PrimaryKey<String> key = new PrimaryKey<>("randomKey");
//        assertThat(this.dataStore_.get(TestRelation.class, key), is(equalTo(tuple)));
//    }
//
//    @Test(expected = PrimaryKeyAlreadyExistsException.class)
//    public void DataStore_throws_exception_upon_adding_duplicate_primary_key() throws PrimaryKeyAlreadyExistsException {
//        String duplicateKey = "duplicateKey";
//        this.dataStore_.add(new TestRelation(duplicateKey));
//        this.dataStore_.add(new TestRelation(duplicateKey));
//    }
//
//    @Test
//    public void DataStore_does_not_have_element_after_removing() throws PrimaryKeyAlreadyExistsException {
//        this.dataStore_.add(new TestRelation("hello!"));
//        PrimaryKey<String> key = new PrimaryKey<>("hello!");
//        this.dataStore_.remove(TestRelation.class, key);
//        assertThat(this.dataStore_.get(TestRelation.class, key), is(nullValue()));
//    }
//
//    @Test
//    public void DataStore_returns_null_upon_removal() {
//        TestRelation result = this.dataStore_.remove(
//                TestRelation.class,
//                new PrimaryKey<>("non existent key")
//                );
//        assertThat(result, is(nullValue()));
//    }
//
//    @Test
//    public void DataStore_returns_removed_tuple_upon_successful_removal() throws PrimaryKeyAlreadyExistsException {
//        TestRelation tuple = new TestRelation("whee!");
//        this.dataStore_.add(tuple);
//
//        PrimaryKey<String> key = new PrimaryKey<>("whee!");
//        TestRelation removedTuple = this.dataStore_.remove(TestRelation.class, key);
//    }
//
//    @Test
//    public void DataStore_returns_null_on_getting_wrong_primary_key_type() throws PrimaryKeyAlreadyExistsException {
//        this.dataStore_.add(new TestRelation("hello!"));
//        PrimaryKey<Integer> integerKey = new PrimaryKey<>(5);
//
//        assertThat(this.dataStore_.get(TestRelation.class, integerKey), is(nullValue()));
//    }
//
//    @Test
//    public void DataStore_returns_null_on_removing_wrong_primary_key_type() throws PrimaryKeyAlreadyExistsException {
//        this.dataStore_.add(new TestRelation("boo!"));
//        PrimaryKey<Integer> integerKey = new PrimaryKey<>(5);
//
//        assertThat(this.dataStore_.remove(TestRelation.class, integerKey), is(nullValue()));
//    }
//
//    @Test
//    public void DataStore_returns_all_sorted_via_getAll_with_null_descriptor()
//            throws PrimaryKeyAlreadyExistsException {
//        this.dataStore_.add(new TestRelation("relation 51"));
//        this.dataStore_.add(new TestRelation("relation 52"));
//        this.dataStore_.add(new TestRelation("relation 50"));
//
//        List<TestRelation> tuples = this.dataStore_.getAll(TestRelation.class, null);
//        Iterator<TestRelation> it = tuples.iterator();
//        assertThat(it.next().getPrimaryKey(), is(equalTo("relation 50")));
//        assertThat(it.next().getPrimaryKey(), is(equalTo("relation 51")));
//        assertThat(it.next().getPrimaryKey(), is(equalTo("relation 52")));
//    }
//
//    @Test
//    public void DataStore_returns_valid_records_with_descriptor() throws PrimaryKeyAlreadyExistsException {
//        this.dataStore_.add(new TestRelation("twinkle twinkle little star"));
//        this.dataStore_.add(new TestRelation("how I wonder what you are"));
//        this.dataStore_.add(new TestRelation("up above the world so high"));
//        this.dataStore_.add(new TestRelation("like a diamond in the sky"));
//
//        RelationConstraint<TestRelation> indexHasWo = new RelationConstraint<TestRelation>() {
//            @Override
//            public boolean matches(TestRelation tuple) {
//                return tuple.getPrimaryKey().getValue().contains("wo");
//            }
//        };
//
//        List<TestRelation> tuplesWithWo = this.dataStore_.getAll(TestRelation.class, indexHasWo);
//        Iterator<TestRelation> it = tuplesWithWo.iterator();
//        assertThat(it.next().getPrimaryKey(), is(equalTo("how I wonder what you are")));
//        assertThat(it.next().getPrimaryKey(), is(equalTo("up above the world so high")));
//    }

}
