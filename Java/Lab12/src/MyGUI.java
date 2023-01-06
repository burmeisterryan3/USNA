import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * Class which allows for GUI interface between a client and server process <br/>
 * --See FileClient for further details on implementation
 */
public class MyGUI extends JFrame implements MouseListener
{

		private FileClient fc;
		private JButton connect;
		private JButton update;
		private JButton getFile;
		private JButton quit;
		private JTextArea text;
		private JTextArea getText;
		private JTextArea success;
		private JPanel myPanel;
		private JPanel secondPanel;
		private int port;
		private String ip;
		
		/**
		 * Constructor for MYGUI <br/> Requires a FileClient Object, an IP address, and a port number
		 * 
		 * @param fc FileClient which allows for interaction between a MyGUI object and a FileServer
		 * @param ip IP address of server to connect to
		 * @param port Port number to establish connection on
		 */
		public MyGUI(FileClient fc, String ip, int port){
			super(ip + ": " + port);
			this.fc = fc;
			this.ip = ip;
			this.port = port;
			connect = new JButton("Connect");
			connect.addMouseListener(this);
			myPanel = new JPanel(new BorderLayout());
			//secondPanel = new JPanel(new BoxLayout(secondPanel, BoxLayout.X_AXIS));
			//secondPanel.add(connect);
			myPanel.add(connect/*secondPanel*/, BorderLayout.NORTH);
			super.add(myPanel);
			super.setSize(400,400);
			super.setVisible(true);
			super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == connect){
				fc.connect(ip, port);
				update = new JButton("Update");
				update.addMouseListener(this);
				myPanel.remove(connect);
				myPanel.add(update, BorderLayout.NORTH);
				super.validate();
				super.repaint();
			} else if (e.getSource() == update){
				myPanel.remove(update);
				text = new JTextArea(1, 1);
				myPanel.add(text, BorderLayout.NORTH);
				String files = fc.update();
				text.setText(files);
				getText = new JTextArea(1, 1);
				getFile = new JButton("Get File");
				getFile.addMouseListener(this);
				secondPanel = new JPanel(new FlowLayout());
				secondPanel.add(getText);
				secondPanel.add(getFile);
				myPanel.add(secondPanel, BorderLayout.EAST);
				super.validate();
				super.repaint();
			} else if (e.getSource() == getFile){
				String info = "File " +  getText.getText() + ": ";
				info += fc.getFile(getText.getText());
				myPanel.remove(getText);
				success = new JTextArea (1,1);
				myPanel.add(success, BorderLayout.SOUTH);
				success.setText(info);
				quit = new JButton("Quit");
				quit.addMouseListener(this);
				myPanel.add(quit, BorderLayout.WEST);
			} else if (e.getSource() == quit) {
				fc.quit();
				this.dispose();
			}
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
}
