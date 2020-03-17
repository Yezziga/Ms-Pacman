package dataRecording;

import pacman.game.util.*;

/**
 * This class uses the IO class in the PacMan framework to do the actual saving/loading of
 * training data.
 * @author andershh
 *
 */
public class DataSaverLoader {
	
	private static String trainFile = "trainingData.txt";
	private static String testFile = "testData.txt";
	
	public static void SavePacManData(DataTuple data)
	{
		IO.saveFile(trainFile, data.getSaveString(), true);
	}
	
	public static DataTuple[] LoadPacManData()
	{
		String data = IO.loadFile(trainFile);
		String[] dataLine = data.split("\n");
		DataTuple[] dataTuples = new DataTuple[dataLine.length];
		
		for(int i = 0; i < dataLine.length; i++)
		{
			dataTuples[i] = new DataTuple(dataLine[i]);
		}
		
		return dataTuples;
	}
	
	public static DataTuple[] LoadPacManData(String fileName)
	{
		String data = IO.loadFile(fileName);
		String[] dataLine = data.split("\n");
		DataTuple[] dataTuples = new DataTuple[dataLine.length];
		
		for(int i = 0; i < dataLine.length; i++)
		{
			dataTuples[i] = new DataTuple(dataLine[i]);
		}
		
		return dataTuples;
	}
	
	/**
	 * Returns the number of tuples in the testing set.
	 * @return
	 */
	public static int getNrOfTuples (String fileName) {
		String data = IO.loadFile(fileName);
		String[] dataLine = data.split("\n");
		return dataLine.length;
	}
}
