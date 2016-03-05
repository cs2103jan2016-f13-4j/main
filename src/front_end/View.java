package front_end;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import front_end.ui.base.UserInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by maianhvu on 5/3/16.
 */
public class View<T> {

    private UserInterface<T> userInterface_;

    public View(Class<? extends UserInterface<T>> uiClass, T data) {
        this.constructUserInterface(uiClass, data);
    }

    private void constructUserInterface(Class<? extends UserInterface<T>> uiClass, T data) {
        // Get all available constructors for the UserInterface
        Constructor<?> constructors[] = uiClass.getConstructors();

        // Find appropriate constructor for the user interface
        // and attempt to initialize the user interface with it
        for (Constructor<?> constructor : constructors) {
            try {
                Constructor<? extends UserInterface<T>> constr =
                        (Constructor<? extends UserInterface<T>>) constructor;
                this.userInterface_ = constr.newInstance(data);
                break;
            } catch (Exception e) {
                continue;
            }
        }
    }

    public UserInterface<T> getUI() {
        return this.userInterface_;
    }

    public T getData() {
        return this.userInterface_.getData();
    }
}
