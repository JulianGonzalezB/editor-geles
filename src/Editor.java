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
	private BufferedImage mostRecentImg= null;
	
	private int numberOfCols= 10;
	private int numberOfRows= 1;
	private String colorOfText= "White";
	private String orientation= "Horizontal";
	private int fontSize= 9;
	
	private File labelsCSV= null;
	private String [][] dataFromLabels= null;
	private int numberOfColsInFile= 2;
	private int numberOfInfoRows= 5;
	
	private String errorMessage= null;
	private JFrame errorWindow= null;
	
	private String info= null;
	private int infoHeight= 10;
	private int infoLeftMargin= 15;
	
	private String [] concatenatedLabels= null;
	private int labelsHeight= 25;
	
	private String saveLocationSelected= null;
	
	/**
	 * 
	 * @param photo
	 */
	public void setPhoto(BufferedImage photo)
	{
		this.originalBI= photo;
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
		this.orientation= orientation;
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
	public void edit()
	{
		this.readLabels();
		this.writeLabels();
		this.exportImage();
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
				
				for (int word= 0; word < wordsArray.length; word++)
				{
					this.dataFromLabels[row][word]= wordsArray[word];
				}
				row++;
			}
			
			if (this.dataFromLabels[0][1].equals("PCR") || this.dataFromLabels[0][1].equals("pcr") || this.dataFromLabels[0][1].equals("Pcr"))
			{
				this.dataFromLabels[1][0]= "Primer";
			}
			else
			{
				this.dataFromLabels[1][0]= "Protocolo";
			}
			
			this.concatenateInfo();
			this.concatenateLabels();
			
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
			BufferedImage imageToEdit= this.originalBI;
			Graphics graphics= imageToEdit.getGraphics();
			
			if(this.colorOfText.equals("White"))
			{
				graphics.setColor(Color.WHITE);
			}
			else
			{
				graphics.setColor(Color.BLACK);
			}
			
			graphics.setFont(new Font("Arial Black", Font.PLAIN, this.fontSize));
			
			//Draw the info of the gel
			graphics.drawString(this.info, this.infoLeftMargin, imageToEdit.getHeight() - this.infoHeight);
			
			for(int channel = 0; channel < this.numberOfCols; channel++)
			{
				graphics.drawString(this.concatenatedLabels[channel], (channel + 1)  * (imageToEdit.getWidth() / this.numberOfCols), this.labelsHeight);
			}
			
			if(this.numberOfRows == 2)
			{
				for(int channel = 0; channel < this.numberOfCols; channel++)
				{
					graphics.drawString(this.concatenatedLabels[this.numberOfCols + channel], (channel + 1)  * (imageToEdit.getWidth() / this.numberOfCols), (imageToEdit.getHeight() / 4) - this.labelsHeight);
				}
			}
			
			this.mostRecentImg= imageToEdit;
		}
		catch(Exception e)
		{
			this.errorMessage= "	.jpg file not found.	Please select a file and try again";
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
			ImageIO.write(this.mostRecentImg, "jpg", new File(this.saveLocationSelected));
		}
		catch(IOException e)
		{
			this.errorMessage= "Invalid Save destination";
			this.showErrorWindow();
		}
	}
	
	/**
	 * 
	 */
	private void showErrorWindow()
	{
		//Esto no funciona
		this.errorWindow= new JFrame();
		this.errorWindow.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		this.errorWindow.setSize(260, 180);
		this.errorWindow.setTitle("ERROR");
		this.errorWindow.setLocationRelativeTo(null);
		this.errorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.errorWindow.getContentPane().setLayout(null);
		BorderLayout layout= new BorderLayout();
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
		
		this.info= infoString;
		
	}
	
	/**
	 * 
	 */
	private void concatenateLabels()
	{
		String [] finalLabels= new String[this.numberOfCols];
		String currentLabel= "";
		int rowOfLabel= 6;
		
		for(int row = 0; row < this.numberOfRows; row++)
		{
			for(int col = 0; col < this.numberOfColsInFile; col++)
			{
				currentLabel += this.dataFromLabels[rowOfLabel][col];
			}
			
			finalLabels[row]= currentLabel;
			currentLabel= "";
			rowOfLabel++;
		}
		
		this.concatenatedLabels= finalLabels;
	}
	
	/**
	 * 
	 */
	public void setSaveLocation(String saveLocation)
	{
		this.saveLocationSelected= saveLocation;
	}
}
