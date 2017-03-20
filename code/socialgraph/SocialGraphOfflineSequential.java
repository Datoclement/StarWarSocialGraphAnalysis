package socialgraph;

import java.util.*;

import completesocialgraph.*;

/**
 * Offline and sequential version of SocialGraph
 */
public class SocialGraphOfflineSequential extends SocialGraph{

    /**
     * the local complete social graph
     */
    private Map<String, LinkedList<String>> csg;

    public SocialGraphOfflineSequential(String root, int depth){
        super();
        this.csg = new CompleteSocialGraphReader().getGraph();
        this.searchSequential(root,depth);
    }

    protected LinkedList<String> findNeighbors(String cur){
        return csg.get(cur);
    }
}
