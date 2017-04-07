package util;

import java.io.*;
import java.net.*;

/**
 * A class to get the source code of Wookiepedia pages
 */
public class SourceCode{

    String prefix = "http://starwars.wikia.com/wiki/";

    /**
     * Where source code are stored
     */
    public String content;

    /**
     * Get the source code from the address prefix+path
     * @param path the file name of destination
     */
    public SourceCode(String path){

        content = "";

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
                content += line;
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
    }
}
