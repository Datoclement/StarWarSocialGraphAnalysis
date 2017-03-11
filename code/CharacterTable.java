import java.util.*;
import java.io.*;

public class CharacterTable{

    public Map<String, LinkedList<String>> characters;

    CharacterTable(String tableFile){

        characters = new HashMap<String, LinkedList<String>>();

        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(tableFile));
            String line = "";
            // System.out.println("test");
            while((line = in.readLine()) != null){
                String[] informations = line.split(" ");
                characters.put(informations[0],new LinkedList<String>());
                if(informations[0]=="Yoda"){
                    System.out.println("get yoda");
                    for(int i=0;i<informations.length;i++){
                        System.out.println(informations);
                    }
                }
                for(int i=1;i<informations.length;i++){
                    if(informations[i]!=""){
                        characters.get(informations[0]).add(informations[i]);
                    }
                }
            }
        }
        catch(Exception e){e.printStackTrace();}

    }


    public static void main(String[] args){
        CharacterTable ct = new CharacterTable("socialGraphComplete.txt");
    }
}
