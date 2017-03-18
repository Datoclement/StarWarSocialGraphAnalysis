package completesocialgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;


public class CompleteSocialGraphMaker {

    HashSet<String> Characters;
    ArrayList<String> characters;//ordered
    ArrayList<HashSet<String>> Neighbors;
    int number;
    String characterTableFile = "../characterTableFile.txt";

    CompleteSocialGraphMaker() throws IOException{
        //read characters from the file and store them into Hashset Characters
        BufferedReader data = new BufferedReader(new FileReader(characterTableFile));
        String Character = data.readLine();
        this.Characters = new HashSet<String>();
        while(Character != null){
            this.Characters.add(Character);
            Character = data.readLine();
        }
        if(data != null) data.close();

        this.number = this.Characters.size();

        this.characters = new ArrayList<String>();
        this.Neighbors = new ArrayList<HashSet<String>>();

        for(String c : this.Characters){
            this.characters.add(c);
            HashSet<String> n = this.findNeighbors(c);
            this.Neighbors.add(n);
            System.out.println(c + " " + n.size());
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

    public static void main(String[] args) throws IOException {

        CompleteSocialGraphMaker socialGraph = new CompleteSocialGraphMaker();

        ArrayList<HashSet<String>> n = socialGraph.Neighbors;
        ArrayList<String> c = socialGraph.characters;
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
}
