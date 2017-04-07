package characterscraper;

import java.util.*;
import java.text.*;
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
    protected String characterTableFile = "characterscraper/characterTableFile.txt";

    protected String root = "Category:Individuals_by_occupation";

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
    void findCharacters(String psc, Queue<String> q){

        int head = psc.indexOf("mw-pages");
        if(head == -1)return;
        int temp, tail;
        temp = tail = head+1;
        while(true){
            temp = psc.indexOf("<div",temp+1);
            tail = psc.indexOf("</div>",tail+1);
            if(tail < temp)break;
        }
        int sp = psc.indexOf("<h3> </h3>",head+1);
        if(sp>=0 && sp < tail) head = psc.indexOf("<h3>",sp+1);
        sp = psc.indexOf("wikia-paginator",head+1);
        if(sp>=0 && sp < tail) {temp = tail; tail = sp;}
        int cur = head;
        while(true){
            cur = psc.indexOf("\"/wiki/",cur+1);
            if(cur==-1 || cur>tail)break;
            cur+=7;
            int end = psc.indexOf('\"',cur+1);
            String cha = psc.substring(cur,end);
            if(cha.indexOf("Unidentified")>=0)continue;
            this.list.add(cha);
        }
        head = tail;
        tail = temp;
        cur = head;
        while(true){
            cur = psc.indexOf("\"/wiki/",cur+1);
            if(cur==-1 || cur>tail)return;
            cur+=7;
            int end = psc.indexOf('\"',cur+1);
            q.add(psc.substring(cur,end));
        }
    }

    /**
     * save the character list in the file indicated by characterTableFile
     */
    public void save(){

        Path file = Paths.get(characterTableFile);
        try{
            Files.write(file, list, Charset.forName("UTF-8"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * to print the process bar
     */
    protected void printProcess(){
        double nn = this.list.size() * 1.0/21175;
        int n = (int)(nn*20);
        String infos = "\r|"
                + String.join("", Collections.nCopies(n, "="))
                + String.join("", Collections.nCopies(20-n, " "))
                + "| " + new DecimalFormat("#0.0").format(nn*100)+"% completed";
        System.out.print(infos);
    }

    public Set<String> getList(){return this.list;}
}
