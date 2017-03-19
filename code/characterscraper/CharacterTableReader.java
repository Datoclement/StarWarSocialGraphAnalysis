package characterscraper;

import java.io.*;
import java.util.*;
import java.lang.*;

public class CharacterTableReader{

    private List<String> list;
    String characterTableFile = "characterscraper/characterTableFile.txt";

    public CharacterTableReader(){
        this.list = new LinkedList<String>();
        BufferedReader data = null;
        try{
            data = new BufferedReader(new FileReader(characterTableFile));
            String line = data.readLine();
            while(line != null){
                this.list.add(line);
                line = data.readLine();
            }
            data.close();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public List<String> getList(){return this.list;}
}
