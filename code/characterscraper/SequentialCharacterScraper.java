package characterscraper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import util.SourceCode;

/**
 * A sequential extension of the CharacterScraper
 */
public class SequentialCharacterScraper extends CharacterScraper{

    HashSet<String> visited;

    public SequentialCharacterScraper(){

        this.list = new HashSet<String>(40000);

        this.visited = new HashSet<String>(40000);

        Queue<String> q = new LinkedList<String>();

        q.add(root);

        int count = 0;

        while(!q.isEmpty()){

            String str = q.poll();

            if(this.visited.contains(str))continue;

            this.visited.add(str);

            String pageSourceCode = new SourceCode(str).content;

            this.findSubCategories(pageSourceCode,q);
            this.findCharacters(pageSourceCode,q);

            count++;
            System.out.println(count);
        }
    }


    public static void main(String[] args){

        CharacterScraper cs = new SequentialCharacterScraper();

        cs.save();
    }
}
