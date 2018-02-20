
public class Score {
	public int score;
	public String name;
	
	public Score(int score,String name){
		this.score=score;
		this.name=name;
	}
	
	public String toString(){
		return name+" " +String.valueOf(score);
	}
}
