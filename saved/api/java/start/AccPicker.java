package start;

import com.Whodundid.core.util.storageUtil.EArrayList;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AccPicker extends JFrame implements ItemListener, KeyListener, MouseListener {
	
	private GradleStart start;
	private static JFrame thePanel;
	private static JLabel logText, text, serverText, rmLabel, serverLabel;
	private static JLabel inputLabel, hypixelLabel;
	private JCheckBox safeRM;
	private JCheckBox connectToServerOnStart, hypixel;
	private JTextField serverField;
	private EArrayList<AccButton> accButtons = new EArrayList();
	private Rectangle res;
	private boolean useSafeRM = false;
	private String server = "";
	private boolean connectToServer = false;
	
	public AccPicker(GradleStart startIn) {
		start = startIn;
		thePanel = this;
		
		thePanel.getContentPane().setBackground(SystemColor.inactiveCaption);
		thePanel.getContentPane().setLayout(null);
		thePanel.setLocationRelativeTo(null);
		thePanel.setTitle("EMC Dev Account Selector");
		
		res = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		thePanel.setBounds(res.width / 2 - 250, res.height / 2 - 250, 500, 500);
		thePanel.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		thePanel.setVisible(true);
		
		makeObjects();
	}
	
	private void makeObjects() {
		accButtons.add(new AccButton(this, res.x + 120, res.y + 150).setAcc("Whodundid", "Whodundid@gmail.com", "Albatross1998"));
		accButtons.add(new AccButton(this, res.x + 120, res.y + 190).setAcc("Jenarie", "Whodundid7@gmail.com", "IDon'tFuckingKNow12"));
		accButtons.add(new AccButton(this, res.x + 120, res.y + 230));
		
		for (AccButton b : accButtons) { thePanel.getContentPane().add(b); }
		
		safeRM = new JCheckBox();
		safeRM.setBounds(res.x + 75, res.y + 30, 21, 21);
		safeRM.addItemListener(this);
		
		connectToServerOnStart = new JCheckBox();
		connectToServerOnStart.setBounds(res.x + 75, res.y + 60, 21, 21);
		connectToServerOnStart.addItemListener(this);
		
		hypixel = new JCheckBox();
		hypixel.setBounds(res.x + 75, res.y + 90, 21, 21);
		hypixel.addItemListener(this);
		
		text = new JLabel();
		text.setBounds(res.x + 90, res.y + 380, 400, 50);
		
		serverText = new JLabel();
		serverText.setBounds(res.x + 90, res.y + 395, 400, 50);
		
		rmLabel = new JLabel("use safe rm");
		rmLabel.setBounds(res.x + 100, res.y + 15, 110, 50);
		rmLabel.addMouseListener(this);
		
		serverLabel = new JLabel("Conect to server on start");
		serverLabel.setBounds(res.x + 100, res.y + 45, 200, 50);
		serverLabel.addMouseListener(this);
		
		hypixelLabel = new JLabel("Connect to Hypixel on start");
		hypixelLabel.setBounds(res.x + 100, res.y + 75, 200, 50);
		hypixelLabel.addMouseListener(this);
		
		logText = new JLabel();
		logText.setBounds(res.x + 100, res.y + 410, 400, 50);
		
		serverField = new JTextField();
		serverField.setVisible(false);
		serverField.setBounds(res.x + 120, res.y + 350, 250, 25);
		serverField.addKeyListener(this);
		
		inputLabel = new JLabel("Enter a server IP");
		inputLabel.setVisible(false);
		inputLabel.setBounds(res.x + 125, res.y + 320, 250, 25);
		
		thePanel.getContentPane().add(text);
		thePanel.getContentPane().add(serverText);
		thePanel.getContentPane().add(rmLabel);
		thePanel.getContentPane().add(serverLabel);
		thePanel.getContentPane().add(logText);
		thePanel.getContentPane().add(safeRM);
		thePanel.getContentPane().add(connectToServerOnStart);
		thePanel.getContentPane().add(hypixel);
		thePanel.getContentPane().add(hypixelLabel);
		thePanel.getContentPane().add(inputLabel);
		thePanel.getContentPane().add(serverField);
		
		thePanel.repaint();
	}
	
	public void tryLogin(String userName, String[] args) {
		logText.setText(userName != null && !userName.isEmpty() ? "Attemping login for: " + userName + "..." : "Attemping login...");
		thePanel.repaint();
		Thread t = new Thread(() -> {
			try {
				//PrintWriter w = new PrintWriter("checkRM");
				//w.print(useSafeRM);
				//w.close();
				start.launch(args); }
			catch (Throwable e) { e.printStackTrace(); }
		});
		t.start();
	}
	
	public static void onLoginFailed() {
		logText.setText("Login failed!");
	}
	
	public static void closeWindow() {
		thePanel.setVisible(false);
		thePanel.dispatchEvent(new WindowEvent(thePanel, WindowEvent.WINDOW_CLOSING));
		thePanel.dispose();
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == safeRM) {
			useSafeRM = e.getStateChange() == 1;
			text.setText(useSafeRM ? "Will use safe rm mode!" : "");
		}
		
		if (e.getSource() == connectToServerOnStart) {
			connectToServer = connectToServerOnStart.isSelected() || hypixel.isSelected();
			
			serverField.setVisible(connectToServer);
			inputLabel.setVisible(connectToServer);
			
			if (e.getStateChange() == 1) {
				if (hypixel.isSelected()) { hypixel.setSelected(false); }
				server = serverField.getText();
			}
			
			if (!server.isEmpty()) {
				serverText.setText(connectToServer ? "Will attempt to connect to: " + server + " on start!" : "");
			}
		}
		
		if (e.getSource() == hypixel) {
			connectToServer = connectToServerOnStart.isSelected() || hypixel.isSelected();
			
			if (e.getStateChange() == 1 && connectToServerOnStart.isSelected()) {
				connectToServerOnStart.setSelected(false);
				serverField.setVisible(false);
				inputLabel.setVisible(false);
			}
			
			server = "mc.hypixel.net";
			if (!server.isEmpty()) {
				serverText.setText(connectToServer ? "Will attempt to connect to: " + server + " on start!" : "");
			}
		}
	}

	@Override public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == serverField) {
			server = serverField.getText();
			if (!server.isEmpty()) {
				serverText.setText(connectToServer ? "Will attempt to connect to: " + server + " on start!" : "");
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == serverField) {
			server = serverField.getText();
			if (!server.isEmpty()) {
				serverText.setText(connectToServer ? "Will attempt to connect to: " + server + " on start!" : "");
			}
		}
	}
	
	public boolean connectToServer() { return connectToServer; }
	public String getServer() { return server; }

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == rmLabel) { safeRM.setSelected(!safeRM.isSelected()); }
		if (e.getSource() == serverLabel) { connectToServerOnStart.setSelected(!connectToServerOnStart.isSelected()); }
		if (e.getSource() == hypixelLabel) { hypixel.setSelected(!hypixel.isSelected()); }
	}

	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}
