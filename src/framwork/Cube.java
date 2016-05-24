package framwork;

import java.util.Arrays;

/**class for cube hahaha**/


public class Cube {
	
	private myApplet parent;
	private int state;	//the life of the cube ( 6 means disappear )
	private int x, y;		//the left-up-most & display position of the cube
	private String name;	//the text of the cube
	private String[] colour;	///save the color of each state
	private String target;	//the "answer" of the cube
	private boolean show, drag;
	
	
	public Cube(){
		this.state = 0;
		this.name = null;
		this.show = false;
	}
	
	public Cube(String name, String target, String[] colour){
		this.name = name;
		this.target = target;
		this.colour = colour;
		this.show = false;
	}
	
	public Cube(myApplet applet, int state, String name, String target, int x, int y){
		this.parent = applet;
		this.state = 3;
		this.x = x;
		this.y = y;
		this.name = name;
		this.target = target;
		this.show = true;
	}

	public Cube(myApplet parent, int state, Cube cube, int x, int y) {
		this.parent = parent;
		this.state = 0;
		this.x = x;
		this.y = y;
		this.name = cube.name;
		this.target = cube.target;
		this.colour = cube.colour;
		this.show = true;
	}

	public void display(){
		//display cube
		this.parent.fill( myApplet.unhex(this.colour[this.state]) );	///fill with the color relative to the state
		this.parent.rect(this.x, this.y, myApplet.cubewidth, myApplet.cubeheight, 5);	
	}
	
	public void display(int x, int y){
		this.x=x; this.y=y;
		this.parent.fill(myApplet.unhex(this.colour[this.state]) );
		this.parent.rect(x, y, myApplet.cubewidth, myApplet.cubeheight, 5);	
	}
	

	public void grow(){
		this.y = this.y - myApplet.cubeheight - 10;
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
	
	public void setDarg(boolean b){
		this.drag = b;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public void addX(int addX){
		if (drag)
			this.x = this.x + addX;
		else
			this.x = this.x;	//remain the same
	}
	public void addY(int addY){
		if (drag)
			this.y = this.y + addY;
		else
			this.y = this.y;	//remain the same
	}
	public void setX(int newX){
		this.x = newX;
	}
	public void setY(int newY){
		this.y = newY;
	}
}
