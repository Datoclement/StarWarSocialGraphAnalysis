import java.io.*;
import java.util.*;

import characterscraper.*;
import completesocialgraph.*;
import socialgraph.*;

public class Main{

    public static void main(String[] args) throws IOException{

        String flag = args[0];
        if(flag.equals("scraper")) scraper_benchmark();
        else if(flag.equals("completegraph")) completegraph_benchmark();
        else if(flag.equals("online"))
            if(args.length > 4) changeToSpaceWarning();
            else online_benchmark(args);
        else if(args.length > 3) changeToSpaceWarning();
        else offline_benchmark(args);
        usage_message();
    }

    static void scraper_benchmark(){
        System.out.println("Running a test for different scraping methods...");
        System.out.println("It takes about 4 minutes in total...");
        CharacterScraper css,csc;
        long t1 = System.nanoTime();
        System.out.println("Running a test of scraper using sequential method...");
        css = new SequentialCharacterScraper();
        long t2 = System.nanoTime();
        System.out.println("Running a test of scraper using concurrent method...");
        csc = new ConcurrentCharacterScraper();
        long t3 = System.nanoTime();
        System.out.println("Sequential preprocess time: "+(t2-t1)/1000000+"ms.");
        System.out.println(css.getList().size()+" characters are found.");
        System.out.println("Concurrent preprocess time: "+(t3-t2)/1000000+"ms.");
        System.out.println(csc.getList().size()+" characters are found.");
    }

    static void completegraph_benchmark(){}

    static void offline_benchmark(String[] args){
        String name = args[0];
        int depth = Integer.parseInt(args[1]);
        String filename = args[2];
        System.out.println("Testing offline method...");
        long t1 = System.nanoTime();
        System.out.println("Using offline sequential method...");
        SocialGraph sgfs = new SocialGraphOfflineSequential(
                name, depth, new CompleteSocialGraphReader().getGraph());
        System.out.println("Offline sequential method finished.");
        long t2 = System.nanoTime();
        System.out.println("Using offline concurrent method...");
        SocialGraph sgfp = new SocialGraphOfflineParallel(
                name, depth, new CompleteSocialGraphReader().getGraph());
        System.out.println("Offline concurrent method finished.");
        long t3 = System.nanoTime();

        System.out.println();
        String str;
        System.out.println("Result size: (depth,number of characters in this depth)");
        Map<Integer,Set<String> > rfs = sgfs.getResult();
        str = "Sequential: ";
        for(int i=1;i<=depth;i++){
            str += "("+ i + ","+rfs.get(i).size()+")";
            if(i<depth) str += ",";
        }
        System.out.println(str);
        Map<Integer,Set<String> > rfp = sgfp.getResult();
        str = "Concurrent: ";
        for(int i=1;i<=depth;i++){
            str += "("+ i + ","+rfp.get(i).size()+")";
            if(i<depth) str += ",";
        }
        System.out.println(str);

        System.out.println();
        System.out.println("Sequential method used time: "+(t2-t1)/1000000+"ms.");
        System.out.println("Concurrent method used time: "+(t3-t2)/1000000+"ms.");
        sgfs.writeInFile(filename);
    }

    static void online_benchmark(String[] args){
        String name = args[1];
        int depth = Integer.parseInt(args[2]);
        String filename = args[3];
        System.out.println("Testing online method...");
        long t1 = System.nanoTime();
        System.out.println("Using online sequential method...");
        SocialGraph sgns = new SocialGraphOnlineSequential(
                name, depth, new CharacterTableReader().getList());
        System.out.println("Online sequentail method finished.");
        long t2 = System.nanoTime();
        System.out.println("Using online concurrent method...");
        SocialGraph sgnp = new SocialGraphOnlineParallel(
                name, depth, new CharacterTableReader().getList());
        System.out.println("Online concurrent method finished.");
        long t3 = System.nanoTime();

        System.out.println();
        String str;
        System.out.println("Result size: (depth,number of characters in this depth)");
        Map<Integer,Set<String> > rns = sgns.getResult();
        str = "Sequential: ";
        for(int i=1;i<=depth;i++){
            str += "("+ i + ","+rns.get(i).size()+")";
            if(i<depth) str += ",";
        }
        System.out.println(str);
        Map<Integer,Set<String> > rnp = sgnp.getResult();
        str = "Concurrent: ";
        for(int i=1;i<=depth;i++){
            str += "("+ i + ","+rnp.get(i).size()+")";
            if(i<depth) str += ",";
        }
        System.out.println(str);

        System.out.println();
        System.out.println("Sequential method used time: "+(t2-t1)/1000000+"ms.");
        System.out.println("Concurrent method used time: "+(t3-t2)/1000000+"ms.");
        sgns.writeInFile(filename);
    }

    static void changeToSpaceWarning(){
        System.out.println("Error: Too many arguments");
        System.out.println("Please change space ' ' in name into '_'.");
    }

    static void usage_message(){
        System.out.println();
        System.out.println("usage:");
        System.out.println("0: run \"java Main CHARACTER_NAME DEPTH FILENAME\" can see a test on the offline method.");
        System.out.println("1: run \"java Main scraper\" can see a test on the character scraper.");
        System.out.println("2: run \"java Main completegraph\" can see a test on the graph scraper.");
        System.out.println("3: run \"java Main online CHARACTER_NAME DEPTH FILENAME\" can see a test on the online method.");
    }
}
