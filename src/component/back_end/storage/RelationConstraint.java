package component.back_end.storage;


public abstract class RelationConstraint<T extends RelationInterface> {
    public abstract boolean matches(T tuple);
}
