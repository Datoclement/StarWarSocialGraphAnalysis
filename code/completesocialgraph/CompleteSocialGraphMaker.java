package completesocialgraph;

import java.io.*;
import java.util.*;
import java.text.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

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
    private LinkedBlockingQueue<String> characters;

    /**
     * Structure to store the edges in the graph
     */
    private ConcurrentHashMap<String, HashSet<String> > neighbors;

    /**
     * destination to store the final social graph
     */
    private String graphFile = "completesocialgraph/csg.txt";

    /**
     * number of processed nodes
     */
    final AtomicInteger cnt = new AtomicInteger(0);

    public CompleteSocialGraphMaker(){

        this.characters = new LinkedBlockingQueue<String>(new CharacterTableReader().getList());
        this.neighbors = new ConcurrentHashMap<String, HashSet<String>>();

        LinkedBlockingQueue<String> characters = new LinkedBlockingQueue<String>(new CharacterTableReader().getList());
        // for(String c : this.characters){
        //     HashSet<String> n = this.findNeighbors(c);
        //     this.neighbors.put(c, n);
        //     // System.out.println(c + " " + n.size());
        // }
        int n = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[n];

        for(int i = 0; i < n; i++){
            final int id = i;
            threads[id] = new Thread(new Runnable(){
                public void run(){
                    String c = characters.poll();
                    while(c != null){
                        HashSet<String> n = findNeighbors(c);
                        neighbors.put(c,n);
                        c = characters.poll();
                        // CompleteSocialGraphMaker.this.save();
                        CompleteSocialGraphMaker.this.printProcess();
                    }
                }
            });
            threads[id].start();
        }
        try{
            for(int i=0;i<n;i++)
                threads[i].join();
        }
        catch(Exception e){e.printStackTrace();}
        System.out.println("\ndone\n");
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
    public void save(){
        try{
            // HashMap<String, HashSet<String> > n = this.neighbors;
            // HashSet<String> c = this.characters;
            PrintWriter out = new PrintWriter(graphFile);
            for(String character:this.characters){
                    out.print(character+"    ");
                    if(!this.neighbors.containsKey(character)) continue;
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

    /**
     * to print the process bar
     */
    void printProcess(){
        double nn = cnt.incrementAndGet() * 1.0/21175;
        int n = (int)(nn*20);
        String infos = "\r|"
                + String.join("", Collections.nCopies(n, "="))
                + String.join("", Collections.nCopies(20-n, " "))
                + "| " + new DecimalFormat("#0.0").format(nn*100)+"% completed";
        System.out.print(infos);
    }

    public static void main(String[] args) throws IOException {

        CompleteSocialGraphMaker socialGraph = new CompleteSocialGraphMaker();

        socialGraph.save();
    }
}
