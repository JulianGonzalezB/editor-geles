import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class that performs the actual edition over the loaded photo
 * with the information and labels given in the csv file.
 * This class is instantiated one single time but the edition
 * could run more than one time.
 * The first run is an order by the View object (View-Controller)
 * And all subsequent runs if there are are ordered by the preview.
 * @author julian
 *
 */
public class Editor {
	
	//Image provided by the user and edited image
	private BufferedImage originalBI= null;
	private BufferedImage imageToEdit= null;
	
	//Features of the gel
	private int numberOfCols= 10;
	private int numberOfRows= 1;
	
	//Text format
	private String colorOfText= "White";
	private String infOrientation= "Vertical";
	private int fontSize= 5;
	private int infoSize= 5;
	
	//About the csv file
	private File labelsCSV= null;
	private String [][] dataFromLabels= null;
	private int numberOfColsInFile= 2;
	private int numberOfInfoRows= 5;
	
	//Variables for adjusting text from the preview
	////For the first row of labels
	private int superiorX= 0;
	private int superiorY= 0;
	////For the second row of labels
	private int inferiorX= 0;
	private int inferiorY= 0;
	////For the Info text
	private int infoY= 0;
	private int infoX= 0;
	
	//If there any error
	private String errorMessage= null;
	private JFrame errorWindow= null;
	
	//For the labels to be in horizontal if selected
	private String concatenatedInfo= null;
	
	//For save the edited photo
	private String saveLocationSelected= null;
	
	/**
	 * Method for loading the photo file in the editor
	 * @param photo is a .JPEG File
	 * @return void
	 * @throws IOException 
	 */
	public void setPhoto(File photo) throws IOException
	{
		this.originalBI= ImageIO.read(photo);
	}
	
	/**
	 * Method for loading the csv file with the labels 
	 * @param labels is a .csv File comma separated.
	 * @return void
	 */
	public void setLabels(File labels)
	{
		this.labelsCSV= labels;
	}
	
	/**
	 * Method that sets the number of columns per row in the gel
	 * @param int numberOfCols is an integer
	 * Default is 10
	 * @return void
	 */
	public void setNumberOfCols(int numberOfCols)
	{
		this.numberOfCols= numberOfCols;
	}
	
	/**
	 * Method that sets the number of rows in the gel
	 * @param numberOfRows is an integer
	 * Default is 1
	 * @return void
	 */
	public void setNumberOfRows(int numberOfRows)
	{
		this.numberOfRows= numberOfRows;
	}
	
	/**
	 * Method that sets the color of the text to print in the photo
	 * @param colorOfText is a String and could be White or Black
	 * Default is White
	 * @return void
	 */
	public void setColorOfText(String colorOfText)
	{
		this.colorOfText= colorOfText;
	}
	
	/**
	 * Method that sets the orientation of the info text
	 * @param orientation is a String and could be Vertical or Horizontal
	 * Default is Vertical
	 * @return void
	 */
	public void setOrientation(String orientation)
	{
		this.infOrientation= orientation;
	}
	
	/**
	 * Method that sets the font size
	 * @param fontSize is an integer
	 * default is 5
	 * @return void
	 */
	public void setFontSize(int fontSize)
	{
		this.fontSize= fontSize;
	}
	
	/**
	 * Method that sets the font size of the info text
	 * @param infoSize is an integer
	 * Default is 5
	 * @return void
	 */
	public void setInfoSize(int infoSize)
	{
		this.infoSize= infoSize;
	}
	
	/**
	 * Method that run the method that make the actual edition of the image
	 * @return BufferedImage that is the edited image to be used by the preview
	 */
	public BufferedImage edit()
	{
		this.readLabels();
		this.writeLabels();
		
		return this.imageToEdit;
	}
	
