package framwork;


import controlP5.ControlP5;




public class StartWindow {
	public ControlP5 cp5;
	myApplet parent;
		
	public StartWindow(myApplet applet)  {
		
		this.parent = applet;
		if(this.parent.gamePhase==0){
		cp5=new ControlP5(this.parent);
		cp5.addButton("OnePlayer")
			.setPosition( (myApplet.width-300)/2, myApplet.height-450)
			.setImage(parent.loadImage("oneplayer.png"))
			.updateSize();
			//.setSize(250,50);
		cp5.addButton("TwoPlayer")
			.setPosition( (myApplet.width-300)/2, myApplet.height-345)
			.setImage(parent.loadImage("twoplayer.png"))
			.updateSize();
			//.setSize(250,50);
		cp5.addButton("ReadMe")
			//.setLabel("R e a d  M e")
			.setPosition( (myApplet.width-350)/2, myApplet.height-200)
			.setImage(parent.loadImage("readme.png"))
			.updateSize();
			//.setSize(250,50);
		cp5.addButton("Ok")
		//.setLabel("R e a d  M e")
		.setPosition( myApplet.width-150, 10)
		.setImage(parent.loadImage("ok.png"))
		.updateSize();
		//.setSize(250,50);
		
		
		/*cp5.getController("OnePlayer")
	       .getCaptionLabel()
	       .setSize(22);
		cp5.getController("TwoPlayer")
	       .getCaptionLabel()
	       .setSize(24);
		cp5.getController("MultiPlayer")
	       .getCaptionLabel()
	       .setSize(24);*/
		
		}
	}
	
	

	public void display(){	
		if(!parent.readmeDisplay){
			cp5.getController("OnePlayer").show();
			cp5.getController("TwoPlayer").show();
			cp5.getController("ReadMe").show();
			cp5.getController("Ok").hide();
		}else{
			cp5.getController("OnePlayer").hide();
			cp5.getController("TwoPlayer").hide();
			cp5.getController("ReadMe").hide();
			cp5.getController("Ok").show();
		}
		this.parent.redraw();
	}
		
		
	

}


