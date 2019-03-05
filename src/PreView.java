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

/**
 * Class for  presenting the edited image to the user
 * And provides him/her a way to adjust details in the text position
 * This class is instantiated by the class View
 * @author julian
 *
 */
public class PreView 
{
	//Window
	private JFrame preframe;
	//Image edited by the editor
	private BufferedImage imgJustEdited= null;
	//Editor
	private Editor editor= null;
	private JLabel lblImg= null;
	//Original photo
	private File photo= null;
	
	/**
	 * Builder of the class
	 * @wbp.parser.entryPoint
	 */
	public void PreView()
	{
		//Creates the window
		preframe = new JFrame();
		preframe.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		preframe.setSize(768, 680);
		preframe.setTitle("GELS EDITOR PREVIEW");
		preframe.setLocationRelativeTo(null);
		preframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		preframe.getContentPane().setLayout(null);
		this.createMicroAdjustFirstRow();
		this.createMicroAdjustSecondRow();
		this.createMicroAdjustInfo();
		this.saveButton();
		this.preframe.setVisible(true);
	}
	
	/**
	 * Method that creates the elements involved in the movement of the first row in both axis
	 * passing the action to the editor for re-edit
	 * @return void
	 */
	private void createMicroAdjustFirstRow()
	{
		JLabel lblSuperiorLabels = new JLabel("Superior Labels");
		lblSuperiorLabels.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSuperiorLabels.setBounds(16, 15, 126, 15);
		preframe.getContentPane().add(lblSuperiorLabels);
		
		//Moves the row upwards
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
		
		//Moves the row downwards
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
		
		//Moves the row leftwards
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
		
		//Moves the row rightwards
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
	 * Method that creates the elements involved in the movement of the second row in both axis
	 * passing the action to the editor for re-edit
	 * @return void
	 */
	private void createMicroAdjustSecondRow()
	{
		JLabel lblInferiorLabels = new JLabel("Inferior Labels");
		lblInferiorLabels.setFont(new Font("Dialog", Font.BOLD, 14));
		lblInferiorLabels.setBounds(12, 597, 130, 15);
		preframe.getContentPane().add(lblInferiorLabels);
		
		//Moves the row upwards
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
		btnUp_1.setBounds(12, 480, 52, 25);
		preframe.getContentPane().add(btnUp_1);
		
		//Moves the row downwards
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
		btnDown_1.setBounds(12, 504, 71, 25);
		preframe.getContentPane().add(btnDown_1);
		
		//Moves the row leftwards
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
		btnLeft_1.setBounds(257, 587, 71, 25);
		preframe.getContentPane().add(btnLeft_1);
		
		//Moves the row rightwards
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
		btnRight_1.setBounds(340, 587, 63, 25);
		preframe.getContentPane().add(btnRight_1);
		
	}
	
	/**
	 * Method that creates the elements involved in the movement of the info text in both axis
	 * passing the action to the editor for re-edit
	 * @return void
	 */
	private void createMicroAdjustInfo()
	{
		JLabel lblInfoText = new JLabel("Info Text");
		lblInfoText.setFont(new Font("Dialog", Font.BOLD, 14));
		lblInfoText.setBounds(16, 227, 83, 15);
		preframe.getContentPane().add(lblInfoText);
		
		//Moves the info upwards
		JButton btnUp_2 = new JButton("UP");
		btnUp_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveInfoY(-10);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnUp_2.setFont(new Font("Dialog", Font.BOLD, 7));
		btnUp_2.setBounds(31, 257, 52, 25);
		preframe.getContentPane().add(btnUp_2);
		
		//Moves the info leftwards
		JButton btnLeft_2 = new JButton("LEFT");
		btnLeft_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.moveInfoX(-10);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnLeft_2.setFont(new Font("Dialog", Font.BOLD, 7));
		btnLeft_2.setBounds(12, 283, 52, 25);
		preframe.getContentPane().add(btnLeft_2);
		
		//Moves the info rightwards
		JButton btnRight_2 = new JButton("RIGHT");
		btnRight_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveInfoX(10);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnRight_2.setFont(new Font("Dialog", Font.BOLD, 7));
		btnRight_2.setBounds(66, 283, 63, 25);
		preframe.getContentPane().add(btnRight_2);
		
		//Moves the info downwards
		JButton btnDown_2 = new JButton("DOWN");
		btnDown_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.moveInfoY(10);
				try {
					editor.setPhoto(photo);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				rePaint(editor.reEdit());
			}
		});
		btnDown_2.setFont(new Font("Dialog", Font.BOLD, 7));
		btnDown_2.setBounds(31, 308, 64, 25);
		preframe.getContentPane().add(btnDown_2);
	}
	
	/**
	 * Method that sets the edited image for show it to the user in the preview window
	 * @param img is a BufferedImage is the image edited by the editor
	 * @return void
	 */
	public void setImgEdited(BufferedImage img)
	{
		this.imgJustEdited= this.resize(img, 608, 525);
		this.showImg();
	}
	
	/**
	 * Method that puts the image in the window using a JLabel
	 * @return void
	 */
	private void showImg()
	{
		this.lblImg = new JLabel("");
		lblImg.setIcon(new ImageIcon(this.imgJustEdited));
		lblImg.setBounds(135, 41, 608, 525);
		preframe.getContentPane().add(lblImg);
	}
	
	/**
	 * Method that save the image as an order from the user
	 * calls the method Export of the editor and the method Reset
	 * @return void
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
		btnSave.setBounds(619, 585, 115, 25);
		preframe.getContentPane().add(btnSave);
		
	}
	
	/**
	 * Method to resize the image for preview.
	 * Method took from Stack overflow.
	 * @param img is a BufferedImage to resize
	 * @param newWidth is an integer for the new width of the image
	 * @param newHeight is an integer for the new height of the image
	 * @return BufferedImage resized
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
	 * Method for repainting the image presented
	 * @param img is a BufferedImage to present
	 * @return void
	 */
	private void rePaint(BufferedImage img)
	{
		this.imgJustEdited= this.resize(img, 608, 525);
		this.lblImg.setIcon(new ImageIcon(this.imgJustEdited));
	}
	
	/**
	 * Method for receiving the editor from the View
	 * @param receivedEditor is the editor
	 * @return void
	 */
	public void receiveEditor(Editor receivedEditor)
	{
		this.editor= receivedEditor;
	}
	
	/**
	 * Method to receive the original, non-edited, photo
	 * @param photo is the file.JPEG selected by the user
	 * @return void
	 */
	public void setOriginalPhoto(File photo)
	{
		this.photo= photo;
	}
}
