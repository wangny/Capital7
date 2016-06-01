package framwork;

public class ProgressBar implements Runnable{
	
	private myApplet parent;
	boolean done = false;
	private static int goal = 100; // from 0 to 100
	private int value;
	private boolean stop;
	
	public ProgressBar(myApplet p){
		parent = p;
		value = 0;
	}
	
	public void display(){
		int progressX = 100;
	    int progressY = 100;
	    int progressHeight = 8;
	    parent.fill (0);
	    parent.rect(progressX, progressY, goal*5, progressHeight);
	    parent.fill (255);
	    parent.rect(progressX, progressY, value*5, progressHeight);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		stop = false;
		
		while(true){
			System.out.print("");
			if(parent.gamePhase==5) continue;
			
			value++;
			if(value>goal) {
				value = 0;
				done = true;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(stop) break;
		}
	}
	
	public void undone(){
		done = false;
	}
	
	public boolean isdone(){
		return done;
	}
	
	public void stop(){
		stop = true;
	}
	
	
}
