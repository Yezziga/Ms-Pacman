package dataRecording;

import java.util.HashSet;
import java.util.stream.IntStream;

//Do not touch, continue work tomorrow
public class Feature {
	private String name = null;
	private HashSet<FeatureValue> values = new HashSet<FeatureValue>();
	
	public Feature(String[][] data, int column) {
		this.name = data[0][column];
		IntStream.range(1, data.length).forEach(row -> values.add(new FeatureValue(data[row][column])));
		values.stream().forEach(featureValue -> {
			int counter = 0;
			for (int row = 1; row < data.length; row++)
				if(featureValue.getName() == data[row][column]) {
					featureValue.setOccurences(counter++);
				}
		});
	}
	
	public String getName() {
		return name;
	}
	public HashSet<FeatureValue> getValues() {
		return values;
	}
	public String toString() {
		return name;
	}
}
