import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.io.IODialog;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class Main1 extends GraphicsProgram{
	
	private static int APPLICATION_WIDTH = 800;
	private static int APPLICATION_HEIGHT = 450;
	private static int GROUND_HEIGHT = 35;
	private static int BUTTON_HEIGHT = 25;
	private static int BUTTON_WIDTH = 80;
	private int lives = 3;
	private int proj_speed = 5;
	private int bomb_speed = 3;
	private int score=0;
	private int[] stages={10,25,50,100,250,500};
	private int stage=0;
	private int multi=1;
	private boolean doubleshot = false;
	private long lastshot=0;
	private long lastbomb=0;
	private boolean start=false;
	private boolean over=false;
	private static String ins ="Arrow keys to move;\nSpace to shoot;\nShift to throw bombs;\nR to restart game.";
	private static String about ="The project made by:\nOlexiy Lebedev,\nFedorak Bohdan.";
	
	private String plane_image = "Aircraft.png";
	private String groundenemy_image = "groundenemy1.png";
	private String groundenemy1_image = "groundenemy2.png";
	private String flyingenemy_image = "flyingenemy.png";
	private String flyingenemy1_image = "flyingenemy1.png";
	private String proj_image = "proj1.png";
	private String background_image = "background1.png";
	private String ground_image = "ground.png";
	private String bomb_image = "bombita.png";
	private String vertproj_image = "vertproj.png";
	private String horproj_image = "horproj.png";
	private String bombhit_image = "groundhit.png";
	private String PUscore_image = "PUscore.png";
	private String PUlife_image = "PUlife.png";
	private String PUshot_image = "PUshot.png";
	private String PUdestroy_image = "PUdestroy.png";
	private String GUIbackground_image = "gui_background.png";
	
	
	RandomGenerator rand = new RandomGenerator();

	private GLabel gameOver = new GLabel("GAME OVER",250,250);
	private GLabel scoreLabel = new GLabel("Score:0",APPLICATION_WIDTH-64,10);
	private GLabel livesLabel = new GLabel("Lives:"+String.valueOf(lives),APPLICATION_WIDTH-64,22);
	private Plane plane = new Plane(0, 20, plane_image);
	private GImage background = new GImage(background_image);
	private GImage GUIbackground = new GImage(GUIbackground_image);
	private GImage ground = new GImage(ground_image,0,APPLICATION_HEIGHT-GROUND_HEIGHT);
	private ArrayList<Projectile> projar = new ArrayList<Projectile>();
	private ArrayList<Enemy> enemyar = new ArrayList<Enemy>();
	private ArrayList<Projectile> enemyprojar = new ArrayList<Projectile>();
	private ArrayList<PowerUp> powerupar = new ArrayList<PowerUp>();
	private GRect highscoreRect = new GRect(30,BUTTON_HEIGHT,BUTTON_WIDTH,BUTTON_HEIGHT);
	private GRect instructions = new GRect(30,BUTTON_HEIGHT*2,BUTTON_WIDTH,BUTTON_HEIGHT);
	private GRect aboutRect = new GRect(30,BUTTON_HEIGHT*3,BUTTON_WIDTH,BUTTON_HEIGHT);
	private GRect exitRect = new GRect(30,BUTTON_HEIGHT*4,BUTTON_WIDTH,BUTTON_HEIGHT);
	private GRect startgame = new GRect(30,0,BUTTON_WIDTH,BUTTON_HEIGHT);
	private GLabel startLabel = new GLabel("New game",40,15);
	private GLabel highscoresLabel = new GLabel("Highscores",40,BUTTON_HEIGHT+15);
	private GLabel instructionsLabel = new GLabel("Instructions",40,BUTTON_HEIGHT*2+15);
	private GLabel aboutLabel = new GLabel("About",40,BUTTON_HEIGHT*3+15);
	private GLabel exitLabel = new GLabel("Exit game",40,BUTTON_HEIGHT*4+15);
	

	public static void main(String[] args) {
		new Main1().start();
	}
	
	public void run(){
		makeGUI();
		while(true){
		if(start)rungame();
		pause(30);
		}
	}
	
	private void rerun(){
		this.setSize(160,200);
		over=false;
		add(GUIbackground);
		add(startgame);
		add(highscoreRect);
		add(exitRect);
		add(aboutRect);
		add(instructions);
		addLabels();
		while(true){
		if(start)rungame();
		pause(30);
		}
	}
	
	private void makeGUI(){
		this.setSize(160,200);
		add(GUIbackground);
		highscoreRect.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				Highscores highscores = new Highscores();
				IODialog dial = new IODialog();
				dial.println(highscores);
			}
		});
		add(highscoreRect);
		instructions.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				IODialog dial = new IODialog();
				dial.println(ins);
			}
		});
		add(instructions);
		aboutRect.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				IODialog dial = new IODialog();
				dial.println(about);
			}
		});
		add(aboutRect);
		exitRect.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				System.exit(0);
			}
		});
		add(exitRect);
		startgame.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				remove(startgame);
				remove(highscoreRect);
				remove(exitRect);
				remove(aboutRect);
				remove(instructions);
				removeLabels();
				start=true;
			}
		});
		add(startgame);
		addLabels();
	}
	
	private void rungame(){
	    addKeyListeners();
		this.setSize(APPLICATION_WIDTH+15, APPLICATION_HEIGHT+60);
		background.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		add(background);
		ground.setSize(APPLICATION_WIDTH,GROUND_HEIGHT);
		add(ground);
		scoreLabel.setVisible(true);
		add(scoreLabel);
		add(livesLabel);
		add(plane);
		long startTime=System.currentTimeMillis();
		while(true){
			if(!towardsBounds())
				plane.move(plane.getHorSpeed(), plane.getVertSpeed());
			moveEnemies();
			int air = rand.nextInt(70);
			int ground = rand.nextInt(70);
			if(stage>=1)if(air==0)shootAir();
			if(stage>=2)if(ground==0)shootGround();
			moveProjs();
			movePowerUps();
			doCollisions(enemyar,projar);
			if(System.currentTimeMillis()-startTime>=spawnRate()){
				startTime=System.currentTimeMillis();
				Enemy newEnemy = genEnemy();
				enemyar.add(newEnemy);
				add(newEnemy);
			}
			crushPlane();
			destroyEnemies();
			explodebombs();
			if(score>=stages[stage]){
				stage++;
				genPowerUp();
				if(stage==6)gameOver();
			}
			if(over)gameOver();
			pause(10);
		}
	}
	
	
	private void gameOver(){
		Highscores highscores = new Highscores();
		IODialog dial = new IODialog();
		String name = dial.readLine("Enter your name:");
		highscores.addScore(name,score);
		gameOver.setVisible(true);
		add(gameOver);
		pause(2000);
		start=false;
		over=false;
		lives=3;
		stage=0;
		plane.setVertSpeed(0);
		plane.setHorSpeed(0);
		plane.setLocation(0, 0);
		rerun();
	}
	
	private void addLabels(){
		add(startLabel);
		add(highscoresLabel);
		add(instructionsLabel);
		add(aboutLabel);
		add(exitLabel);
	}
	
	private void removeLabels(){
		remove(startLabel);
		remove(highscoresLabel);
		remove(instructionsLabel);
		remove(aboutLabel);
		remove(exitLabel);
	}

	private Enemy groundEnemy(){
		Enemy newEnemy = new Enemy(APPLICATION_WIDTH,APPLICATION_HEIGHT-60,groundenemy_image);
		newEnemy.setSize(75, 60);
		newEnemy.setSpeed(-1);
		newEnemy.setPoints(1);
		newEnemy.type="groundenemy";
		return newEnemy;
	}
	
	private Enemy groundEnemy1(){
		Enemy newEnemy = new Enemy(APPLICATION_WIDTH,APPLICATION_HEIGHT-60,groundenemy1_image);
		newEnemy.setSize(75, 60);
		newEnemy.setSpeed(-1);
		newEnemy.setPoints(3);
		newEnemy.type="groundenemy1";
		return newEnemy;
	}
	
	private Enemy flyingEnemy(){
		double heigth = rand.nextDouble(0,200);
		Enemy newEnemy = new Enemy(APPLICATION_WIDTH,heigth,flyingenemy_image);
		newEnemy.setSize(60, 60);
		newEnemy.setSpeed(-2);
		newEnemy.setPoints(2);
		newEnemy.type="flyingenemy";
		return newEnemy;
	}
	
	private Enemy flyingEnemy1(){
		double heigth = rand.nextDouble(0,200);
		double speed = rand.nextDouble(1,7);
		Enemy newEnemy = new Enemy(APPLICATION_WIDTH,heigth,flyingenemy1_image);
		newEnemy.setSize(60, 60);
		newEnemy.setSpeed(-speed);
		newEnemy.setPoints(3);
		newEnemy.type="flyingenemy1";
		return newEnemy;
	}
	
	private Enemy genEnemy(){
		int i = rand.nextInt(2);
		if(stage>1)i = rand.nextInt(3);
		if(stage>2)i = rand.nextInt(4);
		switch(i){
		case 0: return flyingEnemy();
		case 1: return groundEnemy();
		case 2:return flyingEnemy1();
		default:return groundEnemy1();
		}
	}
	
	private Projectile genProj(double horizontal,double vertical,double hor_plane, double vert_plane){
		Projectile newProj = new Projectile(horizontal, vertical, proj_image);
		newProj.setVertSpeed(vert_plane);
		newProj.setHorSpeed(hor_plane+proj_speed);
		newProj.setSize(50, 10);
		return newProj;
	}
	
	private Projectile genBomb(double horizontal,double vertical,double hor_plane, double vert_plane){
		Projectile newProj = new Projectile(horizontal, vertical, bomb_image);
		newProj.setVertSpeed(vert_plane/3+bomb_speed);
		newProj.setHorSpeed(hor_plane/3);
		newProj.setSize(15, 22);
		return newProj;
	}
	
	private void genPowerUp(){
		int height = rand.nextInt(10, 400);
		String powerup_image="";
		String type="";
		int i = rand.nextInt(4);
		switch(i){
		case 0:powerup_image=PUscore_image;
		type="score";
		break;
		case 1:powerup_image=PUlife_image;
		type="life";
		break;
		case 2:powerup_image=PUshot_image;
		type="shot";
		break;
		case 3:powerup_image=PUdestroy_image;
		type="destroy";
		break;
		}
		PowerUp pUp = new PowerUp(APPLICATION_WIDTH,height,powerup_image);
		pUp.setType(type);
		powerupar.add(pUp);
		add(pUp);
	}
	
	public void keyPressed(KeyEvent ke) {
	    if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
	        plane.setVertSpeed(1.5);
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_UP) {
	        plane.setVertSpeed(-1.5);
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
	        plane.setHorSpeed(2.4);
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
	        plane.setHorSpeed(-2.4);
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_R) {
	       	over=true;
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
	    	if(System.currentTimeMillis()-lastshot>=150){
	    		   if(doubleshot==false){
		        	Projectile newProj = genProj(plane.getPoint_X(), plane.getPoint_Y(), plane.getHorSpeed(), plane.getVertSpeed());
					projar.add(newProj);
					add(newProj);
		        }else{
		        	Projectile newProj1 = genProj(plane.getPoint_X(), plane.getPoint_Y(), plane.getHorSpeed(), plane.getVertSpeed()-0.5);
		        	Projectile newProj2 = genProj(plane.getPoint_X(), plane.getPoint_Y(), plane.getHorSpeed(), plane.getVertSpeed()+0.5);
		        	projar.add(newProj1);
		        	projar.add(newProj2);
					add(newProj1);
					add(newProj2);
		        }
	    		lastshot=System.currentTimeMillis();
	    	}
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
	    	if(System.currentTimeMillis()-lastbomb>=300){
		        Projectile newProj = genBomb(plane.getPointB_X(), plane.getPointB_Y(), plane.getHorSpeed(), plane.getVertSpeed());
		        newProj.bomb=true;
				projar.add(newProj);
				add(newProj);
				lastbomb=System.currentTimeMillis();
	    	}
	    }
	    
	}
	
	public void keyReleased(KeyEvent ke) {
	    if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
	        plane.setVertSpeed(0);
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_UP) {
	        plane.setVertSpeed(0);
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
	        plane.setHorSpeed(0);
	    }
	    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
	        plane.setHorSpeed(0);
	    }
	}
	
	private int spawnRate(){
		switch(stage){
		case 0:return rand.nextInt(100000);
		case 1:return rand.nextInt(70000);
		case 2:return rand.nextInt(50000);
		case 3:return rand.nextInt(30000);
		case 4:return rand.nextInt(10000);
		default:return rand.nextInt(5000);
		}
	}
	
	private void destroyEnemies(){
		for(int i=0;i<enemyar.size();i++){
			proceedDestroy(enemyar.get(i));
		}
	}
	
	private void proceedDestroy(Enemy enemy){
		if(enemy.stage==1){
			enemy.destroy();
		}
		if((enemy.stage==2)&&(System.currentTimeMillis()-enemy.destroyTime>=1000))
			enemy.destroy();
		if((enemy.stage==3)&&(System.currentTimeMillis()-enemy.destroyTime>=100)){
			score = enemy.getPoints()*multi+score;
			scoreLabel.setLabel("Score:"+String.valueOf(score));
			remove(enemy);
			enemyar.remove(enemy);
		}
	}
	
	private void explodebombs(){
		for(int i=0;i<projar.size();i++){
			if(projar.get(i).destroyTime!=Long.MAX_VALUE)explodebomb(projar.get(i));
		}
	}
	
	private void explodebomb(Projectile proj) {
		if(System.currentTimeMillis()-proj.destroyTime>=100){
			remove(proj);
			projar.remove(proj);
		}else if(System.currentTimeMillis()-proj.destroyTime<0){
			proj.setLocation(proj.getX()-proj.getWidth(), proj.getY()-proj.getHeight());
			proj.setImage(bombhit_image);
			proj.setVertSpeed(0);
			proj.setHorSpeed(0);
			proj.destroyTime=System.currentTimeMillis();
		}
	}
	
	private void shootAir(){
		for(int i=0;i<enemyar.size();i++){
			if(enemyar.get(i).type.equals("flyingenemy")){
				Projectile proj = new Projectile(enemyar.get(i).getHorX(),enemyar.get(i).getHorY(),horproj_image);
				proj.setHorSpeed(-5);
				enemyprojar.add(proj);
				add(proj);
			}
		}
	}
	
	private void shootGround(){
		for(int i=0;i<enemyar.size();i++){
			if(enemyar.get(i).type.equals("groundenemy1")){
				Projectile proj = new Projectile(enemyar.get(i).getVertX(),enemyar.get(i).getVertY(),vertproj_image);
				proj.setVertSpeed(-4);
				enemyprojar.add(proj);
				add(proj);
			}
		}
	}
	
	private void moveEnemies(){
		for(int i = 0;i<enemyar.size();i++){
			enemyar.get(i).move(enemyar.get(i).getSpeed(), 0);
			if(enemyar.get(i).getX()<0){
				remove(enemyar.get(i));
				enemyar.remove(i);
			}
		}
	}
	
	private void moveProjs(){
		for(int i=0;i<projar.size();i++){
			projar.get(i).move(projar.get(i).getHorSpeed(), projar.get(i).getVertSpeed());
			if((projar.get(i).getX()>APPLICATION_WIDTH)||(projar.get(i).getY()+projar.get(i).getHeight()>APPLICATION_HEIGHT-20)){
				if(projar.get(i).bomb)explodebomb(projar.get(i));
				else{
					remove(projar.get(i));
					projar.remove(i);
				}
			}
		}
		for(int i=0;i<enemyprojar.size();i++){
			enemyprojar.get(i).move(enemyprojar.get(i).getHorSpeed(), enemyprojar.get(i).getVertSpeed());
			if((enemyprojar.get(i).getX()>APPLICATION_WIDTH)||(enemyprojar.get(i).getY()+enemyprojar.get(i).getHeight()>APPLICATION_HEIGHT)){
				remove(enemyprojar.get(i));
				enemyprojar.remove(i);
			}
		}	
	}
	
	private void movePowerUps(){
		for(int i=0;i<powerupar.size();i++){
			powerupar.get(i).move(powerupar.get(i).getSpeed(), 0);
			if(collision(plane,powerupar.get(i))){
				switch(powerupar.get(i).getType()){
				case "score":
					multi=multi*2;
					break;
				case "life":
					lives++;
					livesLabel.setLabel("Lives"+String.valueOf(lives));
					break;
				case "shot":
					if(doubleshot==true){
						lives++;
					livesLabel.setLabel("Lives"+String.valueOf(lives));	
					}				
					else doubleshot=true;
					break;
				case "destroy":
					for(int j=0;j<enemyar.size();j++){
						enemyar.get(j).stage=1;
						proceedDestroy(enemyar.get(j));
					}
					break;
				}
				remove(powerupar.get(i));
				powerupar.remove(i);
			}
		}
	}
	

	private void doCollisions(ArrayList<Enemy> enemyar,ArrayList<Projectile> projar){
		try {
			for(int i=0;i<projar.size();i++){
				for(int j=0;j<enemyar.size();j++){
					if((collision(projar.get(i),enemyar.get(j)))&&(enemyar.get(j).stage==0)){
						if(projar.get(i).bomb)explodebomb(projar.get(i));
						else{
							remove(projar.get(i));
							projar.remove(i);
						}
						enemyar.get(j).stage=1;
						proceedDestroy(enemyar.get(j));
					}
				}
				for(int j=0;j<enemyprojar.size();j++){
					if(collision(projar.get(i),enemyprojar.get(j))){
						remove(projar.get(i));
						remove(enemyprojar.get(j));
						projar.remove(i);
						enemyprojar.remove(j);
					}
				}
			}
			for(int i=0;i<enemyprojar.size();i++)
				if(collision(enemyprojar.get(i),plane)){
					if(lives==0)gameOver();
					else{
						remove(enemyprojar.get(i));
						enemyprojar.remove(i);
						lives--;
						livesLabel.setLabel("Lives"+String.valueOf(lives));
					}
				}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	private boolean collision(GImage a, GImage b){
		if (a.getBounds().intersects(b.getBounds())) {
	        return true;
	    }
	    else return false;
	}
	
	private boolean towardsBounds(){
		if((plane.getX()<=0)&&(plane.getHorSpeed()<0)||(plane.getPoint_X()>=APPLICATION_WIDTH)&&(plane.getHorSpeed()>0)||(plane.getY()<=0)&&(plane.getVertSpeed()<0)){
			return true;
		}
		return false;
	}
	
	private void crushPlane(){
		if((plane.getPointB_Y()>=APPLICATION_HEIGHT)&&(plane.getVertSpeed()>0)){
			if(lives==0)gameOver();
			else{
				lives--;
				livesLabel.setLabel("Lives"+String.valueOf(lives));
				plane.setLocation(0, 0);
			}
		}
		for(int i=0;i<enemyar.size();i++){
			if(collision(plane,enemyar.get(i))){
				if(lives==0)gameOver();
				else{
					remove(enemyar.get(i));
					enemyar.remove(i);
					lives--;
					livesLabel.setLabel("Lives"+String.valueOf(lives));
				}
			}
		}
	}
	
}
