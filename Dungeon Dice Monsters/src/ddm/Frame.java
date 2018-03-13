package ddm;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

public class Frame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	JWindow loadWindow;

	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Dungeon Dice Monsters - a Furman Production");
		setResizable(false);
	    JDialog.setDefaultLookAndFeelDecorated(true);
		init();
	}
	
	public void init() {
		
		loading();
		
		Panel mainPanel = new Panel();

		loadWindow.dispose();
		
		add(mainPanel);
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		mainPanel.getTiles()[2][(mainPanel.getTiles().length-1)/2].updatePics();
		mainPanel.getTiles()[mainPanel.getTiles().length-3][(mainPanel.getTiles().length-1)/2].updatePics();
		
		JOptionPane.showMessageDialog(null, 
				"Welcome to Dungeon Dice Monsters!", 
				mainPanel.getTiles()[mainPanel.getTiles().length-3][(mainPanel.getTiles().length-1)/2].monster().owner().name()
				+ "  vs.  "
				+ mainPanel.getTiles()[2][(mainPanel.getTiles().length-1)/2].monster().owner().name(),
				JOptionPane.INFORMATION_MESSAGE);
		
		mainPanel.changePhase("Roll");	//starts game
	}
	
	public void loading(){
		loadWindow = new JWindow();
		JLabel loadingLabel = new JLabel();
		loadingLabel.setOpaque(false);
		loadingLabel.setFocusable(false);
		loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		URL imageURL = getClass().getResource("resources/" + "loading" + ".gif");	
		if (imageURL != null) {
			ImageIcon imageIcon = new ImageIcon(imageURL);
			//Image image = imageIcon.getImage();
			//Image newimg = image.getScaledInstance(500, 500, java.awt.Image.SCALE_DEFAULT);
			//imageIcon = new ImageIcon(newimg);
			loadingLabel.setIcon(imageIcon);
			loadWindow.add(loadingLabel);
			loadWindow.pack();
			loadWindow.setLocationRelativeTo(null);
			loadWindow.setVisible(true);
		}
		else {
			System.out.println("imageURL: " + imageURL);
			JOptionPane.showConfirmDialog(null, "ERROR 69: bad imageURL recieved from Frame class for loading.gif\n"
					+ "Do something about it.", 
	 				"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void main (String[] args) {
		new Frame();
	}
	

}
