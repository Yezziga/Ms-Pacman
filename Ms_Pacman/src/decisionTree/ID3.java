package decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataRecording.Dataset;

public class ID3 {
	
	public ID3(Dataset dataset, ArrayList<String> listOfAttributes, String label, Node node) {

	}
	
	public String attributeSelection(Dataset dataset, List<String> attributeList) {
		String res = null;
		float infoD = calculateEntropy(dataset, "strategy");
		
		ArrayList<Float> gainA = new ArrayList<Float>();
		
		for(String attr : attributeList) {
			float infoADAux = infoAD(dataset, attr);
			float gain = infoD - infoADAux;
			gainA.add(gain);
		}
		
		float max = 0;
		int posMax = 0;
		
		for(int i = 0; i < gainA.size(); i++) {
			if(gainA.get(i) > max) {
				max = gainA.get(i);
				posMax = i;
			}
		}
		
		res = attributeList.get(posMax);
		return res;
	}
	
	public float infoAD(Dataset dataset, String attribute) {
		float info = 0;
		
		HashMap<String, Integer> map = dataset.attributesWithValuesAndCounts.get(attribute);
		List<String> values = new ArrayList(map.keySet());
		for(String value : values) {
			Dataset dt = dataset.getSubDataSetWithValue(attribute, value);
			float sizeDataset = dataset.dataset.size();
			float subDataset = dt.dataset.size();
			info += (subDataset / sizeDataset) * calculateEntropy(dt, "strategy");
		}
		return info;
	}
	
	public float calculateEntropy(Dataset dataset, String attribute) {
		float infoD = 0;
		
		HashMap<String, Integer> map = dataset.attributesWithValuesAndCounts.get(attribute);
		
		List<String> strategyValues = new ArrayList(map.keySet());
		
		for(String strategyVal : strategyValues) {
			float pi = calculateProbability(dataset, attribute, strategyVal);
			if(pi > 0) {
				float res = -pi * log2(pi);
				
				infoD += res;
			} else if(pi == (-1)) {
				return infoD;
			}
		}
		return infoD;
	}
	
	public float calculateProbability(Dataset dataset, String attributes, String value) {
		HashMap<String, Integer> mapValuesAttr = dataset.attributesWithValuesAndCounts.get(attributes);
		
		float sizeAttr = mapValuesAttr.get(value);
		float sizeDataset = dataset.dataset.size();
		
		return sizeAttr / sizeDataset; 
	}
	
	float log2(float x) {
		return (float) (Math.log(x) / Math.log(2));
	}
	
	public void calculateAverageInfo() {
		
	}
	
	public void calculateInformationGain() {
		
	}
}