	/**
	 * Method that reads the info an labels from the csv file
	 * @return void
	 */
	private void readLabels()
	{
		try
		{
			//Open the file
			Scanner scanner= new Scanner(this.labelsCSV);
			scanner.useDelimiter("\\n");
			int row= 0;
			String inputLine= null;
			this.dataFromLabels= new String[this.numberOfRows * (this.numberOfCols) + this.numberOfInfoRows + 1][this.numberOfColsInFile];
			
			//Read each line and split it by comma
			while (scanner.hasNextLine())
			{
				inputLine= scanner.nextLine();
				String[] wordsArray= inputLine.split(",");
				
				if(wordsArray.length != 2)
				{
					//If info id added to the file but not needed to be printed in the photo
					String[] definitiveArray= new String[2];
					definitiveArray[0]= wordsArray[0];
					definitiveArray[1]= wordsArray[1];
					wordsArray= definitiveArray;
				}
				
				//When everything is normal (2 columns)
				for (int word= 0; word < wordsArray.length; word++)
				{
					//Copy each element into an array
					this.dataFromLabels[row][word]= wordsArray[word];
				}
				row++;
			}
			
			//When the info orientation is Horizontal
			if (this.dataFromLabels[0][1].equals("PCR") || this.dataFromLabels[0][1].equals("pcr") || this.dataFromLabels[0][1].equals("Pcr"))
			{
				this.dataFromLabels[2][0]= "Primer:";
			}
			else
			{
				this.dataFromLabels[2][0]= "Protocolo:";
			}
			
			this.concatenateInfo();
			scanner.close();
			
		}
		catch (FileNotFoundException e)
		{
			//If the reading fails
			this.errorMessage= "Problems with .csv file. Please try again";
			this.showErrorWindow();
		}
	}
	
