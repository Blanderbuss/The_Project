import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Highscores extends ArrayList<Score>{
	
	private static String highscore_file="highscores.txt";
	File file = new File(highscore_file);
	
	public Highscores(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			for(int i=0;i<10;i++){
					String s=reader.readLine();
					if(s==null)break;
					String newname = s.substring(0, s.indexOf(" "));
					int newScore = Integer.valueOf(s.substring(s.indexOf(' ')+1));
					Score newscore = new Score(newScore,newname);
					this.add(newscore);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addScore(String name,int score){
		Score newscore = new Score(score,name);
		name=name.replaceAll(" ", "");
		for(int i=0;i<10;i++){
			if(i>=size()){
				add(newscore);
				break;
			}
			if(get(i).score<score){
				add(i,newscore);
				break;
			}
		}
		try {
			File temp = new File("temp.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
			for(int i=0;i<size();i++){
				writer.write(get(i).toString()+"\n");
			}
			writer.close();
			file.delete();
			temp.renameTo(file);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString(){
		String s="";
		for(int i=0;i<size();i++){
			s=s+String.valueOf(i+1)+")"+get(i).toString()+"\n";
		}
		return s;
	}

}
