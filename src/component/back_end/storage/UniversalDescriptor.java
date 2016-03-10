package component.back_end.storage;

import component.back_end.storage.TaskDescriptor;


/**
 * This singleton descriptor matches all Tasks.
 * Use when selecting all Tasks from the store.
 * 
 * TODO:        Consider whether to remove this in the future, as getAll in TaskCollection
 *              supports simply passing in null to achieve the same behaviour.
 * 
 * created by thenaesh on 10 Mar 2016
 *
 */
public class UniversalDescriptor extends TaskDescriptor {

    // singleton functionality
    private static UniversalDescriptor descriptor = null;
    private UniversalDescriptor() {
    }
    
    // singleton accessor
    public static UniversalDescriptor get() {
        if (descriptor == null) {
            descriptor = new UniversalDescriptor();
        }
        
        return descriptor;
    }
    @Override
    public boolean matches(Task task) {
        return true;
    }

}
