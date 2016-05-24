package framwork;
import controlP5.ControlP5;



@SuppressWarnings("serial")
public class StartWindow {
	public ControlP5 cp5;
	myApplet parent;

	
		
		public StartWindow(myApplet applet)  {
			this.parent = applet;
			if(this.parent.gamePhase==0){
			cp5=new ControlP5(this.parent);
			cp5.addButton("OnePlayer")
				.setLabel("O n e  P l a y e r")
				.setPosition(myApplet.width-525, myApplet.height-290)
				.setSize(250,50);
			cp5.addButton("TwoPlayer")
				.setLabel("T w o  P l a y e r")
				.setPosition(myApplet.width-525, myApplet.height-220)
				.setSize(250,50);
			cp5.addButton("MultiPlayer")
				.setLabel("M u l t i   P l a y e r")
				.setPosition(myApplet.width-525, myApplet.height-150)
				.setSize(250,50);
			
			
			cp5.getController("OnePlayer")
		       .getCaptionLabel()
		       .setSize(22);
			cp5.getController("TwoPlayer")
		       .getCaptionLabel()
		       .setSize(24);
			cp5.getController("MultiPlayer")
		       .getCaptionLabel()
		       .setSize(24);
			
			}
		}
		
		public void display(){	
			cp5.getController("OnePlayer").setVisible(true);
			cp5.getController("TwoPlayer").setVisible(true);
			cp5.getController("MultiPlayer").setVisible(true);
			this.parent.repaint();
		}
		
		
	

		
	}


