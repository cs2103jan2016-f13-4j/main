package front_end;

import java.util.List;
import objects.*;


public abstract class TranslationEngineSpec {
    void display(List<Task> taskList) {
    }
    
    Command getCommand() {
        return null;
    }
}
