public class Main{

    public static void main(String[] args){

        String name = args[0];

        if(name.equals("*")){
            // preprocess benchmark
            CharacterScraper cs;
            long t1 = System.nanoTime();
            cs = new SequentialCharacterScraper();
            long t2 = System.nanoTime();
            cs = new ConcurrentCharacterScraper();
            long t3 = System.nanoTime();
            System.out.println("Sequential preprocess time: "+(t2-t1)/1000000+"ms.");
            System.out.println("Concurrent preprocess time: "+(t3-t2)/1000000+"ms.");
            return;
        }
        if(args.length>3){
            System.out.println("Error: Too many arguments");
            System.out.println("Please change space in name into '_'.");
            return;
        }
        int depth = Integer.parseInt(args[1]);
        String filename = args[2];

        String characterTableFile = "socialGraphComplete.txt";
        CharacterTable ct = new CharacterTable(characterTableFile);
        SocialGraph sg = new SocialGraph(name, depth, ct);
        sg.writeInFile(filename);
    }
}
