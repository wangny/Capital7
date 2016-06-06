package framwork;

import java.io.Serializable;

/**class for cube hahaha**/


public class Cube implements Serializable{
	
	private myApplet parent;
	private int state;			///the life of the cube ( 6 means disappear )
	private int x, y;			///the left-up-most & display position of the cube
	private String name;		///the text of the cube
	private String[] colour;	///save the color of each state
	private String target;		///the "answer" of the cube
	private boolean show, drag;
	
	
	public Cube(){				///default constructor
		this.state = 0;
		this.name = null;
		this.show = false;
		this.drag = false;
	}
	
	public Cube(String name, String target, String[] colour){	///for cube data base
		this.name = name;
		this.target = target;
		this.colour = colour;
		this.show = false;
		this.drag = false;
	}
	
	public Cube(myApplet applet, int state, String name, String target, int x, int y){  ///old-version, unused
		this.parent = applet;
		this.state = state;
		this.x = x;
		this.y = y;
		this.name = name;
		this.target = target;
		this.show = true;
		this.drag = false;
	}

	public Cube(myApplet parent, int state, Cube cube, int x, int y) { 	///for real cube
		this.parent = parent;
		this.state = state;
		this.x = x;
		this.y = y;
		this.name = cube.name;
		this.target = cube.target;
		this.colour = cube.colour;
		this.show = true;
		this.drag = false;
	}

	public void display(){
		//display cube
		if(this!=null){
			if(this.state<=6) this.parent.fill( myApplet.unhex(this.colour[this.state]) );	///fill with the color relative to the state
			this.parent.rect(this.x, this.y, myApplet.cubewidth, myApplet.cubeheight, 5);
		}
		
		if(this.state > 4) this.parent.fill(myApplet.unhex(this.colour[0]));
		else this.parent.fill(255);
		this.parent.textSize(17);
		this.parent.text(this.name, this.x+1, this.y+20);
	}
	
	public void display(int x, int y){	
		this.x=x; this.y=y;
		this.parent.fill(myApplet.unhex(this.colour[this.state]) );
		this.parent.rect(x, y, myApplet.cubewidth, myApplet.cubeheight, 5);	
	}
	
	public String getTarget(){
		return this.target;
	}
	
	public String getName(){
		return this.name;
	}

	public int getState(){
		return this.state;
	}
	
	public void setState(int s){
		this.state = s;
	}
	
	public void setDrag(boolean b){
		this.drag = b;
	}
	
	public boolean isDragged(){
		return this.drag;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void addX(int addX){
		if (drag) this.x = this.x + addX;
	}
	
	public void addY(int addY){
		if (drag) this.y = this.y + addY;
	}
	
	public void setX(int newX){
		this.x = newX;
	}
	
	public void setY(int newY){
		this.y = newY;
	}
}
