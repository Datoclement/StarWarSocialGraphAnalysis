package socialgraph;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Abstract class for the search in the graph centered by a given character
 */
public abstract class SocialGraph{

    /**
     * Structure to store the result, mapping each integer i to
     * the group of persons that are i-distant to the root
     */
    private final Map<Integer, Set<String> > result;

    public SocialGraph(){
        result = new HashMap<Integer, Set<String> >();
    }

    public Map<Integer,Set<String> > getResult(){return result;}

    /**
     * To search the social graph with sequential method
     * @param root the person from whom the social graph begins
     * @param depth the limit depth of searching
     */
    protected void searchSequential(String root,int depth){
        Queue<String> queue = new LinkedList<String>();
        Set<String> visited = new HashSet<String>();
        Map<String,Integer> dist = new HashMap<String,Integer>();
        queue.add(root);
        visited.add(root);
        dist.put(root,0);
        // System.out.println(visited.contains("Yoda"));
        while(true){
            String cur = queue.poll();
            int curdist = dist.get(cur);
            if(curdist==depth)break;
            if(!result.containsKey(curdist+1)){
                result.put(curdist+1, new HashSet<String>());
            }
            LinkedList<String> nei = findNeighbors(cur);
            for(String n:nei){
                if(visited.contains(n)) continue;
                visited.add(n);
                dist.put(n,curdist+1);
                if(n.indexOf("Legend")<0)
                    result.get(curdist+1).add(n);
                queue.add(n);
            }
        }
    }

    /**
     * To search the social graph with concurrent method
     * @param root the person from whom the social graph begins
     * @param depth the limit depth of searching
     */
    protected void searchParallel(String root,int depth){

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        LinkedBlockingQueue<String> next = new LinkedBlockingQueue<String>();
        Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>());
        int n = Runtime.getRuntime().availableProcessors();
        queue.add(root);
        visited.add(root);

        for(int i=0; i<depth; i++){
            result.put(i+1,Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>()));
            Thread[] ts = new Thread[n];
            final int id = i;
            final LinkedBlockingQueue<String> q = queue;
            final LinkedBlockingQueue<String> nq = next;
            for(int j=0;j<n;j++){
                ts[j] = new Thread(new Runnable(){
                    public void run(){
                        String cur = "";
                        try{
                            while(true){
                                cur = q.poll();
                                if(cur==null){
                                    Thread.sleep(10);
                                    cur = q.poll();
                                    if(cur==null)break;
                                }
                                LinkedList<String> nei = findNeighbors(cur);
                                for(String s : nei){
                                    if(visited.contains(s))continue;
                                    visited.add(s);
                                    nq.put(s);
                                    if(s.indexOf("Legend")<0)
                                        result.get(id+1).add(s);
                                }
                            }
                        }
                        catch(Exception e){e.printStackTrace();}
                    }
                });
                ts[j].start();
            }
            try{
                for(int j=0;j<n;j++){
                    ts[j].join();
                }
            }
            catch(Exception e){e.printStackTrace();}
            queue = next;
            next = new LinkedBlockingQueue<String>();
        }
    }

    /**
     * To find the neighbors of the node cur in the social graph
     * @return a list of neighbors
     */
    protected abstract LinkedList<String> findNeighbors(String cur);

    /**
     * save the result to the given destination
     * @param filename the destination
     */
    public void writeInFile(String filename){
        PrintWriter out = null;
        try{
            out = new PrintWriter(filename);
            for(int i=1;i<=result.size();i++){
                LinkedList<String> cur = new LinkedList<String>(result.get(i));
                Collections.sort(cur);
                for(String s : cur){
                    if(s.indexOf("Legend")<0){
                        out.println(URLDecoder.decode(s,"UTF-8").replace("_"," ")+", "+i);
                    }
                }
            }
        }
        catch(Exception e){e.printStackTrace();}
        out.close();
    }
}
