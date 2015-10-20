import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener {

	private JPanel main, firstBlock, buttonBlock, panelNewResolution;
	private JButton buttonOpen, buttonSave, buttonShow, buttonOrginal;
	private JTextField textWidth, textHeight;
	private Picture picture, newPicture;
	private File file;
	private JFileChooser fileopen;
	private static boolean isOpen = false;

	public Window() {
		super("Ресайзинг картинок");
		startPanel();
		setLocationRelativeTo(main);
		this.add(main);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void startPanel() {
		main = new JPanel(new BorderLayout());
		main.setBorder(new EmptyBorder(10, 100, 7, 100));
		buttonBlock = new JPanel();
		buttonOpen = new JButton("Відкрити картинку");
		buttonOpen.addActionListener(this);
		buttonBlock.add(buttonOpen);
		main.add(buttonBlock, BorderLayout.CENTER);
	}

	public Window(File selectedFile) {
		super("Ресайзинг картинок");
		file = selectedFile;
		picture = new Picture(selectedFile);
		this.main = new JPanel(new BorderLayout());
		this.main.setBorder(new EmptyBorder(10, 10, 0, 10));
		firstBlock = new JPanel(new GridLayout(2, 2));
		buttonOpen = new JButton("Відкрити нову картинку");
		buttonOpen.addActionListener(this);
		firstBlock.add(new JLabel("Оригінальне розширення: "));
		firstBlock.add(new JLabel(picture.width() + "x" + picture.height()));
		firstBlock.add(new JLabel("Нове розширення: "));
		panelNewResolution = new JPanel();
		textWidth = new JTextField("" + picture.width());
		textHeight = new JTextField("" + picture.height());
		panelNewResolution.add(textWidth);
		panelNewResolution.add(new JLabel("x"));
		panelNewResolution.add(textHeight);
		firstBlock.add(panelNewResolution);

		buttonBlock = new JPanel();
		buttonSave = new JButton("Зберегти");
		buttonSave.addActionListener(this);
		buttonOrginal = new JButton("Оригінал");
		buttonOrginal.addActionListener(this);
		buttonShow = new JButton("Показати");
		buttonShow.addActionListener(this);
		buttonBlock.add(buttonSave);
		buttonBlock.add(buttonOrginal);
		buttonBlock.add(buttonShow);
		main.add(buttonOpen, BorderLayout.NORTH);
		main.add(firstBlock, BorderLayout.CENTER);
		main.add(buttonBlock, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(main);
		add(main);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void createNewPicture() {
		int newWidth, newHeight;
		try{
			newWidth = (int) Double.parseDouble(textWidth.getText());
			newHeight = (int) Double.parseDouble(textHeight.getText());
		} catch(Exception e){
			return;
		}
		SeamCarver pic = new SeamCarver(picture);
		boolean progressOFF = true;
		int count = Math.abs(pic.width() - newWidth)
				+ Math.abs(pic.height() - newHeight);
		ProgBar tf = new ProgBar();
		if (pic.width() > newWidth) {
			if (progressOFF) {
				tf.run();
				progressOFF = false;
			}
			while (pic.width() != newWidth) {
				pic.removeVerticalSeam(pic.findVerticalSeam());
				tf.setProgress(100
						- ((Math.abs(pic.width() - newWidth) + Math.abs(pic
								.height() - newHeight)) * 100) / count);
			}
			pic.setPic1();
		} else {
			if (progressOFF) {
				tf.run();
				progressOFF = false;
			}
			while (pic.width() != newWidth) {
				pic.addVerticalSeam(pic.findVerticalSeam());
				tf.setProgress(100
						- ((Math.abs(pic.width() - newWidth) + Math.abs(pic
								.height() - newHeight)) * 100) / count);
			}
			pic.setPic();
		}

		if (pic.height() > newHeight) {
			if (progressOFF) {
				tf.run();
				progressOFF = false;
			}
			while (pic.height() != newHeight) {
				pic.removeHorizontalSeam(pic.findHorizontalSeam());
				tf.setProgress(100 - ((Math.abs(pic.width() - newWidth) + Math.abs(pic
								.height() - newHeight)) * 100) / count);
			}
			pic.setPic1();
		} else {
			if (progressOFF) {
				tf.run();
				progressOFF = false;
			}
			while (pic.height() != newHeight) {
				pic.addHorizontalSeam(pic.findHorizontalSeam());
				tf.setProgress(100 - ((Math.abs(pic.width() - newWidth) + Math.abs(pic
								.height() - newHeight)) * 100) / count);
			}
			pic.setPic();
		}
		if (!progressOFF)
			tf.getFrame().dispose();
		newPicture = pic.picture();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonOpen) {
			fileopen = new JFileChooser();
			fileopen.setFileFilter(new FileNameExtensionFilter("Images", "jpg",
					"png", "gif", "bmp"));
			fileopen.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int ret = fileopen.showDialog(null, "Відкрити");

			if (ret == JFileChooser.APPROVE_OPTION) {
				this.setVisible(false);
				@SuppressWarnings("unused")
				Window window = new Window(fileopen.getSelectedFile());
				this.removeAll();
			}
		} else if (e.getSource() == buttonSave) {
			Thread myThready = new Thread(new Runnable() {
				public void run() {
					if (!isOpen) {
						createNewPicture();
					}
					newPicture.save(file.getAbsolutePath().replace(file.getAbsolutePath().substring(file.getAbsolutePath().length()-4), "_new.jpg"));
				}
			});
			myThready.start();
		} else if (e.getSource() == buttonOrginal) {
			picture.show();
			picture.getFrame().setVisible(true);
		} else if (e.getSource() == buttonShow) {
			Thread myThready = new Thread(new Runnable() {
				public void run() {
					isOpen = true;
					boolean widthIsNotNormal = true;
					boolean heightIsNotNormal = true;
					int w = 0,h = 0;
					if(widthIsNotNormal || heightIsNotNormal)
					{
						try{w = (int) Double.parseDouble(textWidth.getText());}
						catch (Exception d)
						{
							textWidth.setText(""+picture.width());
							widthIsNotNormal=false;
						}
							
						try{h = (int) Double.parseDouble(textHeight.getText());}
						catch (Exception d)
						{
							textHeight.setText(""+picture.height());
							heightIsNotNormal=false;
						}
					}
					if(widthIsNotNormal && heightIsNotNormal)
					{
						if (picture.width() == w && picture.height()==h)
							picture.show();
						else {
							if(w>2 && h>2)
							{
								createNewPicture();
								newPicture.show();
							}
						}
					}
				}
			});
			myThready.start();
		}
	}

}
