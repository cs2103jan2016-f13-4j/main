package component.back_end.storage.query;

import component.back_end.storage.Task;

/**
 * Created by maianhvu on 15/03/2016.
 */
public class SearchDescriptor extends TaskDescriptor {

    private String _query;
    private boolean _isCaseSensitive;

    public SearchDescriptor(String query, boolean caseSensitive) {
        this._query = query.trim();
        this._isCaseSensitive = caseSensitive;

        if (!caseSensitive) {
            this._query = this._query.toLowerCase();
        }
    }

    public SearchDescriptor(String query) {
        this(query, false);
    }

    @Override
    public boolean matches(Task task) {
        return this.containsQuery(task.getTaskName()) ||
                this.containsQuery(task.getDescription());
    }

    private boolean containsQuery(String haystack) {
        if (!this._isCaseSensitive) {
            haystack = haystack.toLowerCase();
        }

        return haystack.contains(this._query);
    }
}
