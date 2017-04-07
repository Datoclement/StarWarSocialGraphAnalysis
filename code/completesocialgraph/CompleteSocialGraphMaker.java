package completesocialgraph;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import util.*;
import characterscraper.*;

/**
 * A class that goes through Wookiepedia
 * to draw a total social graph of Star War
 */
public class CompleteSocialGraphMaker {

    /**
     * Set of characters' names
     */
    private HashSet<String> characters;

    /**
     * Structure to store the edges in the graph
     */
    private HashMap<String, HashSet<String> > neighbors;

    /**
     * destination to store the final social graph
     */
    private String graphFile = "SocialGraphComplete.txt";

    CompleteSocialGraphMaker() throws IOException{

        this.characters = new HashSet<String>(new CharacterTableReader().getList());
        this.neighbors = new HashMap<String, HashSet<String>>();

        for(String c : this.characters){
            HashSet<String> n = this.findNeighbors(c);
            this.neighbors.put(c, n);
        }
    }

    /**
     * to find neighbors of a given character
     */
    public HashSet<String> findNeighbors(String character){
        HashSet<String> n = new HashSet<String>();
        String pageData = new SourceCode(character).content;
        int head = pageData.indexOf("<article");
        int tail = pageData.indexOf("</article>", head);
        int cur = head;
        while(true){
            cur = pageData.indexOf("\"/wiki/", cur+1);
            if((cur == -1) || (cur > tail)) return n;
            cur += 7;
            int end = pageData.indexOf('\"', cur+1);
            String url = pageData.substring(cur, end);
            if(this.characters.contains(url))
                n.add(url);
        }
    }

    /**
     * to save the social graph into a local file
     */
    void save(){
        try{
            // HashMap<String, HashSet<String> > n = this.neighbors;
            // HashSet<String> c = this.characters;
            PrintWriter out = new PrintWriter(graphFile);
            for(String character:this.characters){
                    out.print(character+"    ");
                    for(String n : this.neighbors.get(character)){
                        out.print(n + " ");
                    }
                    out.println();
                    out.flush();
            }
            out.close();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {

        CompleteSocialGraphMaker socialGraph = new CompleteSocialGraphMaker();

        socialGraph.save();
    }
}
