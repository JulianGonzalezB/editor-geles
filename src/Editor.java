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

public class Editor {
	
	private BufferedImage originalBI= null;
	private BufferedImage imageToEdit= null;
	
	private int numberOfCols= 10;
	private int numberOfRows= 1;
	
	private String colorOfText= "White";
	private String infOrientation= "Vertical";
	private int fontSize= 5;
	private int infoSize= 5;
	
	private File labelsCSV= null;
	private String [][] dataFromLabels= null;
	private int numberOfColsInFile= 2;
	private int numberOfInfoRows= 5;
	
	private String errorMessage= null;
	private JFrame errorWindow= null;
	
	private String concatenatedInfo= null;
	
	private String saveLocationSelected= null;
	
	/**
	 * 
	 * @param photo
	 * @throws IOException 
	 */
	public void setPhoto(File photo) throws IOException
	{
		this.originalBI= ImageIO.read(photo);
	}
	
	/**
	 * 
	 * @param labels
	 */
	public void setLabels(File labels)
	{
		this.labelsCSV= labels;
	}
	
	/**
	 * 
	 */
	public void setNumberOfCols(int numberOfCols)
	{
		this.numberOfCols= numberOfCols;
	}
	
	/**
	 * 
	 */
	public void setNumberOfRows(int numberOfRows)
	{
		this.numberOfRows= numberOfRows;
	}
	
	/**
	 * 
	 */
	public void setColorOfText(String colorOfText)
	{
		this.colorOfText= colorOfText;
	}
	
	/**
	 * 
	 */
	public void setOrientation(String orientation)
	{
		this.infOrientation= orientation;
	}
	
	/**
	 * 
	 */
	public void setFontSize(int fontSize)
	{
		this.fontSize= fontSize;
	}
	
	/**
	 * 
	 */
	public void setInfoSize(int infoSize)
	{
		this.infoSize= infoSize;
	}
	
	/**
	 * 
	 */
	public void edit()
	{
		this.readLabels();
		this.writeLabels();
		this.exportImage();
		this.imageToEdit= null;
		this.originalBI= null;
		this.labelsCSV= null;
	}
	
	/**
	 * 
	 */
	private void readLabels()
	{
		try
		{
			Scanner scanner= new Scanner(this.labelsCSV);
			scanner.useDelimiter("\\n");
			int row= 0;
			String inputLine= null;
			this.dataFromLabels= new String[this.numberOfRows * (this.numberOfCols) + this.numberOfInfoRows + 1][this.numberOfColsInFile];
			
			while (scanner.hasNextLine())
			{
				inputLine= scanner.nextLine();
				String[] wordsArray= inputLine.split(";");
				
				if(wordsArray.length != 2)
				{
					String[] definitiveArray= new String[2];
					definitiveArray[0]= wordsArray[0];
					definitiveArray[1]= wordsArray[1];
					wordsArray= definitiveArray;
				}
				
				for (int word= 0; word < wordsArray.length; word++)
				{
					this.dataFromLabels[row][word]= wordsArray[word];
				}
				row++;
			}
			
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
			this.errorMessage= ".csv file not found. Please select a file and try again";
			this.showErrorWindow();
		}
	}
	
	/**
	 * 
	 */
	private void writeLabels()
	{
		try
		{
			this.imageToEdit= this.originalBI;
			int height= this.imageToEdit.getHeight();
			int width= this.imageToEdit.getWidth();
			Graphics graphics= this.imageToEdit.getGraphics();
			int XSegment= width / this.numberOfCols;
			
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
					graphics.drawString(this.dataFromLabels[row][1], width / 27, (height / 2 ) + row * (this.infoSize + 20));
				}
			}
			
			graphics.setFont(new Font("Arial Black", Font.PLAIN, this.fontSize));
			
			//Draw the labels in the holes
			for(int row= 0; row < this.numberOfCols; row++)
			{
				for(int col= 0; col < this.numberOfColsInFile; col++)
				{
					graphics.drawString(this.dataFromLabels[row + 6][col], (row) * XSegment + XSegment/4, (height / 7 - 45) + col * (this.fontSize + 5));
				}
			}
			
			if(this.numberOfRows == 2)
			{
				for(int row= 0; row < this.numberOfCols; row++)
				{
					for(int col= 0; col < this.numberOfColsInFile; col++)
					{
						graphics.drawString(this.dataFromLabels[row + 6 + this.numberOfCols][col], (row) * XSegment + XSegment/4, (2 * (height / 3) + 40) + col * (this.fontSize + 5));
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
	 * 
	 * 
	 */
	private void exportImage()
	{
		try
		{
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
	 * 
	 */
	private void showErrorWindow()
	{
		this.errorWindow= new JFrame();
		this.errorWindow.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		this.errorWindow.setSize(260, 180);
		this.errorWindow.setTitle("ERROR");
		this.errorWindow.setLocationRelativeTo(null);
		this.errorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout= new BorderLayout();
		this.errorWindow.getContentPane().setLayout(layout);
		this.errorWindow.setLayout(layout);
		
		JLabel error = new JLabel(this.errorMessage);
		error.setBounds(32, 151, 112, 28);
		error.setFont(new Font("Tahoma", Font.PLAIN, 9));
		this.errorWindow.add(error, BorderLayout.CENTER);
		
		this.errorWindow.setVisible(true);
	}
	
	/**
	 * 
	 */
	private void concatenateInfo()
	{
		String infoString= "";
		
		for(int row = 0; row < this.numberOfInfoRows; row++)
		{
			for(int col = 0; col < this.numberOfColsInFile; col++)
			{
				infoString += this.dataFromLabels[row][col];
				if(col == 1)
				{
					infoString += "    ";
				}
				else
				{
					infoString += "  ";
				}
			}
		}
		
		this.concatenatedInfo= infoString;
		
	}
	
	/**
	 * 
	 */
	public void setSaveLocation(String saveLocation)
	{
		this.saveLocationSelected= saveLocation;
	}
}
