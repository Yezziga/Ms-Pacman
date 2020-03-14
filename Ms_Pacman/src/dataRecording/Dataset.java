package dataRecording;

import java.util.ArrayList;
import java.util.HashMap;

// TODO
public class Dataset {
	public DataTuple[] dataset;
	public HashMap<String, HashMap<String, Integer>> attributes; // attribute, name of attribute value, amount of times 
	
	public Dataset(DataTuple[] dataTuples) {
		dataset = dataTuples;
		attributes = new HashMap<>();
		fillMap();
	}
	
	/**
	 * Fills the dataset's map with name of attribute, the attribute's values and 
	 * how many times the values have appeared in the data in each datatuple
	 */
	private void fillMap() {
		
		for (DataTuple tuple : dataset) {
			
			for (String attribute : tuple.getHashMap().keySet()) {
				
				String attrValue = tuple.getHashMap().get(attribute);
				// check if the attribute already exist in dataset map 
				if(attributes.containsKey(attrValue)) {
					if(attributes.get(attribute).containsKey(attrValue)) {
						int count = attributes.get(attribute).get(attrValue);
						count++;
						attributes.get(attribute).put(attrValue, count);
					} else {
						attributes.get(attribute).put(attrValue, 1);
					}
					
				} else {
					// adds the attribute to the dataset map
					attributes.put(attribute, new HashMap<String, Integer>());
					attributes.get(attribute).put(attrValue, 1);
				}
			}
		}
		
	}

	public DataTuple[] getTuples() {
		return dataset;
	}

	/**
	 * Creates a sub-set of data containing the attribute's values.
	 * @param attribute
	 * @param value
	 * @return
	 */
	public Dataset getSubset(String attribute, String value) {
		ArrayList<DataTuple> newSet = new ArrayList<>();
		for (DataTuple dataTuple : dataset) {
			if(dataTuple.discretizeAttrValue(attribute).equals(value)) {
				newSet.add(dataTuple);
			}
		}
		DataTuple[] arr = (DataTuple[]) newSet.toArray();
		return new Dataset(arr);
	}
	
	double minusPlog2(double p) {
		double returnValue = 0;
		if(p != 0)
			returnValue = (-1) * p * Math.log(p) / Math.log(2);
		return returnValue;
	}
	

}
