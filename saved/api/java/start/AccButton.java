package start;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class AccButton extends JButton implements ActionListener {
	
	private AccPicker gui;
	private String name;
	private String user;
	private String pass;
	
	public AccButton(AccPicker guiIn, int xPos, int yPos) {
		gui = guiIn;
		addActionListener(this);
		setBounds(xPos, yPos, 250, 30);
		setText("no acc");
	}
	
	public AccButton setAcc(String nameIn, String userIn, String passIn) {
		name = nameIn;
		user = "username " + userIn;
		pass = "password " + passIn;
		setText(nameIn);
		return this;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String[] args = { user, pass };
			gui.tryLogin(name, args);
		} catch (Throwable q) { q.printStackTrace(); }
	}
}
