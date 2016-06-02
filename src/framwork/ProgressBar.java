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
	    int progressHeight = 10;
	    parent.fill ( 240 );
	    parent.rect(progressX-5, progressY-5, goal*5+10, progressHeight+10,8);
	    parent.fill (myApplet.unhex("FFCD950C"));
	    parent.rect(progressX, progressY, (goal-value)*5, progressHeight,5);
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
