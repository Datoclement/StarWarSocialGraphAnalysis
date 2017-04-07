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

    private LinkedBlockingQueue<String> characters;
    private ConcurrentHashMap<String, HashSet<String>> neighbors;
    private String graphFile = "completesocialgraph/SocialGraphComplete.txt";

    CompleteSocialGraphMaker() throws IOException{
        //read characters from the file and store them into Hashset Characters

        this.characters = new LinkedBlockingQueue<String>(new CharacterTableReader().getList());
        this.neighbors = new ConcurrentHashMap<String, HashSet<String>>();

        // for(String c : this.characters){
        //     HashSet<String> n = this.findNeighbors(c);
        //     this.neighbors.put(c, n);
        //     // System.out.println(c + " " + n.size());
        // }
        int n = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[n];
        for(int i = 0; i < n; i++){
            threads[i] = new Thread(new Runnable(){
                public void run(){
                    String c = characters.poll();
                    while(c != null){
                        HashSet<String> n = findNeighbors(c);
                        neighbors.put(c,n);
                        c = characters.poll();
                    }
                }
            });
            threads[i].start();
        }
        try{
            for(int i=0;i<n;i++)
                threads[i].join();
        }
        catch(Exception e){e.printStackTrace();}
    }

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
