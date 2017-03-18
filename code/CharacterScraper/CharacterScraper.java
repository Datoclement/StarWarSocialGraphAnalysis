package characterscraper;

import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;

/**
 * a scraper that go through Wookiepedia to find all the characters in the Star War
 */
public abstract class CharacterScraper{

    /**
     * Structure to store the found characters
     */
    protected Set<String> list;

    /**
     * Path to store the result
     */
    String characterTableFile = "../characterTableFile.txt";

    /**
     * find all the subcategories indicated in the source code
     * and add them to q
     * @param  psc a string that contains the source code of a webpage
     * @param    q a queue that stores the subcategories that are to visit later
     */
    void findSubCategories(String psc, Queue<String> q){

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

    /**
     * find all the characters indicated in the source code
     * and add them to list
     * @param  psc a string that contains the source code of a webpage
     */
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
            this.list.add(psc.substring(cur,end));
        }
    }

    /**
     * save the character list in the file indicated by characterTableFile
     */
    void save(){

        Path file = Paths.get(characterTableFile);
        try{
            Files.write(file, list, Charset.forName("UTF-8"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Set<String> getList(){return this.list;}
}
