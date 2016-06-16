package framwork;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Main extends JFrame{
	
	public static void main(String [] args){
		
		myApplet applet = new myApplet();
		applet.init();
		applet.start();
		applet.setFocusable(true);
		
		JFrame window = new JFrame("Capital7");
		window.setContentPane(applet);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(980, 900);
		window.setVisible(true);
	}
}

