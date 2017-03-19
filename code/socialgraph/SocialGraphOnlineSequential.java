package socialgraph;

import java.util.*;
import util.*;

/**
 * Online and sequential version of SocialGraph
 */
public class SocialGraphOnlineSequential extends SocialGraph{

    /**
     * list of names of characters
     */
    private HashSet<String> ct;

    public SocialGraphOnlineSequential(String root, int depth, List<String> ct){
        super();
        this.ct = new HashSet<String>(ct);
        this.searchSequential(root,depth);
    }

    protected LinkedList<String> findNeighbors(String character){
        HashSet<String> n = new HashSet<String>();
        String pageData = new SourceCode(character).content;
        int head = pageData.indexOf("<article");
        int tail = pageData.indexOf("</article>", head);
        int cur = head;
        while(true){
            cur = pageData.indexOf("\"/wiki/", cur+1);
            if((cur == -1) || (cur > tail)) return new LinkedList<String>(n);;
            cur += 7;
            int end = pageData.indexOf('\"', cur+1);
            String url = pageData.substring(cur, end);
            if(this.ct.contains(url))
                n.add(url);
        }
    }
}