	/**
	 * Method that write the labels and text info in the photo
	 * @return void
	 */
	private void writeLabels()
	{
		try
		{
			//Makes a copy of the image
			this.imageToEdit= this.originalBI;
			//Gets the size of the image
			int height= this.imageToEdit.getHeight();
			int width= this.imageToEdit.getWidth();
			
			Graphics graphics= this.imageToEdit.getGraphics();
			//Number of pixels of a segment (column)
			int XSegment= width / this.numberOfCols;
			
			//Color of the text
			if(this.colorOfText.equals("White"))
			{
				graphics.setColor(Color.WHITE);
			}
			else
			{
				graphics.setColor(Color.BLACK);
			}
			
			//Draw the info of the gel
			graphics.setFont(new Font("Arial Black", Font.PLAIN, this.infoSize));
			
			//If Horizontal is selected
			if(this.infOrientation.equals("Horizontal"))
			{
				graphics.drawString(this.concatenatedInfo, width / 17, 24 * height / 25);
			}
			else
			{
				//If vertical is selected
				for(int row= 0; row < this.numberOfInfoRows; row++)
				{
					graphics.drawString(this.dataFromLabels[row][1], width / 27 + this.infoX, (height / 2 ) + row * (this.infoSize + 20) + this.infoY);
				}
			}
			
			graphics.setFont(new Font("Arial Black", Font.PLAIN, this.fontSize));
			
			//Draw the labels in the holes
			for(int row= 0; row < this.numberOfCols; row++)
			{
				for(int col= 0; col < this.numberOfColsInFile; col++)
				{
					//If the field is not empty in the gel
					if(!this.dataFromLabels[row + 6][col].contentEquals("NA"))
					{
						graphics.drawString(this.dataFromLabels[row + 6][col], (row) * XSegment + XSegment/4 + this.superiorX, (height / 7 - 45) + col * (this.fontSize + 5) + this.superiorY);
					}
					
				}
			}
			
			//If there are two rows in the gel
			if(this.numberOfRows == 2)
			{
				for(int row= 0; row < this.numberOfCols; row++)
				{
					for(int col= 0; col < this.numberOfColsInFile; col++)
					{
						//If the field is not empty in the gel
						if(!this.dataFromLabels[row + 6 + this.numberOfCols][col].equals("NA"))
						{
							graphics.drawString(this.dataFromLabels[row + 6 + this.numberOfCols][col], (row) * XSegment + XSegment/4 + this.inferiorX, (2 * (height / 3) + 40) + col * (this.fontSize + 5) + this.inferiorY);
						}
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.err.println(e);
			this.errorMessage= "ERROR IN EDITING IMAGE";
			this.showErrorWindow();
		}
	}
	
	/**
	 * Method that moves the first row In the X axis
	 * @param distance is an integer is the number of pixels the text is going to move
	 * This method is executed by the preview
	 * @return void
	 */
	public void moveOneX(int distance)
	{
		this.superiorX= this.superiorX + distance;
	}
	
	/**
	 * Method that moves the first row in the Y axis
	 * @param distance is an integer is the number of pixels the text is going to move
	 * This method is executed by the preview
	 * @return void
	 */
	public void moveOneY(int distance)
	{
		this.superiorY= this.superiorY + distance;
	}
	
	/**
	 * Method that moves the second row in the X axis
	 * @param distance is an integer is the number of pixels the text is going to move
	 * This method is executed by the preview
	 * @return void
	 */
	public void moveTwoX(int distance)
	{
		this.inferiorX= this.inferiorX + distance;
	}
	
	/**
	 * Method that moves the second row in the Y axis
	 * @param distance is an integer is the number of pixels the text is going to move
	 * This method is executed by the preview
	 * @return void
	 */
	public void moveTwoY(int distance)
	{
		this.inferiorY= this.inferiorY + distance;
	}
	
	/**
	 * Method that moves the info text in the X axis
	 * @param distance is an integer is the number of pixels the text is going to move
	 * This method is executed by the preview
	 * @return void
	 */
	public void moveInfoX(int distance)
	{
		this.infoX= this.infoX + distance;
	}
	
	/**
	 * Method that moves the info text in the Y axis
	 * @param distance is an integer is the number of pixels the text is going to move
	 * This method is executed by the preview
	 * @return void
	 */
	public void moveInfoY(int distance)
	{
		this.infoY= this.infoY + distance;
	}
	
	/**
	 * Method that export the image to the location selected by the user
	 * @return void
	 * 
	 */
	public void exportImage()
	{
		try
		{
			//Write the  edited photo
			ImageIO.write(this.imageToEdit, "jpg", new File(this.saveLocationSelected));
			
		}
		catch(IOException e)
		{
			System.err.println(e);
			this.errorMessage= "Error in saving the edited image.";
			this.showErrorWindow();
		}
	}
	
	/**
	 * Method that creates and show the error window if any error occurs
	 *@return void
	 */
	private void showErrorWindow()
	{
		//Window
		this.errorWindow= new JFrame();
		this.errorWindow.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		this.errorWindow.setSize(260, 180);
		this.errorWindow.setTitle("ERROR");
		this.errorWindow.setLocationRelativeTo(null);
		this.errorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout= new BorderLayout();
		this.errorWindow.getContentPane().setLayout(layout);
		this.errorWindow.setLayout(layout);
		
		//Message
		JLabel error = new JLabel(this.errorMessage);
		error.setBounds(32, 151, 112, 28);
		error.setFont(new Font("Tahoma", Font.PLAIN, 9));
		this.errorWindow.add(error, BorderLayout.CENTER);
		
		this.errorWindow.setVisible(true);
	}
	
	/**
	 * Method that concatenate the info in a single line to be directly printed
	 *@return void
	 */
	private void concatenateInfo()
	{
		String infoString= "";
		
		//for each row
		for(int row = 0; row < this.numberOfInfoRows; row++)
		{
			//For each column
			for(int col = 0; col < this.numberOfColsInFile; col++)
			{
				//Add the new info to the string
				infoString += this.dataFromLabels[row][col];
				if(col == 1)
				{
					//Separation if the next is a category of info
					infoString += "    ";
				}
				else
				{
					//Separation if is the value of the category
					infoString += "  ";
				}
			}
		}
		
		//Sets the global variable
		this.concatenatedInfo= infoString;
		
	}
	
	/**
	 * Method that sets the location where the edited photo will be saved
	 * @param saveLocation String is the path given by the user
	 * @return void
	 */
	public void setSaveLocation(String saveLocation)
	{
		this.saveLocationSelected= saveLocation;
	}
	
	/**
	 * Method that resets the files of the editor (photo, copy of the photo and csv)
	 * @return void
	 */
	public void reset()
	{
		this.imageToEdit= null;
		this.originalBI= null;
		this.labelsCSV= null;
	}
	
	/**
	 * Method for making a new edition over the image using the info of the last loaded csv
	 * This method is executed by the preview for changing "manually" the text
	 * Works almost as a repaint method
	 * @return BufferedImage is the image re-edited
	 */
	public BufferedImage reEdit()
	{
		this.imageToEdit= null;
		this.writeLabels();
		return this.imageToEdit;
	}
}
