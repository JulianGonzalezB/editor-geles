import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PreView 
{
	private JFrame preframe;
	private BufferedImage imgJustEdited= null;
	private Editor editor= null;
	private JLabel lblImg= null;
	private File photo= null;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void PreView()
	{
		preframe = new JFrame();
		preframe.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		preframe.setSize(768, 580);
		preframe.setTitle("GELS EDITOR PREVIEW");
		preframe.setLocationRelativeTo(null);
		preframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		preframe.getContentPane().setLayout(null);
		this.createMicroAdjustFirstRow();
		this.createMicroAdjustSecondRow();
		this.saveButton();
		this.preframe.setVisible(true);
	}
	
	/**
	 * 
	 */
	private void createMicroAdjustFirstRow()
	{
		JButton btnUp = new JButton("UP");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveOneY(-5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnUp.setFont(new Font("Dialog", Font.BOLD, 9));
		btnUp.setBounds(12, 64, 52, 25);
		preframe.getContentPane().add(btnUp);
		
		JButton btnDown = new JButton("DOWN");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveOneY(5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnDown.setFont(new Font("Dialog", Font.BOLD, 9));
		btnDown.setBounds(12, 88, 71, 25);
		preframe.getContentPane().add(btnDown);
		
		JButton btnLeft = new JButton("LEFT");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveOneX(-5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnLeft.setFont(new Font("Dialog", Font.BOLD, 9));
		btnLeft.setBounds(257, 12, 71, 25);
		preframe.getContentPane().add(btnLeft);
		
		JButton btnRight = new JButton("RIGHT");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveOneX(5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnRight.setFont(new Font("Dialog", Font.BOLD, 9));
		btnRight.setBounds(340, 12, 63, 25);
		preframe.getContentPane().add(btnRight);
		
		
	}
	
	/**
	 * 
	 */
	private void createMicroAdjustSecondRow()
	{
		JButton btnUp_1 = new JButton("UP");
		btnUp_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveTwoY(-5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnUp_1.setFont(new Font("Dialog", Font.BOLD, 9));
		btnUp_1.setBounds(12, 324, 52, 25);
		preframe.getContentPane().add(btnUp_1);
		
		JButton btnDown_1 = new JButton("DOWN");
		btnDown_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveTwoY(5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnDown_1.setFont(new Font("Dialog", Font.BOLD, 9));
		btnDown_1.setBounds(12, 351, 71, 25);
		preframe.getContentPane().add(btnDown_1);
		
		JButton btnLeft_1 = new JButton("LEFT");
		btnLeft_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveTwoX(-5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnLeft_1.setFont(new Font("Dialog", Font.BOLD, 9));
		btnLeft_1.setBounds(257, 506, 71, 25);
		preframe.getContentPane().add(btnLeft_1);
		
		JButton btnRight_1 = new JButton("RIGHT");
		btnRight_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveTwoX(5);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnRight_1.setFont(new Font("Dialog", Font.BOLD, 9));
		btnRight_1.setBounds(340, 506, 63, 25);
		preframe.getContentPane().add(btnRight_1);
		
	}
	
	/**
	 * 
	 */
	public void setImgEdited(BufferedImage img)
	{
		this.imgJustEdited= this.resize(img, 608, 435);
		this.showImg();
	}
	
	/**
	 * 
	 */
	private void showImg()
	{
		this.lblImg = new JLabel("");
		lblImg.setIcon(new ImageIcon(this.imgJustEdited));
		lblImg.setBounds(114, 41, 608, 435);
		preframe.getContentPane().add(lblImg);
	}
	
	/**
	 * 
	 */
	private void saveButton()
	{
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.exportImage();
				editor.reset();
				preframe.setVisible(false);
			}
		});
		btnSave.setBounds(620, 504, 115, 25);
		preframe.getContentPane().add(btnSave);
	}
	
	/**
	 * Function to resize the image for preview.
	 * Function took from Stack overflow.
	 */
	public BufferedImage resize(BufferedImage img, int newWidth, int newHeight) { 
	    Image temporal = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(temporal, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	/**
	 * 
	 */
	private void rePaint(BufferedImage img)
	{
		this.imgJustEdited= this.resize(img, 608, 435);
		this.lblImg.setIcon(new ImageIcon(this.imgJustEdited));
	}
	
	/**
	 * 
	 */
	public void recieveEditor(Editor recievedEditor)
	{
		this.editor= recievedEditor;
	}
	
	/**
	 * 
	 */
	public void setOriginalPhoto(File photo)
	{
		this.photo= photo;
	}
}
