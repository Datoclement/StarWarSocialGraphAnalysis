package socialgraph;

import java.util.*;

import completesocialgraph.*;

/**
 * Parallel and offline version of SocialGraph
 */
public class SocialGraphOfflineParallel extends SocialGraph{

    /**
     * the local complete social graph
     */
    private Map<String, LinkedList<String> > csg;

    public SocialGraphOfflineParallel(String root, int depth){
        super();
        this.csg = new CompleteSocialGraphReader().getGraph();
        this.searchParallel(root,depth);
    }

    protected LinkedList<String> findNeighbors(String cur){
        return csg.get(cur);
    }
}
