package framwork;

import java.util.Map;

/**class for cube hahaha**/


public class Cube {
	
	private 
	myApplet parent;
	public int state;	///the live of the cube ( 0 means disappear )
	public int x,y;		/// the position of the cube
	private String name;	///the text of the cube
	private String[] colour = {"FF004B97","FF007979","FF019858"};	///save the color of each state
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
		this.parent.fill( myApplet.unhex(this.colour[this.state-1]) );	///fill with the color relative to the state
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
