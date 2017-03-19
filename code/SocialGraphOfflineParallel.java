import java.util.*;
import java.util.concurrent.*;
import java.lang.*;
import java.io.*;
import java.net.*;

/**
 * A class to get the social graph centred by some given character
 * Parallel and offline version
 */
public class SocialGraphOfflineParallel{

    /**
     * Structure to store the result, mapping each integer i to
     * the group of persons that are i-distant to the root
     */
    final Map<Integer, Set<String> > depths;

    /**
     * @param root the person from whom the social graph begins
     * @param depth the limit depth of searching
     * @param csg the local complete social graph
     */
    SocialGraphOfflineParallel(String root, int depth, Map<String, LinkedList<String>> csg){

        depths = new HashMap<Integer, Set<String> >();
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        LinkedBlockingQueue<String> next = new LinkedBlockingQueue<String>();
        Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>());
        int n = Runtime.getRuntime().availableProcessors();
        queue.add(root);
        visited.add(root);
        for(int i=0;i<depth;i++){
            depths.put(i+1,Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>()));
            Thread[] ts = new Thread[n];
            final int id = i;
            final LinkedBlockingQueue<String> q = queue;
            final LinkedBlockingQueue<String> nq = next;
            for(int j=0;j<n;j++){
                ts[j] = new Thread(new Runnable(){
                    public void run(){
                        String cur = null;
                        try{
                            while(true){
                                cur = q.poll();
                                if(cur==null){
                                    Thread.sleep(300);
                                    cur = q.poll();
                                    if(cur==null)break;
                                }
                                LinkedList<String> nei = csg.get(cur);
                                for(String s : nei){
                                    if(visited.contains(s))continue;
                                    visited.add(s);
                                    nq.put(s);
                                    depths.get(id+1).add(s);
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
     * save the result to the given destination
     * @param filename the destination
     */
    public void writeInFile(String filename){
        PrintWriter out = null;
        try{
            out = new PrintWriter(filename);
        }
        catch(Exception e){e.printStackTrace();}
        for(int i=1;i<=depths.size();i++){
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
