import java.util.*;
import java.util.concurrent.*;
import java.lang.*;
import java.io.*;

public class StarWarSocialGraph{
	final Map<Integer, LinkedBlockingQueue<String> > result;
	final HashSet<String> Characters;

	StarWarSocialGraph(String root, int depth, String characterTableFile) throws IOException{
		this.result = new HashMap<Integer, LinkedBlockingQueue<String> >();

		this.Characters = new HashSet<String>();
		BufferedReader data = new BufferedReader(new FileReader(characterTableFile));
    	String Character = data.readLine();
    	while(Character != null){
    		this.Characters.add(Character);
    		Character = data.readLine();
    	}
    	if(data != null) data.close();

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        LinkedBlockingQueue<String> next = new LinkedBlockingQueue<String>();
        Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>());
        int n = Runtime.getRuntime().availableProcessors();
        queue.add(root);
        visited.add(root);

        for(int i=0; i<depth; i++){
            result.put(i+1, new LinkedBlockingQueue<String>());
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
                                LinkedList<String> nei = new LinkedList<String>(findNeighbors(cur));
                                for(String s : nei){
                                    if(visited.contains(s))continue;
                                    visited.add(s);
                                    nq.put(s);
                                    result.get(id+1).put(s);
                                    System.out.println(s + ", " + id);
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

	public HashSet<String> findNeighbors(String Character){
    	HashSet<String> n = new HashSet<String>();
    	String PageData = new SourceCode(Character).content;
    	int head = PageData.indexOf("<article");
    	int tail = PageData.indexOf("</article>", head);
    	int cur = head;
    	while(true){
    		cur = PageData.indexOf("\"/wiki/", cur+1);
    		if((cur == -1) || (cur > tail)) return n;
    		cur += 7;
    		int end = PageData.indexOf('\"', cur+1);
    		String url = PageData.substring(cur, end);
    		if(this.Characters.contains(url))
    			n.add(url);
    	}
	}

    public void writeInFile(String filename){
        PrintWriter out = null;
        try{
            out = new PrintWriter(filename);
        }
        catch(Exception e){e.printStackTrace();}
        for(int i=1;i<=result.size();i++){
            LinkedList<String> cur = new LinkedList<String>(result.get(i));
            Collections.sort(cur);
            for(String s : cur)
                out.println(s+", "+i);
        }
        out.close();
    }
}