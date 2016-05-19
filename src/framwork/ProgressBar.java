package framwork;

public class ProgressBar implements Runnable{
	
	private myApplet parent;
	boolean done = false;
	private static int goal = 20; // from 0 to 100
	private int value;
	
	
	public ProgressBar(myApplet p){
		parent = p;
		value = 0;
	}
	
	public void display(){
		int progressX = 100;
	    int progressY = 10;
	    int progressHeight = 8;
	    parent.fill (0);
	    parent.rect(progressX, progressY, goal*5, progressHeight);
	    parent.fill (255);
	    parent.rect(progressX, progressY, value*5, progressHeight);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			value++;
			if(value>goal) {
				value = 0;
				done = true;
			}
		}
	}
	
	public void undone(){
		done = false;
	}
	
	public boolean isdone(){
		return done;
	}
	
}
