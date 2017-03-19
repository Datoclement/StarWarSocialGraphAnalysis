package socialgraph;

import java.util.*;

/**
 * Offline and sequential version of SocialGraph
 */
public class SocialGraphOfflineSequential extends SocialGraph{

    /**
     * the local complete social graph
     */
    private Map<String, LinkedList<String>> csg;

    public SocialGraphOfflineSequential(String root, int depth, Map<String, LinkedList<String>> csg){
        super();
        this.csg = csg;
        this.searchSequential(root,depth);
    }

    protected LinkedList<String> findNeighbors(String cur){
        return csg.get(cur);
    }
}
