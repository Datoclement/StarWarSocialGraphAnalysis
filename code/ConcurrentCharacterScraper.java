import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.lang.*;
import java.nio.file.*;
import java.nio.charset.Charset;

public class ConcurrentCharacterScraper extends CharacterScraper{

    final Set<String> visited;

    String characterTableFile = "test.txt";

    public ConcurrentCharacterScraper(){

        this.table = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>(40000));

        this.visited = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>(40000));

        Queue<String> q = new LinkedBlockingQueue<String>();

        q.add("Category:Individuals");

        int n = Runtime.getRuntime().availableProcessors();
        System.out.println("Processers:"+n);

        AtomicInteger waitingThreads = new AtomicInteger(0);

        Thread[] ts = new Thread[n];

        for(int i=0;i<n;i++){
            System.out.println("Processer "+i+" is created.");
            ts[i] = new Thread(new Runnable(){
                public void run(){
                    String str = "";
                    while(true){

                        str = q.poll();
                        if(str==null){
                            try{
                                Thread.sleep(300);
                            }
                            catch(Exception e){e.printStackTrace();}
                            str = q.poll();
                            if(str==null)break;
                        }

                        if(visited.contains(str))continue;

                        visited.add(str);

                        String pageSourceCode = new SourceCode(str).content;

                        // System.out.println(pageSourceCode);
                        ConcurrentCharacterScraper.this.findSubCatagories(
                                pageSourceCode,q);
                        ConcurrentCharacterScraper.this.findCharacters(
                                pageSourceCode);

                        //debug find keyword
                        // if(cs.findCharacters(pageSourceCode)){
                        //     System.out.println("http://starwars.wikia.com/wiki/"+str);
                        //     return;
                        // }
                        System.out.println(Thread.currentThread().getName());
                        System.out.println(
                                ConcurrentCharacterScraper.this.table.size());
                        System.out.println(
                                ConcurrentCharacterScraper.this.visited.size());
                    }
                }
            });
            ts[i].setName("Thread-"+i);
            ts[i].start();
        }
        try{
            for(int i=0;i<n;i++)
                ts[i].join();
        }
        catch(Exception e){e.printStackTrace();}
    }


    public static void main(String[] args){

        CharacterScraper cs = new ConcurrentCharacterScraper();

        cs.save();
    }

    void findSubCatagories(String psc, Queue<String> q){

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
