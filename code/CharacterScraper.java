import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;

public class CharacterScraper{

    ArrayList<String> table;

    String characterTableFile = "characterTableFile.txt";

    CharacterScraper(){
        table = new ArrayList<String>();
    }

    public static void main(String[] args){

        CharacterScraper cs = new CharacterScraper();

        Queue<String> q = new LinkedList<String>();

        q.add("Category:Individuals");

        while(!q.isEmpty()){

            String pageSourceCode = new SourceCode(q.poll()).content;
            System.out.println(pageSourceCode);
            //
            // findSubCatagories(pageSourceCode,q);
            //
            // findCharacters(pageSourceCode);
        }

        cs.save();
    }

    // void findSubCatagories(String psc, Queue q){
    //
    //     int a = psc.indexOf("mw-subcatagories");
    //     if(a == -1)return;
    //     int head = psc.indexOf("div",a);
    //     int tail = psc.indexOf("div",head);
    //     int cur = head;
    //     while(true){
    //         cur = psc.indexOf("\"/wiki/",cur+1);
    //         if(cur==-1 || cur>tail)return;
    //         cur+=7;
    //         end = psc.indexOf('\"',cur+1);
    //         table.add();
    //     }
    // }

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
