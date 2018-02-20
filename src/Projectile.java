import acm.graphics.GImage;

public class Projectile extends GImage{

	public Projectile(double horizontal,double vertical,String image) {
		super(image, horizontal, vertical);
	}
	private double vert_speed = 0;
	private double hor_speed = 0;
	public boolean bomb=false;
	public long destroyTime=Long.MAX_VALUE;
	
	public void setVertSpeed(double vert_speed){
		this.vert_speed = vert_speed;
	}
	
	public double getVertSpeed(){
		return vert_speed;
	}
	
	public void setHorSpeed(double hor_speed){
		this.hor_speed = hor_speed;
	}
	
	public double getHorSpeed(){
		return hor_speed;
	}
	
}
