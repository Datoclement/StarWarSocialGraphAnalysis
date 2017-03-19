package socialgraph;

import java.util.*;

/**
 * Parallel and offline version of SocialGraph
 */
public class SocialGraphOfflineParallel extends SocialGraph{

    /**
     * the local complete social graph
     */
    private Map<String, LinkedList<String>> csg;

    public SocialGraphOfflineParallel(String root, int depth, Map<String, LinkedList<String>> csg){
        super();
        this.csg = csg;
        this.searchParallel(root,depth);
    }

    protected LinkedList<String> findNeighbors(String cur){
        return csg.get(cur);
    }
}
