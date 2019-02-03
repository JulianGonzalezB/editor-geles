import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class View {

	private JFrame frame;
	private JComboBox<String> numberOfCols= null;
	private JComboBox<String> numberOfRows= null;
	private String messagePhoto= "  ...";
	private String messageLabels= "  ...";
	private JComboBox<String> orientationBox= null;
	private JComboBox<String> textColorBox= null;
	private JComboBox<String> fontSizeBox= null;
	private Editor editor= new Editor();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.setSize(640, 480);
		frame.setTitle("GELS EDITOR");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		this.createElements();
		
	}
	
	/**
	 * 
	 */
	private void createElements()
	{
		this.createColVariables();
		
		this.creatRowVariables();
		
		this.runEdition();
		
		this.loadPhoto();
		
		this.loadLabels();
		
		this.loadSaveLocation();
		
		this.textFormat();
		
		this.help();
	}
	
	/**
	 * 
	 */
	private void createColVariables()
	{
		JLabel labelCols = new JLabel("Columns in gel:");
		labelCols.setBounds(32, 208, 112, 28);
		labelCols.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frame.getContentPane().add(labelCols);
		
		this.numberOfCols = new JComboBox<String>();
		numberOfCols.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setNumberOfCols(Integer.parseInt(numberOfCols.getSelectedItem().toString()));
			}
		});
		numberOfCols.setFont(new Font("Tahoma", Font.PLAIN, 16));
		numberOfCols.setBounds(154, 209, 82, 26);
		this.numberOfCols.addItem("10");
		this.numberOfCols.addItem("13");
		this.numberOfCols.addItem("14");
		this.numberOfCols.addItem("16");
		this.numberOfCols.addItem("20");
		this.numberOfCols.addItem("21");
		frame.getContentPane().add(numberOfCols);
	}
	
	/**
	 * 
	 */
	private void creatRowVariables()
	{
		JLabel labelRows = new JLabel("    Rows in gel:");
		labelRows.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelRows.setBounds(32, 247, 112, 28);
		frame.getContentPane().add(labelRows);
		
		this.numberOfRows = new JComboBox<String>();
		numberOfRows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setNumberOfRows(Integer.parseInt(numberOfRows.getSelectedItem().toString()));
			}
		});
		numberOfRows.setFont(new Font("Tahoma", Font.PLAIN, 16));
		numberOfRows.setBounds(154, 249, 82, 24);
		this.numberOfRows.addItem("1");
		this.numberOfRows.addItem("2");
		frame.getContentPane().add(numberOfRows);
	}
	
	/**
	 * 
	 */
	private void loadPhoto()
	{
		JLabel lblLoadPhoto = new JLabel(this.messagePhoto);
		lblLoadPhoto.setBackground(Color.WHITE);
		lblLoadPhoto.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLoadPhoto.setBounds(165, 63, 332, 28);
		frame.getContentPane().add(lblLoadPhoto);
		lblLoadPhoto.setBorder(this.createBorder());
		lblLoadPhoto.setOpaque(true);
		
		JButton btnOpenPhoto = new JButton("Open Photo");
		btnOpenPhoto.setToolTipText("Selected photo must be .jpg or .JPEG image.");
		btnOpenPhoto.addActionListener(new ActionListener()
		{
				private JFileChooser openPhoto= new JFileChooser();

				public void actionPerformed(ActionEvent e) {
					this.openPhoto.setCurrentDirectory(new File("c:\\temp"));
					this.openPhoto.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg", "jpeg"));
					int ifOpen= this.openPhoto.showOpenDialog(frame);
					
					if (ifOpen == JFileChooser.APPROVE_OPTION)
					{
						try
						{
							lblLoadPhoto.setText(openPhoto.getSelectedFile().toString());
							editor.setPhoto(openPhoto.getSelectedFile());
						}
						catch (IOException ioe)
						{
							lblLoadPhoto.setText("  Faild to load Photo...");
						}
					}
					else
					{
						lblLoadPhoto.setText("  No file chosen...");
					}
				}
			
		});
		btnOpenPhoto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnOpenPhoto.setBounds(22, 63, 133, 28);
		frame.getContentPane().add(btnOpenPhoto);
	}
	
	/**
	 * 
	 */
	private void loadLabels()
	{
		JLabel lblLoadLabels = new JLabel(this.messageLabels);
		lblLoadLabels.setBackground(Color.WHITE);
		lblLoadLabels.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLoadLabels.setBounds(165, 102, 332, 28);
		frame.getContentPane().add(lblLoadLabels);
		lblLoadLabels.setBorder(this.createBorder());
		lblLoadLabels.setOpaque(true);
		
		JButton btnOpenLabels = new JButton("Open Labels");
		btnOpenLabels.setToolTipText("Selected file must be a .csv file");
		btnOpenLabels.addActionListener(new ActionListener() {
			
			private JFileChooser openLabels= new JFileChooser();
			
			public void actionPerformed(ActionEvent e) {
				this.openLabels.setCurrentDirectory(new File("c:\\temp"));
				this.openLabels.setFileFilter(new FileNameExtensionFilter( "CSV files (*csv)", "csv"));
				int ifOpen= this.openLabels.showOpenDialog(frame);
				
				if (ifOpen == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						lblLoadLabels.setText(openLabels.getSelectedFile().toString());
						editor.setLabels(openLabels.getSelectedFile());
					}
					catch (RuntimeException rte)
					{
						lblLoadLabels.setText("  Faild to load file...");
					}
				}
				else {
					lblLoadLabels.setText("  No file chosen...");
				}
			}
		});
		
		btnOpenLabels.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnOpenLabels.setBounds(22, 102, 133, 28);
		frame.getContentPane().add(btnOpenLabels);
	}
	
	/**
	 * 
	 */
	private void runEdition()
	{
		JButton buttonRunEdition = new JButton("RUN EDITION");
		buttonRunEdition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.edit();
			}
		});
		buttonRunEdition.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonRunEdition.setBounds(229, 380, 142, 28);
		frame.getContentPane().add(buttonRunEdition);
	}
	
	/**
	 * 
	 */
	private void textFormat()
	{
		JLabel lblTextColor = new JLabel("Text color:");
		lblTextColor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTextColor.setBounds(359, 205, 82, 34);
		frame.getContentPane().add(lblTextColor);
		
		JLabel lblFontSize = new JLabel("  Font size:");
		lblFontSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFontSize.setBounds(359, 251, 82, 20);
		frame.getContentPane().add(lblFontSize);
		
		JLabel lblOrientation = new JLabel("Orientation:");
		lblOrientation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOrientation.setBounds(207, 293, 89, 28);
		frame.getContentPane().add(lblOrientation);
		
		this.orientationBox = new JComboBox<String>();
		orientationBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setOrientation(orientationBox.getSelectedItem().toString());
			}
		});
		orientationBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		orientationBox.setBounds(294, 294, 103, 26);
		frame.getContentPane().add(orientationBox);
		this.orientationBox.addItem("Horizontal");
		this.orientationBox.addItem("Vertical");
		
		this.textColorBox = new JComboBox<String>();
		textColorBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setColorOfText(textColorBox.getSelectedItem().toString());
			}
		});
		textColorBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textColorBox.setBounds(451, 209, 68, 26);
		frame.getContentPane().add(textColorBox);
		this.textColorBox.addItem("White");
		this.textColorBox.addItem("Black");
		
		this.fontSizeBox = new JComboBox<String>();
		fontSizeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setFontSize(Integer.parseInt(fontSizeBox.getSelectedItem().toString()));
			}
		});
		fontSizeBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fontSizeBox.setBounds(451, 248, 68, 26);
		frame.getContentPane().add(fontSizeBox);
		this.fontSizeBox.addItem("9");
		this.fontSizeBox.addItem("10");
		this.fontSizeBox.addItem("11");
		this.fontSizeBox.addItem("12");
	}
	
	/**
	 * 
	 */
	private void help()
	{
		JButton btnHelp = new JButton("HELP");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Agregar que salga una ventana independiente con todo lo necesario para entender el programa
			}
		});
		btnHelp.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnHelp.setBounds(496, 29, 89, 23);
		frame.getContentPane().add(btnHelp);
		
	}
	
	/**
	 * 
	 */
	private Border createBorder()
	{
		Border border= BorderFactory.createLineBorder(Color.BLACK, 1);
		
		return(border);
	}
	
	/**
	 * 
	 */
	private void loadSaveLocation()
	{
		JLabel lblSaveLocation = new JLabel("  ...");
		lblSaveLocation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSaveLocation.setBackground(UIManager.getColor("Button.highlight"));
		lblSaveLocation.setBounds(165, 141, 332, 28);
		frame.getContentPane().add(lblSaveLocation);
		lblSaveLocation.setBorder(this.createBorder());
		lblSaveLocation.setOpaque(true);
		
		JButton btnSaveLocation = new JButton("Save Location");
		btnSaveLocation.addActionListener(new ActionListener() 
		{
			private JFileChooser location= new JFileChooser();
			
			public void actionPerformed(ActionEvent arg0) 
			{
				this.location.setCurrentDirectory(new File("c:\\temp"));
				this.location.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg", "jpeg"));
				int ifSelected= this.location.showSaveDialog(frame);
				
				if (ifSelected == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						lblSaveLocation.setText(location.getSelectedFile().toString());
						editor.setSaveLocation(location.getSelectedFile().toString());
					}
					catch (Exception ioe)
					{
						lblSaveLocation.setText("  You must selec a location");
					}
				}
				else {
					lblSaveLocation.setText("  No location chosen...");
				}
			}
		});
		btnSaveLocation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSaveLocation.setBounds(22, 141, 133, 28);
		frame.getContentPane().add(btnSaveLocation);
	}
}
