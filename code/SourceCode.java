import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;

public class SourceCode{

    String prefix = "http://starwars.wikia.com/wiki/";

    public String content;

    SourceCode(String path){

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
