package decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataRecording.Dataset;

public class ID3 extends AttributeSelection {
	
	@Override
	public String AttributeSelection(Dataset dataset, List<String> attributeList) {
		String res = null;
		float infoD = entropy(dataset, "strategy");
		
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
}
