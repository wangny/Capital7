package framwork;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

@SuppressWarnings("serial")
public class myApplet extends PApplet{
	
	Plate currentp;
	Ani ani;
	StartWindow startwindow;
	PImage img;
	public final static int width = 800, height = 700 , cubewidth = 50, cubeheight = 60;
	int gamePhase ; /// 0 : startwindow, 1 : single, 2 : two player, 3 : multi
	
	public void setup(){
		
		size(width, height);
		
		background(240);
		smooth();
		
		Ani.init(this);
		
		startwindow = new StartWindow(this);
		
		currentp = new Plate(this);
		loadData();
		//Thread t2 = new Thread(currentp);
		//t2.start();
		
		gamePhase = 0;
		img = loadImage("g.png"); // 將圖檔載入
		image(img,0,0); // 決定要顯現的圖檔與其左上角座標
	}
	
	public void draw(){
		//plate background and frame
		/*fill(255);
		rect(50, 20, width-100, height-80, 15);*/
		
		if(gamePhase==0) startwindow.display();
		else if(gamePhase==1){
			fill(255);
			rect(50, 20, width-100, height-80, 15);
			startwindow.cp5.getController("OnePlayer").setVisible(false);
			//startwindow.cp5.getController("OnePlayer").setValue(0);
			startwindow.cp5.getController("TwoPlayer").setVisible(false);
			startwindow.cp5.getController("MultiPlayer").setVisible(false);
			this.repaint();
			currentp.display();
			
		}
	}
	
	public void mousePressed(){
		System.out.println("press press");
		currentp.mousePressed();
	}
	
	public void mouseDragged(){
		currentp.mouseDragged();
	}
	
	public void mouseReleased(){
		currentp.mouseReleased();
	}
	
	public void loadData(){
		System.out.print("execute");
		JSONObject data = loadJSONObject("resources/cube.json");
		JSONArray cubes = data.getJSONArray("cube");
		for (int i = 0; i < cubes.size(); i++){
			JSONObject cube = cubes.getJSONObject(i);
			String name = cube.getString("name");
			String target = cube.getString("target");
			String colours = cube.getString("colour");
			//
			String[] colour = new String[7];
			colour = colours.split(",");
			/*for (int j = 0; j < 7; j++){
				colour[j] = colours.substring(9*j, 9*j+8);
			}*/
			//
			Cube c = new Cube(name,target,colour);	//???
			currentp.cubeDB.add(c);
		}
	}
	
	public void changePhase(int phase){
		gamePhase = phase;
	}
	
	public void OnePlayer(){	
		changePhase(1);
		System.out.println("click one player");
		Thread t2 = new Thread(currentp);
		t2.start();
	}

	
}
