import java.util.*;
import java.util.concurrent.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class SocialGraph{

    final Map<Integer, LinkedBlockingQueue<String> > depths;

    SocialGraph(String root, int depth, CharacterTable ct){

        depths = new HashMap<Integer, LinkedBlockingQueue<String> >();
        Map<String,LinkedList<String> > socialGraphComplete = ct.characters;

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        LinkedBlockingQueue<String> next = new LinkedBlockingQueue<String>();
        Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>());
        int n = Runtime.getRuntime().availableProcessors();
        queue.add(root);
        visited.add(root);
        for(int i=0;i<depth;i++){
            depths.put(i+1,new LinkedBlockingQueue<String>());
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
                                    depths.get(id+1).put(s);
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
        for(int i=1;i<depths.size();i++){
            LinkedList<String> cur = new LinkedList<String>(depths.get(i));
            Collections.sort(cur);
            for(String s : cur)
                try{
                    out.println(URLDecoder.decode(s,"UTF-8").replace("_"," ")+", "+i);
                }
                catch(Exception e) {e.printStackTrace();}
        }
        out.close();
    }
}

// <div class="header-column header-title">
