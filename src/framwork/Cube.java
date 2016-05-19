package framwork;

import java.util.Map;

/**class for cube hahaha**/


public class Cube {
	
	private 
	myApplet parent;
	public int state;
	public int x,y;
	//private int colour;
	private String name;
	private String[] colour = {"FF004B97","FF007979","FF019858"};
	private Map<Character,Integer> targets;
	private boolean show;
	
	
	public Cube(){
		this.state = 0;
		this.name = null;
		this.show = false;
	}
	
	public Cube(myApplet applet, int state, String name, int x, int y){
		this.parent = applet;
		this.state = 3;
		this.x = x;
		this.y = y;
		this.name = name;
		this.show = true;
	}


	public void display(){
		this.parent.fill( myApplet.unhex(this.colour[this.state-1]) );
		this.parent.rect(x, y, myApplet.cubewidth, myApplet.cubeheight, 5);	
	}
	
	
	
	public void display(int x, int y){
		this.x=x; this.y=y;
		this.parent.fill(myApplet.unhex(this.colour[this.state-1]) );
		this.parent.rect(x, y, myApplet.cubewidth, myApplet.cubeheight, 5);	
	}
	

	public void grow(){
		this.y = this.y - myApplet.cubeheight-10;
	}
	
	
	public void addTarget(Character t, int value){
		this.targets.put(t,value);
	}
	
	public Map<Character,Integer> getTargets(){
		return this.targets;
	}
}
