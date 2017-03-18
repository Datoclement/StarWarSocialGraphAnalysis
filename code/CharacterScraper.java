import java.util.*;

/**
 * a scraper that go through Wookiepedia to find all the characters in the Star War
 */

public abstract class CharacterScraper{

    Set<String> table;

    /**
     * save the table content in a file
     */
    abstract void save();
}
