package ui.view;

import javafx.scene.Node;

public abstract class View {

    /**
     * Properties
     */
    private Object _data;
    private Node _component;

    public View(Object data) {
        this._data = data;
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

    protected abstract void buildContent();

    protected void setComponent(Node component) {
        this._component = component;
    }

    public Node getComponent() {
        assert this._component != null;
        return this._component;
    }
}