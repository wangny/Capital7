package framwork;

import java.util.ArrayList;

public class Plate implements Runnable{
	ArrayList<Cube> cubes;
	myApplet parent;
	
	public Plate(myApplet applet){
		this.parent = applet;
		cubes = new ArrayList<Cube>();
	}
	
	public void display(){	
		for(Cube c : cubes){
			c.display();
		}
	}

	
	public void addCube(){		
		for(Cube c : cubes) c.grow();

		int x = 100;
		for(int i=0; i<10; i++){
			Cube c = new Cube(parent, 3, "try", x, myApplet.height-150);
			cubes.add(c);
			x = x + myApplet.cubewidth + 10;
		}
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			System.out.println(parent.pbar.done);
			if(parent.pbar.isdone()==true) {
				System.out.println("1");
				System.out.println("1");
				System.out.println("1");
				System.out.println("1");
				System.out.println("1");
				System.out.println("1");
				addCube();
				parent.pbar.undone();
			}
		}
	}
}
