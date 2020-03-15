package dataRecording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.IntStream;

//Do not touch, continue work tomorrow
public class Dataset2 {
	private String name;
	private String[][] data = null;
	private double entropy = 0;
	private HashMap<Feature, Double> infoGains = new HashMap<Feature, Double>();
	private Feature splitOnFeature = null;
	
	public Dataset2(String name, String[][] data) {
		this.data = data;
		this.name = name;
		calculateEntropy().calculateInfoGains().findSplitOnFeature();
	}
	
	private Dataset2 calculateEntropy() {
		new Feature(data, data[0].length -1).getValues().stream().forEach(featureValue ->
		entropy += minusPlog2((double)featureValue.getOccurences() / (data.length - 1)));
		return this;
	}
	private Dataset2 calculateInfoGains() {
		IntStream.range(0, data[0].length - 1).foreach(row -> {
			Feature feature = new Feature(data, column);
			ArrayList<DataSet> dataSets = new ArraayList<DataSet>();
			feature.getFeatureValues().stream().forEach(featureValues ->
			dataSets.add(createDataSet(feature, featureValue, data)));
			double summation = 0;
			for(int i = 0; i < dataSets.size(); i++) {
				summation += ((double)(dataSets.get(i).getData().length - 1) /(data.length - 1))*dataSets.get(i).getEntropy();
			}
			infoGains.put(feature, entropy - summation);
		});
		return this;
	}
	private Dataset2 findSplitOnFeature() {
		Iterator<Feature> iterator = infoGains.keySet().iterator();
		while(iterator.hasNext()) {
			Feature feature = iterator.next();
			if(splitOnFeature == null || infoGains.get(splitOnFeature) < infoGains.get(feature))
				splitOnFeature = feature;
		}
		return this;
	}
	Dataset2 createDataSet(Feature feature, FeatureValue featureValue, String[][] data) {
		int column = getColNumb(feature.getName());
		String[][] returnData = new String[featureValue.getOccurences()+1][data[0].length];
		returnData[0] = data[0];
		int counter = 1;
		for (int row = 1; row < data.length; row++) {
			if(data[row][column] == featureValue.getName())
				returnData[counter++] = data[row];
		}
		return new Dataset2(feature.getName() + ": " + featureValue.getName(), deleteColumn(returnData, column));
	}
	
	private String[][] deleteColumn(String[][] data, int toDeleteColumn) {
		String returnData[][] = new String[data.length][data[0].length - 1];
		IntStream.range(0, data.length).foreach(row -> {
			int columnCounter = 0;
			for(int column = 0; column < data[0].length; column++) {
				if(column != toDeleteColumn) {
					retunrData[row][columnCounter++] = data[row][column];
				}
			}
		});
		return returnData;
	}
	public int getColNumb(String colName) {
		int returnValue = -1;
		for(int column = 0; column < data[0].length - 1; column++) {
			if(data[0][column] == colName) {
				returnValue = column;
				break;
			}
		}
		return returnValue;
	}
	
	public String[][] getData() {
		return data;
	}
	public double getEntropy() {
		return entropy;
	}
	public HashMap<Feature, Double> getInfoGains() {
		return infoGains;
	}
	public Feature getSplitOnFeature() {
		return splitOnFeature;
	}
	
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		for(int row = 0; row < data.length; row++) {
			for(int column = 0; column < data.length; column++) {
				stringBuffer.append(data[row][column]);
				IntStream.range(0, 24 - data[row][column].length()).forEach(i -> stringBuffer.append(" "));
			}
			stringBuffer.append("\n");
			if(row == 0) {
				IntStream.range(0, 108).forEach(i -> stringBuffer.append("-"));
				stringBuffer.append("\n");
			}
		}
		return stringBuffer.toString();
	}
	
	double minusPlog2(double p) {
		double returnValue = 0;
		if(p != 0)
			returnValue = (-1) * p * Math.log(p) / Math.log(2);
		return returnValue;
	}
}
