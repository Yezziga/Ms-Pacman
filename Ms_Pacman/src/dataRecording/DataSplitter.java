package dataRecording;

import java.io.File;

import pacman.game.util.IO;

/**
 * Class used for splitting the collected data into a training set and testing
 * set(70/30). The training set will be stored in the trainingData.txt, and the
 * testing set in the testData.txt
 * 
 * @author Jessica
 *
 */
public class DataSplitter {
	private static String training = "trainingData.txt";
	private static String testing = "testData.txt";

	/**
	 * Splits the gathered data and saves it in two text files. Training set contain
	 * 70% of the data and testing set 30%. Won't work if there is already a
	 * testData.txt --> remove the text file first!
	 */
	public static void splitData() {

		File testFile = new File(testing);
		if (!testFile.exists()) {
			String data = IO.loadFile(training);
			String[] dataLine = data.split("\n");
			int totalLines = dataLine.length;
			int testLines = (int) Math.ceil((totalLines * 0.7));
			System.out.println(testLines);
			System.out.println(totalLines);
			String testData = "";
			String trainingData = "";
			for (int i = testLines; i < totalLines; i++) {
				testData += dataLine[i] + "\n";

			}
			for (int i = 0; i < testLines; i++) {
				trainingData += dataLine[i] + "\n";
			}
			IO.saveFile(training, trainingData, false);
			IO.saveFile(testing, testData, false);
		}
	}
}