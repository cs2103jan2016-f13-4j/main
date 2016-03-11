package exception.back_end.storage;

import component.back_end.storage.rdbs.PrimaryKeyInterface;
import component.back_end.storage.rdbs.RelationInterface;

/**
 * Created by maianhvu on 7/3/16.
 */
public class PrimaryKeyAlreadyExistsException extends Exception {
    private PrimaryKeyInterface primaryKey_;
    private Class<? extends RelationInterface> relationClass_;

    public PrimaryKeyAlreadyExistsException(PrimaryKeyInterface primaryKey,
                                            Class<? extends RelationInterface> relationClass) {
        this.primaryKey_ = primaryKey;
        this.relationClass_ = relationClass;
    }

    @Override
    public String getMessage() {
        return String.format("Primary key %s already exists for class %s",
                primaryKey_.toString(),
                relationClass_.getSimpleName());
    }
}
