package objects;


/**
 * Index is the type of IDs used in both ID systems (see {@link front_end.TaskIndexMapperSpec}).
 * We do this to ensure we can change the actual type of IDs without any repercussions.
 * 
 * created by thenaesh on Mar 6, 2016
 *
 */
public class Index implements Comparable<Index>{
    private Long rawIndex;
   
    /**
     * ctor that initialises the Index object with a rawIndex
     * @param rawIndex
     */
    public Index(Long rawIndex) {
        this.rawIndex = rawIndex;
    }
    
    @Override
    public boolean equals(Object o) {
        Long rawIndexOther = ((Index) o).rawIndex;
        return rawIndex.equals(rawIndexOther);
    }

    @Override
    public int compareTo(Index o) {
        Long rawIndexOther = o.rawIndex;
        
        if (rawIndex < rawIndexOther) {
            return -1;
        } else if (rawIndex > rawIndexOther) {
            return 1;
        } else {
            return 0;
        }
    }
}
