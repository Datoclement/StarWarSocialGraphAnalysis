import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;

public class CharacterScraper{

    String prefix = "http://starwars.wikia.com/wiki/";

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

            String pageSourceCode = cs.extract(q.poll());

            
        }

        cs.save();
    }

    String extract(String path){

        String result = "";

        URL url = null;
        URLConnection urlc = null;
        BufferedReader in = null;

        try{
            url = new URL(prefix+path);
            urlc = url.openConnection();
            urlc.connect();
            in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                result += line;
            }
        }
        catch(Exception e){
            System.out.println("Error :"+ e);
            e.printStackTrace();
        }
        finally{
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return result;
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
