package framwork;

import java.util.HashMap;
import java.util.Map;

/**class for cube hahaha**/


public class Cube {
	
	private 
	Applet parent;
	public int value;
	int x,y;
	int inix,iniy;
	public int colour;
	public String name;
	private boolean activate;
	private Map<Character,Integer> targets;
	boolean C_rectOver,A_rectOver;
	
	public Cube(){
		
	}

	public void display(){
		this.parent.fill(this.colour);
		this.parent.ellipse(x, y, 50, 50);	
	}
	
	
	public void display(int x, int y){
		this.x=x; this.y=y;
		this.parent.fill(this.colour);
		this.parent.ellipse(x, y, 50, 50);	
	}
	

	public boolean isActivated(){
		return this.activate;
	}
	
	public void changeActivate(){
		if(this.activate==true) this.activate = false;
		else this.activate = true;
		
		if(this.activate==false){
			this.x = this.inix;
			this.y = this.iniy;
		}
	}
	
	public void setActivate(boolean b){
		this.activate = b;
		if(b==false){
			this.x = this.inix;
			this.y = this.iniy;
		}
	}
	
	public void addTarget(Character t, int value){
		this.targets.put(t,value);
	}
	
	public Map<Character,Integer> getTargets(){
		return this.targets;
	}
}
