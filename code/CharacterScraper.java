import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;

public class CharacterScraper{

    HashSet<String> table;

    HashSet<String> visited;

    String characterTableFile = "characterTableFile.txt";

    public CharacterScraper(){

        this.table = new HashSet<String>(40000);

        this.visited = new HashSet<String>(40000);

    }

    public static void main(String[] args){

        CharacterScraper cs = new CharacterScraper();

        Queue<String> q = new LinkedList<String>();

        q.add("Category:Individuals");

        while(!q.isEmpty()){

            String str = q.poll();

            if(cs.visited.contains(str))continue;

            cs.visited.add(str);

            String pageSourceCode = new SourceCode(str).content;

            // System.out.println(pageSourceCode);

            cs.findSubCatagories(pageSourceCode,q);

            cs.findCharacters(pageSourceCode);

            System.out.println(cs.table.size());
            System.out.println(cs.visited.size());
        }

        cs.save();
    }

    void findSubCatagories(String psc, Queue q){

        int head = psc.indexOf("mw-subcategories");
        if(head == -1)return;
        int tail = psc.indexOf("\t</div>",head+1);
        int cur = head;
        // System.out.println(psc.substring);
        while(true){
            cur = psc.indexOf("\"/wiki/",cur+1);
            if(cur==-1 || cur>tail)return;
            cur+=7;
            int end = psc.indexOf('\"',cur+1);
            q.add(psc.substring(cur,end));
        }
    }

    void findCharacters(String psc){

        int a = psc.indexOf("mw-pages");
        if(a == -1)return;
        int head = psc.indexOf("div",a);
        int tail = psc.indexOf("\t<div>",head+1);
        int cur = head;
        while(true){
            cur = psc.indexOf("\"/wiki/",cur+1);
            if(cur==-1 || cur>tail)return;
            cur+=7;
            int end = psc.indexOf('\"',cur+1);
            this.table.add(psc.substring(cur,end));
        }
    }

    void save(){

        Path file = Paths.get(characterTableFile);
        try{
            Files.write(file, table, Charset.forName("UTF-8"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
