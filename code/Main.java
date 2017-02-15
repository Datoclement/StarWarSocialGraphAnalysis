public class Main{

    public static void main(String[] args){

        String name = args[0];
        int depth = Integer.parseInt(args[1]);
        String filename = args[2];

        String characterTableFile;
        CharacterTable ct = new CharacterTable(characterTableFile);

        SocialGraph sg = new SocialGraph(name, depth, ct);

        sg.writeInFile(filename);
    }
}
