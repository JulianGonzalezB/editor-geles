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
/**
 * This is the main class. This class is a View-Controller
 * Creates the main window, receives the files (Photo and csv) and the features of the text
 * To be printed, pass all to the editor and the preview.
 * Has an editor and a PreView.
 * @author julian
 *
 */
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
	 * It confirms if the security folder exists to set visible the main window.
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
	 * Initialize the contents of the main window.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.setSize(768, 480);
		frame.setTitle("GELS EDITOR");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Call the creation of the components.
		this.createElements();
		
	}
	
	/**
	 *Method to create all the components in the main window
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
	 * Method that creates the label and ComboBox for the selection of number of columns in the gel.
	 */
	private void createColVariables()
	{
		//Creates the label
		JLabel labelCols = new JLabel("Columns in gel:");
		labelCols.setBounds(32, 208, 112, 28);
		labelCols.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frame.getContentPane().add(labelCols);
		
		//Creates the JComboBox
		this.numberOfCols = new JComboBox<String>();
		numberOfCols.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setNumberOfCols(Integer.parseInt(numberOfCols.getSelectedItem().toString()));
			}
		});
		
		//Sets the options to be displayed in the ComboBox.
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
	 * Method that creates the label and ComboBox for the selection of number of rows in the gel.
	 */
	private void creatRowVariables()
	{
		//Creates the label
		JLabel labelRows = new JLabel("    Rows in gel:");
		labelRows.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelRows.setBounds(32, 247, 112, 28);
		frame.getContentPane().add(labelRows);
		
		//Creates the JComboBox
		this.numberOfRows = new JComboBox<String>();
		numberOfRows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setNumberOfRows(Integer.parseInt(numberOfRows.getSelectedItem().toString()));
			}
		});
		
		//Sets the options to be displayed in the ComboBox
		numberOfRows.setFont(new Font("Tahoma", Font.PLAIN, 16));
		numberOfRows.setBounds(154, 249, 82, 24);
		this.numberOfRows.addItem("1");
		this.numberOfRows.addItem("2");
		frame.getContentPane().add(numberOfRows);
	}
	
	/**
	 * Method to create all the elements involved in the
	 * loading of the photo and the loading itself.
	 */
	private void loadPhoto()
	{
		//Creates the Jlabel. The text of the label change according to if
		//A photo was selected or not.
		JLabel lblLoadPhoto = new JLabel(this.messagePhoto);
		lblLoadPhoto.setBackground(Color.WHITE);
		lblLoadPhoto.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLoadPhoto.setBounds(165, 63, 414, 28);
		frame.getContentPane().add(lblLoadPhoto);
		lblLoadPhoto.setBorder(this.createBorder());
		lblLoadPhoto.setOpaque(true);
		
		//Creates the button to select the photo.
		JButton btnOpenPhoto = new JButton("Open Photo");
		btnOpenPhoto.setToolTipText("Selected photo must be .jpg or .JPEG image.");
		btnOpenPhoto.addActionListener(new ActionListener()
		{
				private JFileChooser openPhoto= new JFileChooser();

				public void actionPerformed(ActionEvent e) {
					
					//Sets the initial directory
					this.openPhoto.setCurrentDirectory(new File("c:\\temp"));
					//Sets the filter
					this.openPhoto.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg", "jpeg"));
					int ifOpen= this.openPhoto.showOpenDialog(frame);
					
					//If the selection of the photo is valid then change the text of the label
					//And pass the photo to the editor and the preview.
					//The photo is passed to the preview for allowing to change the photo in the preview and not to overdraw the edition
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
		
		//Sets the details of the button
		btnOpenPhoto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnOpenPhoto.setBounds(22, 63, 133, 28);
		frame.getContentPane().add(btnOpenPhoto);
	}
	
	/**
	 * Method to create all the elements involved in the
	 * loading of the labels in the .csv file
	 * and the loading itself.
	 */
	private void loadLabels()
	{
		//Creates the Jlabel. The text of the label change according to if
		//A .csv was selected or not.
		JLabel lblLoadLabels = new JLabel(this.messageLabels);
		lblLoadLabels.setBackground(Color.WHITE);
		lblLoadLabels.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLoadLabels.setBounds(165, 102, 414, 28);
		frame.getContentPane().add(lblLoadLabels);
		lblLoadLabels.setBorder(this.createBorder());
		lblLoadLabels.setOpaque(true);
		
		//Creates the button for selecting a .csv file.
		JButton btnOpenLabels = new JButton("Open Labels");
		btnOpenLabels.setToolTipText("Selected file must be a .csv file");
		btnOpenLabels.addActionListener(new ActionListener() {
			
			private JFileChooser openLabels= new JFileChooser();
			
			public void actionPerformed(ActionEvent e) {
				
				//Sets the initial directory
				this.openLabels.setCurrentDirectory(new File("c:\\temp"));
				this.openLabels.setFileFilter(new FileNameExtensionFilter( "CSV files (*csv)", "csv"));
				int ifOpen= this.openLabels.showOpenDialog(frame);
				
				//If the file was successfully selected
				if (ifOpen == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						//Change the text of the label and pass the .csv file
						//To the editor. This occurs just one time.
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
		
		//Sets the features of the button
		btnOpenLabels.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnOpenLabels.setBounds(22, 102, 133, 28);
		frame.getContentPane().add(btnOpenLabels);
	}
	
	/**
	 * Method to run the edition. This method must be called only when both
	 * the photo and the csv files have been already successfully selected.
	 * Features of the text to be printed in the photo and similar are not needed.
	 */
	private void runEdition()
	{
		//Creates the RunEdition button
		JButton buttonRunEdition = new JButton("RUN EDITION");
		buttonRunEdition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//When the button is pressed a PreView is created. The PreView receives
				//The editor and the original photo in order to be able to modify the edited photo.
				preview= new PreView();
				preview.PreView();
				preview.receiveEditor(editor);
				preview.setOriginalPhoto(photo);
				preview.setImgEdited(editor.edit());
				
			}
		});
		
		//Sets the features of the button
		buttonRunEdition.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonRunEdition.setBounds(335, 380, 142, 28);
		frame.getContentPane().add(buttonRunEdition);
	}
	
	/**
	 * Method that creates all the elements involved in the format
	 * of the text to be printed in the photo.
	 */
	private void textFormat()
	{
		//Label of the text color
		JLabel lblTextColor = new JLabel("Text color:");
		lblTextColor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTextColor.setBounds(359, 205, 82, 34);
		frame.getContentPane().add(lblTextColor);
		
		//Label of the Font Size
		JLabel lblFontSize = new JLabel("  Font size:");
		lblFontSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFontSize.setBounds(359, 251, 82, 20);
		frame.getContentPane().add(lblFontSize);
		
		//Label of the Orientation of the information
		JLabel lblOrientation = new JLabel("Info. Orientation:");
		lblOrientation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOrientation.setBounds(21, 283, 134, 28);
		frame.getContentPane().add(lblOrientation);
		
		//When an orientation is selected the global variable change in the editor
		this.orientationBox = new JComboBox<String>();
		orientationBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setOrientation(orientationBox.getSelectedItem().toString());
			}
		});
		
		//Features and options for Orientation
		orientationBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		orientationBox.setBounds(154, 284, 103, 26);
		frame.getContentPane().add(orientationBox);
		this.orientationBox.addItem("Vertical");
		this.orientationBox.addItem("Horizontal");
		
		//When a color is selected the global variable change in the editor
		this.textColorBox = new JComboBox<String>();
		textColorBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setColorOfText(textColorBox.getSelectedItem().toString());
			}
		});
		
		//Features and options for text color
		textColorBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textColorBox.setBounds(451, 209, 68, 26);
		frame.getContentPane().add(textColorBox);
		this.textColorBox.addItem("White");
		this.textColorBox.addItem("Black");
		
		//When a font size is selected the global variable change in the editor
		this.fontSizeBox = new JComboBox<String>();
		fontSizeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setFontSize(Integer.parseInt(fontSizeBox.getSelectedItem().toString()));
			}
		});
		
		//Features and options for Font size
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
		
		//Label of the font size of the info.
		JLabel lblInfoFontSize = new JLabel("Info. Font size:");
		lblInfoFontSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInfoFontSize.setBounds(329, 286, 112, 23);
		frame.getContentPane().add(lblInfoFontSize);
		
		//When a font size is selected the global variable change in the editor
		this.infoSizeBox= new JComboBox();
		infoSizeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editor.setInfoSize(Integer.parseInt(infoSizeBox.getSelectedItem().toString()));
			}
		});
		
		//Features and options for the font size of the info
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
	 * Method that displays the help when the respective button is pressed
	 */
	private void help()
	{
		//Creates the button
		JButton btnHelp = new JButton("HELP");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Agregar que salga una ventana independiente con todo lo necesario para entender el programa
			}
		});
		
		//Features of the button
		btnHelp.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnHelp.setBounds(653, 11, 89, 23);
		frame.getContentPane().add(btnHelp);
		
	}
	
	/**
	 * Method to create a border to be used in the labels that says if
	 * the files were successfully selected
	 */
	private Border createBorder()
	{
		Border border= BorderFactory.createLineBorder(Color.BLACK, 1);
		
		return(border);
	}
	
	/**
	 * Method for giving the csv model to the user when the buttons is pressed.
	 * The location of the file to be created needs to be selcted by the user.
	 */
	private void creatGetCSVButton()
	{
		//Creates the button
		JButton btnGetCsvModel = new JButton("Get CSV Model");
		btnGetCsvModel.addActionListener(new ActionListener()
		{
			private JFileChooser getModel= new JFileChooser();
			
			public void actionPerformed(ActionEvent arg0)
			{
				//Sets the default directory
				this.getModel.setCurrentDirectory(new File("c:\\temp"));
				this.getModel.setFileFilter(new FileNameExtensionFilter("CSV files (*csv)", "csv"));
				int ifSelected= this.getModel.showSaveDialog(frame);
				
				//If a location was selected
				if (ifSelected == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						//Reads the csv file of the resource
						InputStream inputStream = View.class.getResourceAsStream("/resources/model.csv");
						
						Scanner scanner= new Scanner(inputStream);
						scanner.useDelimiter("\\n");
						File modelCsv= new File(this.getModel.getSelectedFile().toString());
						
						BufferedWriter writer = new BufferedWriter(new FileWriter(modelCsv));
						String line= null;
						
						while(scanner.hasNextLine())
						{
							//Read each line and write it in the new file
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
		
		//Features of the button
		btnGetCsvModel.setBounds(22, 10, 154, 25);
		frame.getContentPane().add(btnGetCsvModel);
	}
	
	/**
	 * Method for selecting the location where the edited photo is going to be saved
	 */
	private void loadSaveLocation()
	{
		//Creates the label
		JLabel lblSaveLocation = new JLabel("  ...");
		lblSaveLocation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSaveLocation.setBackground(UIManager.getColor("Button.highlight"));
		lblSaveLocation.setBounds(165, 141, 414, 28);
		frame.getContentPane().add(lblSaveLocation);
		lblSaveLocation.setBorder(this.createBorder());
		lblSaveLocation.setOpaque(true);
		
		//Creates the button
		JButton btnSaveLocation = new JButton("Save Location");
		btnSaveLocation.addActionListener(new ActionListener() 
		{
			private JFileChooser location= new JFileChooser();
			
			public void actionPerformed(ActionEvent arg0) 
			{
				//Sets the default directory
				this.location.setCurrentDirectory(new File("c:\\temp"));
				this.location.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg", "jpeg"));
				int ifSelected= this.location.showSaveDialog(frame);
				
				//If a valid location was selected
				if (ifSelected == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						//The location is passed to the editor
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
		
		//Features of the button
		btnSaveLocation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSaveLocation.setBounds(22, 141, 133, 28);
		frame.getContentPane().add(btnSaveLocation);
	}
	
	/**
	 * Method that check if this is the first time the program is going to be used.
	 * If it is, the program will ask for a key to allow the use of the system.
	 * If it is not, the program runs automatically.
	 */
	private  void checkIfFirstTime()
	{
		Path path= Paths.get("C:\\Gels_editor");
		
		//Chek if the directory Gels_editor already exists
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
	 * Method that creates the security window.
	 * Receives a key that posteriorly is compared with the local key
	 */
	private void keyWindow()
	{
		//Creates the security window
		this.keyWindow= new JFrame();
		this.keyWindow.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 20));
		this.keyWindow.setSize(390, 180);
		this.keyWindow.setTitle("KEY NEEDED TO INSTALL GELS EDITOR");
		this.keyWindow.setLocationRelativeTo(null);
		this.keyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.keyWindow.getContentPane().setLayout(null);
		
		//Creates the label
		JLabel lblKeyLabel = new JLabel("Insert the key:");
		lblKeyLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblKeyLabel.setToolTipText("");
		lblKeyLabel.setBounds(10, 47, 98, 19);
		keyWindow.getContentPane().add(lblKeyLabel);
		
		txtKey = new JTextField();
		txtKey.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) {
				//Verify if the inserted key is correct
				isKeyCorrect(txtKey.getText());
			}
		});
		
		//Features of the security window
		txtKey.setBounds(107, 48, 98, 20);
		keyWindow.getContentPane().add(txtKey);
		txtKey.setColumns(10);
		
		//Makes visible the security window
		this.keyWindow.setVisible(true);
		this.keyWindow.toFront();
		this.keyWindow.repaint();
	}
	
	/**
	 * Method that verify if the inserted key is equals to the local key
	 */
	private void isKeyCorrect(String insertedKey)
	{
		if(this.localKey.equals(insertedKey))
		{
			//If the inserted key is correct all permission are given
			this.permission= true;
			this.createNewFolder();
			this.keyWindow.setVisible(false);
			this.frame.setVisible(true);
		}
	}
	
	/**
	 * Method that creates the folder Gels_Editor  when the system is install for
	 * First time.
	 */
	private void createNewFolder()
	{
		Path path = Paths.get("C:\\Gels_editor");
		try {
			//Creates the directory
			Files.createDirectory(path);
			//Hid the directory
			Files.setAttribute(path, "dos:hidden", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
