import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class SocialGraphCompleteTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//final String characterTableFile = "C:\\Users\\yayundai\\Desktop\\StarWarSocialGraphAnalysis\\code\\characterTableFile.txt";
		SocialGraphComplete socialGraph = new SocialGraphComplete("characterTableFile.txt");
		ArrayList<HashSet<String>> n = socialGraph.Neighbors;
    ArrayList<String> c = socialGraph.characters;
    PrintWriter out = new PrintWriter("SocialGraphComplete.txt");
    for(int i=0; i < c.size(); i++){
        	out.print(c.get(i)+"    ");
        	for(String s : n.get(i)){
        		out.print(s + " ");
        	}
        	out.println();
        	out.flush();
    }
    out.close();
	}

}
