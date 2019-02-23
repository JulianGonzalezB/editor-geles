import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class View {

	private JFrame frame;
	
	//Variables that describe the gels
	private JComboBox<String> numberOfCols= null;
	private JComboBox<String> numberOfRows= null;
	
	//Initial text of photo and labels
	private String messagePhoto= "  ...";
	private String messageLabels= "  ...";
	
	//Visual variables of the text in the gel
	private JComboBox<String> orientationBox= null;
	private JComboBox<String> textColorBox= null;
	private JComboBox<String> fontSizeBox= null;
	private JComboBox<String> infoSizeBox= null;
	
	//Edition and preview
	private Editor editor= new Editor();
	private PreView preview= null;
	private File photo= null;
	
	//Variables for security
	private JFrame keyWindow;
	private String localKey= "XZF2#thOwe789";
	private boolean permission= false;
	private JTextField txtKey;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(false);
					Path path= Paths.get("C:\\Gels_editor");
					if(Files.exists(path))
					{
						window.frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the main window and check the license.
	 */
	public View()
	{
		this.initialize();
		this.checkIfFirstTime();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.setSize(768, 480);
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
		this.creatGetCSVButton();
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
		lblLoadPhoto.setBounds(165, 63, 414, 28);
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
							photo= openPhoto.getSelectedFile();
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
		lblLoadLabels.setBounds(165, 102, 414, 28);
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
				
				preview= new PreView();
				preview.PreView();
				preview.recieveEditor(editor);
				preview.setOriginalPhoto(photo);
				preview.setImgEdited(editor.edit());
				
			}
		});
		buttonRunEdition.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonRunEdition.setBounds(335, 380, 142, 28);
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
		
		JLabel lblOrientation = new JLabel("Info. Orientation:");
		lblOrientation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOrientation.setBounds(21, 283, 134, 28);
		frame.getContentPane().add(lblOrientation);
		
		this.orientationBox = new JComboBox<String>();
		orientationBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setOrientation(orientationBox.getSelectedItem().toString());
			}
		});
		orientationBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		orientationBox.setBounds(154, 284, 103, 26);
		frame.getContentPane().add(orientationBox);
		this.orientationBox.addItem("Vertical");
		this.orientationBox.addItem("Horizontal");
		
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
		this.fontSizeBox.addItem("5");
		this.fontSizeBox.addItem("10");
		this.fontSizeBox.addItem("15");
		this.fontSizeBox.addItem("20");
		this.fontSizeBox.addItem("25");
		this.fontSizeBox.addItem("30");
		this.fontSizeBox.addItem("35");
		this.fontSizeBox.addItem("40");
		this.fontSizeBox.addItem("50");
		this.fontSizeBox.addItem("60");
		this.fontSizeBox.addItem("70");
		this.fontSizeBox.addItem("80");
		
		JLabel lblInfoFontSize = new JLabel("Info. Font size:");
		lblInfoFontSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInfoFontSize.setBounds(329, 286, 112, 23);
		frame.getContentPane().add(lblInfoFontSize);
		
		this.infoSizeBox= new JComboBox();
		infoSizeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setInfoSize(Integer.parseInt(infoSizeBox.getSelectedItem().toString()));
			}
		});
		infoSizeBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		infoSizeBox.setBounds(451, 286, 68, 26);
		frame.getContentPane().add(this.infoSizeBox);
		this.infoSizeBox.addItem("5");
		this.infoSizeBox.addItem("10");
		this.infoSizeBox.addItem("15");
		this.infoSizeBox.addItem("20");
		this.infoSizeBox.addItem("25");
		this.infoSizeBox.addItem("30");
		this.infoSizeBox.addItem("35");
		this.infoSizeBox.addItem("40");
		this.infoSizeBox.addItem("50");
		this.infoSizeBox.addItem("60");
		this.infoSizeBox.addItem("70");
		this.infoSizeBox.addItem("80");
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
		btnHelp.setBounds(653, 11, 89, 23);
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
	private void creatGetCSVButton()
	{
		JButton btnGetCsvModel = new JButton("Get CSV Model");
		btnGetCsvModel.addActionListener(new ActionListener()
		{
			private JFileChooser getModel= new JFileChooser();
			
			public void actionPerformed(ActionEvent arg0)
			{
				this.getModel.setCurrentDirectory(new File("c:\\temp"));
				this.getModel.setFileFilter(new FileNameExtensionFilter("CSV files (*csv)", "csv"));
				int ifSelected= this.getModel.showSaveDialog(frame);
				
				if (ifSelected == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						InputStream inputStream = View.class.getResourceAsStream("/resources/model.csv");
						
						Scanner scanner= new Scanner(inputStream);
						scanner.useDelimiter("\\n");
						File modelCsv= new File(this.getModel.getSelectedFile().toString());
						
						BufferedWriter writer = new BufferedWriter(new FileWriter(modelCsv));
						String line= null;
						
						while(scanner.hasNextLine())
						{
							line= scanner.nextLine();
							writer.write(line);
							writer.newLine();
						}
						
						scanner.close();
						writer.close();
					}
					catch (Exception ioe)
					{
						
					}
				}
			}
		});
		btnGetCsvModel.setBounds(22, 10, 154, 25);
		frame.getContentPane().add(btnGetCsvModel);
	}
	
	/**
	 * 
	 */
	private void loadSaveLocation()
	{
		JLabel lblSaveLocation = new JLabel("  ...");
		lblSaveLocation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSaveLocation.setBackground(UIManager.getColor("Button.highlight"));
		lblSaveLocation.setBounds(165, 141, 414, 28);
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
						lblSaveLocation.setText("  You must selec Name and Location");
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
	
	/**
	 * 
	 */
	private  void checkIfFirstTime()
	{
		Path path= Paths.get("C:\\Gels_editor");
		if(Files.notExists(path))
		{
			this.keyWindow();
		}
		else
		{
			this.permission= true;
		}
	}
	
	/**
	 * 
	 */
	private void keyWindow()
	{
		this.keyWindow= new JFrame();
		this.keyWindow.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 20));
		this.keyWindow.setSize(390, 180);
		this.keyWindow.setTitle("KEY NEEDED TO INSTALL GELS EDITOR");
		this.keyWindow.setLocationRelativeTo(null);
		this.keyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.keyWindow.getContentPane().setLayout(null);
		
		JLabel lblKeyLabel = new JLabel("Insert the key:");
		lblKeyLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblKeyLabel.setToolTipText("");
		lblKeyLabel.setBounds(10, 47, 98, 19);
		keyWindow.getContentPane().add(lblKeyLabel);
		
		txtKey = new JTextField();
		txtKey.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) {
				isKeyCorrect(txtKey.getText());
			}
		});
		txtKey.setBounds(107, 48, 98, 20);
		keyWindow.getContentPane().add(txtKey);
		txtKey.setColumns(10);
		this.keyWindow.setVisible(true);
		this.keyWindow.toFront();
		this.keyWindow.repaint();
	}
	
	/**
	 * 
	 */
	private void isKeyCorrect(String insertedKey)
	{
		if(this.localKey.equals(insertedKey))
		{
			this.permission= true;
			this.createNewFolder();
			this.keyWindow.setVisible(false);
			this.frame.setVisible(true);
		}
	}
	
	/**
	 * 
	 */
	private void createNewFolder()
	{
		Path path = Paths.get("C:\\Gels_editor");
		try {
			Files.createDirectory(path);
			Files.setAttribute(path, "dos:hidden", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
