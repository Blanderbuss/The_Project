import acm.graphics.GImage;

public class Plane extends GImage{

	//private String image = "Aircraft.png";
	private double HEIGTH = this.getBounds().getHeight();
	private double WIDTH = this.getBounds().getWidth();
	private double vert_speed = 0;
	private double hor_speed = 0;
	
	public Plane(double horizontal,double vertical,String image) {
		super(image, horizontal, vertical);
	}
	
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
	
	public double getPoint_X(){
		return this.getBounds().getX()+WIDTH;
	}
	
	public double getPoint_Y(){
		return this.getBounds().getY()+HEIGTH/2;
	}
	
	public double getPointB_X(){
		return this.getBounds().getX()+WIDTH/2;
	}
	
	public double getPointB_Y(){
		return this.getBounds().getY()+HEIGTH;
	}

}
