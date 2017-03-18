import java.util.*;
import java.io.*;

public class CompleteSocialGraphReader{

    public Map<String, LinkedList<String>> characters;
    private String graphfile = "../socialGraphComplete.txt";

    public CompleteSocialGraphReader(){

        characters = new HashMap<String, LinkedList<String>>();

        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(graphfile));
            String line = "";
            // System.out.println("test");
            while((line = in.readLine()) != null){
                String[] informations = line.split(" ");
                characters.put(informations[0],new LinkedList<String>());
                for(int i=1;i<informations.length;i++){
                    if(!informations[i].equals("")){
                        characters.get(informations[0]).add(informations[i]);
                    }
                }
            }
        }
        catch(Exception e){e.printStackTrace();}

    }


    // public static void main(String[] args){
    //     CompleteSocialGraphReader csg = new CompleteSocialGraphReader();
    // }
}
