package decisionTree;

import dataRecording.Dataset;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public abstract class AttributeSelection {
	
	public String AttributeSelection(Dataset dataset, List<String> attributeList) {
		return null;
	}
	
	public float entropy(Dataset dataset, String attribute) {
		float infoD = 0;
		HashMap<String, Integer> map = dataset.attributes.get(attribute);
		List<String> strategyValues = new ArrayList(map.keySet());
		
		for(String strategyVal : strategyValues) {
			float pi = CalculateProbability(dataset, attribute, strategyVal);
			if(pi > 0) {
				float res = -pi * log2(pi);
				infoD += res;
			}
			else if(pi == (-1)) {
				return infoD;
			}
		}
		return infoD;
	}
	
	public float infoAD(Dataset dataset, String attribute) {
		float info = 0;
		
		HashMap<String, Integer> map = dataset.attributes.get(attribute);
		
		List<String> values = new ArrayList (map.keySet());
		for(String value : values) {
			Dataset dt = dataset.getSubset(attribute, value);
			float sizeDataset = dataset.dataset.size();
			float subDataset = dt.dataset.size();
			
			info += (subDataset / sizeDataset) * entropy(dt, "strategy");
		}
		return info;
	}
	
	public float CalculateProbability(Dataset dataset, String attribute, String value) {
		HashMap<String, Integer> mapValuesAttr = dataset.attributes.get(attribute);
		
		float sizeAttr = mapValuesAttr.get(value);
		float sizeDataset = dataset.dataset.size();
		
		return sizeAttr / sizeDataset;
	}
	
	float log2(float x) {
		return (float) (Math.log(x) / Math.log(2));
	}
}
