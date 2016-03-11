package component.back_end.storage.query;

import component.back_end.storage.Task;

/**
 * 
 * @author Huiyie
 *
 */

public abstract class TaskDescriptor {
    public abstract boolean matches(Task task);
}
