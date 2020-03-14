package dataRecording;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a dataset and contains information about the stored
 * data, attributes, values of attributes and number of times that value appears
 * in the dataset.
 * 
 * @author Jessica
 *
 */
public class Dataset {
	public DataTuple[] dataset;
	/**
	 * Name of attribute : attributeVale : number of occurrences
	 */
	public HashMap<String, HashMap<String, Integer>> attributes;

	public Dataset(DataTuple[] data) {
		dataset = data;
		attributes = new HashMap<>();
		fillMap();
	}

	/**
	 * Creates and returns a sub-dataset with tuples containing the same attribute
	 * value
	 * 
	 * @param attribute
	 *            the attribute to look for
	 * @param value
	 *            the attribute value to look for
	 * @return a Dataset
	 */
	public Dataset getSubDataset(String attribute, String value) {

		ArrayList<DataTuple> newDataset = new ArrayList<DataTuple>();

		for (DataTuple tuple : dataset) {
			if (tuple.discretize(attribute).equals(value)) {
				newDataset.add(tuple);
			}
		}
		DataTuple[] arr = new DataTuple[newDataset.size()];
		for (int i = 0; i < newDataset.size(); i++) {
			arr[i] = newDataset.get(i);
		}
		return new Dataset(arr);
	}

	/**
	 * Fills the dataset's HashMap with name of attributes, the attributes' values and the number of occurrences the values have appeared in the data in each tuple-
	 */
	public void fillMap() {
		
		// go through all tuples in the dataset
		for (DataTuple tuple : this.dataset) {
			
			//go through all the attributes in the tuple
			for (String attribute : tuple.getHash().keySet()) {
				String attributeValue = tuple.getHash().get(attribute);
				// check if the attribute already exist in the dataset map
				if (attributes.containsKey(attribute)) {
					// If attribute & the value already exist, increment the nr of occurrence
					if (attributes.get(attribute).containsKey(attributeValue)) {
						Integer count = attributes.get(attribute).get(attributeValue);
						count++;
						attributes.get(attribute).put(attributeValue, count);
					} else {
						// first time the value occurs; add to the hashmap
						attributes.get(attribute).put(attributeValue, 1);
					}
				} else {
					// first time the attribute occurs; add to the hashmap
					attributes.put(attribute, new HashMap<String, Integer>());
					attributes.get(attribute).put(attributeValue, 1);
				}
			}
		}
	}

	// TODO: text to print out nr of occurrence for each attribute value
	public String attributesToString() {
		String str = "";
		return str;
	}
}
