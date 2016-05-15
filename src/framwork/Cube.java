package framwork;

import java.util.Map;

/**class for cube hahaha**/


public class Cube {
	
	private 
	myApplet parent;
	public int state;
	public int x,y;
	private int colour;
	private String name;
	private Map<Character,Integer> targets;
	private boolean show;
	
	
	public Cube(){
		this.state = 0;
		this.colour = 000000;
		this.name = null;
		this.show = false;
	}
	
	public Cube(myApplet applet, int state, String name){
		this.parent = applet;
		this.state = state;
		this.colour = 000000;
		this.name = name;
		this.show = true;
	}

	public void display(){
		this.parent.fill(this.colour);
		this.parent.rect(x, y, 50, 50);	
	}
	
	
	public void display(int x, int y){
		this.x=x; this.y=y;
		this.parent.fill(this.colour);
		this.parent.rect(x, y, 50, 50);	
	}
	

	
	public void addTarget(Character t, int value){
		this.targets.put(t,value);
	}
	
	public Map<Character,Integer> getTargets(){
		return this.targets;
	}
}
