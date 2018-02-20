import acm.graphics.GImage;

public class Enemy extends GImage{

	private double HEIGHT = this.getBounds().getHeight();
	private double WIDTH = this.getBounds().getWidth();
	private double speed = 1;
	private int points;
	public int stage = 0;
	public String type;
	public long destroyTime=Long.MAX_VALUE;
	private String airexp_image = "airexplosion.png";
	private String damaged1_image = "damagedtank1.png";
	private String damaged2_image = "damagedtank2.png";
	private String groundexp_image = "groundexplosion.png";
	
	public void destroy(){
		if(this.stage==2){
			this.setImage(groundexp_image);
			this.stage=3;
			this.destroyTime=System.currentTimeMillis();
		}
		if(this.stage==1){
			if((this.type=="flyingenemy")||(this.type=="flyingenemy1")){
				this.setImage(airexp_image);
				this.stage=3;
			}
			else if(this.type=="groundenemy"){
				this.setImage(damaged1_image);
				this.stage=2;
			}
			else if(this.type=="groundenemy1"){
				this.setImage(damaged2_image);
				this.stage=2;
			}
			this.destroyTime=System.currentTimeMillis();
		}
	}
	
	public Enemy(double horizontal,double vertical,String image) {
		super(image, horizontal, vertical);
	}
	
	public void setSpeed(double speed){
		this.speed=speed;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public void setPoints(int points){
		this.points=points;
	}
	
	public int getPoints(){
		return points;
	}
	
	public double getHorX(){
		return this.getX()-20;
	}
	
	public double getHorY(){
		return this.getY()+HEIGHT/2;
	}
	
	public double getVertX(){
		return this.getX()+WIDTH/2;
	}
	
	public double getVertY(){
		return this.getY()-20;
	}
}
