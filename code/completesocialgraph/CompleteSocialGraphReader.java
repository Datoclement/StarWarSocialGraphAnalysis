package completesocialgraph;

import java.util.*;
import java.io.*;

/**
 * A class to get the complete social graph from the local file
 */
public class CompleteSocialGraphReader{

    /**
     * Structure to store the social graph
     */
    private Map<String, LinkedList<String> > graph;

    /**
     * Path to the local file
     */
    private String graphfile = "completesocialgraph/socialGraphComplete.txt";

    public CompleteSocialGraphReader(){

        graph = new HashMap<String, LinkedList<String>>();

        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(graphfile));
            String line = "";
            // System.out.println("test");
            while((line = in.readLine()) != null){
                String[] informations = line.split(" ");
                graph.put(informations[0],new LinkedList<String>());
                for(int i=1;i<informations.length;i++){
                    if(!informations[i].equals("")){
                        graph.get(informations[0]).add(informations[i]);
                    }
                }
            }
        }
        catch(Exception e){e.printStackTrace();}

    }

    public Map<String, LinkedList<String> > getGraph(){return this.graph;}
}
