import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

import java.awt.Color;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class ProgBar extends JFrame {

	private JPanel contentPane;
	private ProgBar frame;
public ProgBar getFrame() {
		return frame;
	}
public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ProgBar();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static JProgressBar progressBar = new JProgressBar();
    public void setProgress(int a){
    	progressBar.setValue(a);
    	progressBar.repaint();
    }
	public ProgBar() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(60, 37, 320, 37);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		progressBar.setBorderPainted(false);
		progressBar.setPreferredSize(new Dimension(50, 14));
		progressBar.setStringPainted(true);		
		progressBar.setForeground(Color.GREEN);
		contentPane.add(progressBar, BorderLayout.CENTER);
	}
}
