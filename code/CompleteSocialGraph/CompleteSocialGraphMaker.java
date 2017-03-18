package completesocialgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import util.*;
import characterscraper.*;

/**
 * A class that goes through Wookiepedia
 * to draw a total social graph of Star War
 */
public class CompleteSocialGraphMaker {

    public LinkedBlockingQueue<String> Characters;
    public HashMap<String, HashSet<String>> Neighbors;
    String characterTableFile = "../characterTableFile.txt";

    CompleteSocialGraphMaker() throws IOException{
        //read characters from the file and store them into Hashset Characters

        this.Characters = new LinkedBlockingQueue<String>(); 
        this.Neighbors = new HashMap<String, HashSet<String>>();

        BufferedReader data = new BufferedReader(new FileReader(characterTableFile));
        String Character = data.readLine();
        while(Character != null){
            this.Characters.add(Character);
            Character = data.readLine();
        }
        data.close();
        
        for(String c : this.Characters){
            HashSet<String> n = this.findNeighbors(c);
            this.Neighbors.put(c, n);
            System.out.println(c + " " + n.size());
        }
    }

    public HashSet<String> findNeighbors(String Character){
        HashSet<String> n = new HashSet<String>();
        String pageData = new SourceCode(Character).content;
        int head = pageData.indexOf("<article");
        int tail = pageData.indexOf("</article>", head);
        int cur = head;
        while(true){
            cur = pageData.indexOf("\"/wiki/", cur+1);
            if((cur == -1) || (cur > tail)) return n;
            cur += 7;
            int end = pageData.indexOf('\"', cur+1);
            String url = pageData.substring(cur, end);
            if(this.Characters.contains(url))
                n.add(url);
        }
    }

    void save(){
        try{
            ArrayList<HashSet<String> > n = this.neighbors;
            ArrayList<String> c = this.characters;
            PrintWriter out = new PrintWriter("SocialGraphComplete.txt");
            for(int i=0; i < c.size(); i++){
                    out.print(c.get(i)+"    ");
                    for(String s : n.get(i)){
                        out.print(s + " ");
                    }
                    out.println();
                    out.flush();
            }
            out.close();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void main(String[] args) throws IOException {

        CompleteSocialGraphMaker socialGraph = new CompleteSocialGraphMaker();

        socialGraph.save();
    }
}
