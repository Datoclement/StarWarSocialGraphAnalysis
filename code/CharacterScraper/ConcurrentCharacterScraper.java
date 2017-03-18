package characterscraper;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.lang.*;
import java.nio.file.*;
import java.nio.charset.Charset;
import util.*;

/**
 * A concurrent extension of the CharacterScraper
 */
public class ConcurrentCharacterScraper extends CharacterScraper{

    final Set<String> visited;

    public ConcurrentCharacterScraper(){

        this.list = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>(40000));

        this.visited = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>(40000));

        Queue<String> q = new LinkedBlockingQueue<String>();

        q.add("Category:Individuals");

        int n = Runtime.getRuntime().availableProcessors();
        System.out.println("Processers: "+n);

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

                        ConcurrentCharacterScraper.this.findSubCatagories(
                                pageSourceCode,q);
                        ConcurrentCharacterScraper.this.findCharacters(
                                pageSourceCode);
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
}
