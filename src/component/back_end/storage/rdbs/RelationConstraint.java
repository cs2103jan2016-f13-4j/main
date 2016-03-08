package component.back_end.storage.rdbs;


public abstract class RelationConstraint<T extends RelationInterface> {
    public abstract boolean matches(T tuple);
}
