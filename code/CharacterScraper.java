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

            //debug find keyword
            // if(cs.findCharacters(pageSourceCode)){
            //     System.out.println("http://starwars.wikia.com/wiki/"+str);
            //     return;
            // }

            System.out.println(cs.table.size());
            System.out.println(cs.visited.size());
        }

        cs.save();
    }

    void findSubCatagories(String psc, Queue q){

        int head = psc.indexOf("mw-subcategories");
        if(head == -1)return;
        int temp, tail;
        temp = tail = head+1;
        while(true){
            temp = psc.indexOf("<div",temp+1);
            tail = psc.indexOf("</div>",tail+1);
            if(tail < temp)break;
        }

        int cur = head;
        while(true){
            cur = psc.indexOf("\"/wiki/",cur+1);
            if(cur==-1 || cur>tail)return;
            cur+=7;
            int end = psc.indexOf('\"',cur+1);
            q.add(psc.substring(cur,end));
        }
    }

    boolean findCharacters(String psc){

        int head = psc.indexOf("mw-pages");
        if(head == -1)return false;
        int temp, tail;
        temp = tail = head+1;
        while(true){
            temp = psc.indexOf("<div",temp+1);
            tail = psc.indexOf("</div>",tail+1);
            if(tail < temp)break;
        }
        int cur = head;
        while(true){
            cur = psc.indexOf("\"/wiki/",cur+1);
            if(cur==-1 || cur>tail)return false;
            cur+=7;
            int end = psc.indexOf('\"',cur+1);
            this.table.add(psc.substring(cur,end));

            // debug: find key word
            // if(psc.substring(cur,end).indexOf("Category_Ten")>=0){
            //     System.out.println(psc.substring(cur,end));
            //     return true;
            // }
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
