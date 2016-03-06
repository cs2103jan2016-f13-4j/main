package component.front_end;

import entity.*;


/**
 * Tasks have two different indices:
 *      1) an actual id, globally unique, used by the Data Store to index the task
 *      2) a visual id, used to interact with the user
 *              -> ordering when displaying tasks
 *              -> user selection of tasks to edit or delete
 * 
 * This dual-id system exists because we do not wish to scare the user by exposing the actual internal id
 * to him (possibly a big scary globally-unique hexadecimal number that goes around town frightening little kids).
 * 
 * The job of the Task Index Mapper is to maintain state of the current mapping between the two index systems.
 * This mapping may change at every Dispatcher heartbeat, but is always a bijection.
 * 
 * Rank: Second Lieutenant (reports to CPT Translation Engine)
 * 
 * created by thenaesh on Mar 6, 2016
 *
 */
public abstract class TaskIndexMapperSpec {
    public abstract Index translateRawToVisual(Index rawID);
    public abstract Index translateVisualToRaw(Index visualID);
}
