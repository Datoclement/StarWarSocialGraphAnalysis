import java.io.*;
import characterscraper.*;
import completesocialgraph.*;

public class Main{

    public static void main(String[] args) throws IOException{

        String name = args[0];

        if(name.equals("preprocess")){
            // preprocess benchmark
            System.out.println("Running a test for different preprocess methods...");
            System.out.println("It takes about 4 minutes in total...");
            CharacterScraper css,csc;
            long t1 = System.nanoTime();
            System.out.println("Running a test of preprocess using sequential method...");
            css = new SequentialCharacterScraper();
            long t2 = System.nanoTime();
            System.out.println("Running a test of preprocess using concurrent method...");
            csc = new ConcurrentCharacterScraper();
            long t3 = System.nanoTime();
            System.out.println("Sequential preprocess time: "+(t2-t1)/1000000+"ms.");
            System.out.println(css.getList().size()+" characters are found.");
            System.out.println("Concurrent preprocess time: "+(t3-t2)/1000000+"ms.");
            System.out.println(csc.getList().size()+" characters are found.");
            return;
        }
        if(args.length>3){
            //Input error
            System.out.println("Error: Too many arguments");
            System.out.println("Please change space ' ' in name into '_'.");
            return;
        }

        int depth = Integer.parseInt(args[1]);
        String filename = args[2];


        CompleteSocialGraphReader csgr = new CompleteSocialGraphReader();
        SocialGraphOfflineParallel sg = new SocialGraphOfflineParallel(
                name, depth, csgr.getGraph());

        SocialGraphOnlineParallel swsg = new SocialGraphOnlineParallel(
                name, depth, new CharacterTableReader().getList());
        swsg.writeInFile(filename);

        System.out.println("Tips: run \"java Main preprocess\" can see a test on the preprocess");
    }
}
