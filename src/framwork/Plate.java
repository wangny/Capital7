package framwork;

import java.util.ArrayList;

public class Plate {
	ArrayList<Cube> cubes;
	
	public Plate(){
		
	}
	
	public void display(){
		for(Cube c : cubes){
			c.display();
		}
	}
}
