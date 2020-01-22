package dataRecording;

import java.util.ArrayList;
import java.util.HashMap;

// TODO
public class Dataset {
	//private DataTuple[] dataset;
	public ArrayList<DataTuple> dataset;
	public HashMap<String, HashMap<String, Integer>> attributesWithValuesAndCounts;
	
	
	public Dataset(ArrayList<DataTuple> dataTuples) {
		dataset = dataTuples;
		attributesWithValuesAndCounts = new HashMap<>();
		fillAttributesHashMap();

	}
	
	public Dataset getSubDataSetWithValue(String attribute, String value) {
		ArrayList<DataTuple> newDataset = new ArrayList<DataTuple>();
		
		for(DataTuple tuple : dataset) {
			if(tuple.discretizeAttrValue(attribute).equals(value)) {
				newDataset.add(tuple);
			}
		}
		return new Dataset(newDataset);
	}
	
	/*public DataTuple[] getTuples() {
		return dataset;
	}*/
	
	public ArrayList<DataTuple> getTuples(){
		return dataset;
	}
	
	public void fillAttributesHashMap() {
		for (DataTuple t : this.dataset) {
			for (String attr: t.getHashMap().keySet()) {
				String value = t.getHashMap().get(attr);
				if(attributesWithValuesAndCounts.containsKey(attr)) {
					if(attributesWithValuesAndCounts.get(attr).containsKey(value)) {
						Integer count = attributesWithValuesAndCounts.get(attr).get(value);
						count++;
						attributesWithValuesAndCounts.get(attr).put(value, count);
					} else {
						attributesWithValuesAndCounts.get(attr).put(value, 1);
					}
				} else {
					attributesWithValuesAndCounts.put(attr, new HashMap<String, Integer>());
					attributesWithValuesAndCounts.get(attr).put(value, 1);
				}
			}
		}
	}

}
