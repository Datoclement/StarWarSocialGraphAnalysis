import java.util.*;
import java.io.*;

public class CharacterTable{

    Map<String, LinkedList<String>> characters;

    CharacterTable(String tableFile){

        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(tableFile));
            String line = "";
            // System.out.println("test");
            while((line = in.readLine()) != null){
                String[] informations = line.split(" ",0);
                for(int i=0;i<informations.length;i++){
                    System.out.println(informations[i]);
                }
            }
            System.out.println("test");
        }
        catch(Exception e){e.printStackTrace();}

    }

    public static void main(String[] args){
        CharacterTable ct = new CharacterTable("socialGraphComplete.txt");
    }
}
