package framwork;
import controlP5.ControlP5;

@SuppressWarnings("serial")
public class StartWindow {
	private ControlP5 cp5;
	myApplet parent;

	
		
		public StartWindow(myApplet applet)  {
			this.parent = applet;
		}
		
		public void display(){	
			cp5=new ControlP5(this.parent);
			cp5.addButton("OnePlayer")
				.setLabel("O n e  P l a y e r")
				.setPosition(myApplet.width-250, myApplet.height-500)
				.setSize(200,50);
			cp5.addButton("TwoPlayer")
				.setLabel("T w o  P l a y e r")
				.setPosition(myApplet.width-250, myApplet.height-600)
				.setSize(200,50);
			cp5.addButton("MultiPlayer")
			.setLabel("M u l t i   P l a y e r")
			.setPosition(myApplet.width-250, myApplet.height-600)
			.setSize(200,50);
			
			
			cp5.getController("OnePlayer")
		       .getCaptionLabel()
		       .setSize(22);
			cp5.getController("TwoPlayer")
		       .getCaptionLabel()
		       .setSize(24);
			cp5.getController("MultiPlayer")
		       .getCaptionLabel()
		       .setSize(22);
			
		}

	

		
	}


