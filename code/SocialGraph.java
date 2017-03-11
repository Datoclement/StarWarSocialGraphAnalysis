import java.util.*;
import java.util.concurrent.*;
import java.lang.*;
import java.io.*;

public class SocialGraph{

    final LinkedBlockingQueue<String> neighbors;
    final Map<String, Integer> depths;

    SocialGraph(String root, int depth, CharacterTable ct){

        neighbors = new LinkedBlockingQueue<String>();
        depths = new HashMap<String,Integer>();
        Map<String,LinkedList<String> > socialGraphComplete = ct.characters;

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        LinkedBlockingQueue<String> next = new LinkedBlockingQueue<String>();
        Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>());
        int n = Runtime.getRuntime().availableProcessors();
        queue.add(root);
        for(int i=0;i<=depth;i++){

            Thread[] ts = new Thread[n];
            final int id = i;
            final LinkedBlockingQueue<String> q = queue;
            final LinkedBlockingQueue<String> nq = next;
            for(int j=0;j<n;j++){
                ts[j] = new Thread(new Runnable(){
                    public void run(){
                        String cur = "";
                        try{
                            while((cur = q.poll())!=null){
                                LinkedList<String> nei = socialGraphComplete.get(cur);
                                for(String s : nei){
                                    if(visited.contains(s))continue;
                                    visited.add(s);
                                    nq.put(s);
                                    neighbors.put(s);
                                    depths.put(s,id+1);
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

    public void writeInFile(String filename){
        PrintWriter out = null;
        try{
            out = new PrintWriter(filename);
        }
        catch(Exception e){e.printStackTrace();}
        for(String s : neighbors){
            out.println(s+", "+depths.get(s));
        }
        out.close();
    }
}

// <div class="header-column header-title">
