import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;

/**
 * A sequential extension of the CharacterScraper
 */
public class SequentialCharacterScraper extends CharacterScraper{

    HashSet<String> visited;

    public SequentialCharacterScraper(){

        this.list = new HashSet<String>(40000);

        this.visited = new HashSet<String>(40000);

        Queue<String> q = new LinkedList<String>();

        q.add("Category:Individuals");

        while(!q.isEmpty()){

            String str = q.poll();

            if(this.visited.contains(str))continue;

            this.visited.add(str);

            String pageSourceCode = new SourceCode(str).content;

            this.findSubCatagories(pageSourceCode,q);
            this.findCharacters(pageSourceCode);
        }
    }


    public static void main(String[] args){

        CharacterScraper cs = new SequentialCharacterScraper();

        cs.save();
    }
}
