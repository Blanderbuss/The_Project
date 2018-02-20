import acm.graphics.GImage;

public class PowerUp extends GImage{
	
	private String type;
	private double speed=-2;

	public PowerUp(double horizontal,double vertical,String image) {
		super(image, horizontal, vertical);
	}
	
	public void setType(String type){
		this.type=type;
	}
	
	public String getType(){
		return type;
	}
	
	public void setSpeed(double speed){
		this.speed=speed;
	}
	
	public double getSpeed(){
		return speed;
	}

}
