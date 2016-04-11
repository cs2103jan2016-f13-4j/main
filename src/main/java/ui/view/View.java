package ui.view;

import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import shared.Command;

import java.util.function.Function;

/**
 * View is the abstract class which controls the display and the behavior of the content displayed in the User Interface.
 *
 *
 * @@author A0127046L
 */
public abstract class View {

    /**
     * Properties
     */
    private Object _data;
    private Command _lastCommand;
    private Node _component;

    public View(Object data, Command lastCommand) {
        this._data = data;
        if (lastCommand != null) {
            this._lastCommand = lastCommand;
        } else {
            this._lastCommand = Command.initialCommand();
        }
        this.buildContent();
    }

    protected <T> T getData() {
        try {
            return (T) this._data;
        } catch (ClassCastException e) {
            assert false; // Explode
            return null;
        }
    }

    protected Command getLastCommand() {
        return this._lastCommand;
    }

    /**
     * this method is called to register the view
     */
    protected abstract void buildContent();

    protected void setComponent(Node component) {
        component.setId("component--main");
        this._component = component;
    }

    public Node getComponent() {
        assert this._component != null;
        return this._component;
    }

    public void setData(Object data) {
        this._data = data;
    }

    public Function<KeyEvent, Boolean> getKeyInputInterceptor() {
        return (event -> false);
    }
}
