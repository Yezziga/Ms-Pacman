package dataRecording;

import java.util.ArrayList;
import java.util.HashMap;

// TODO
public class Dataset {
	private DataTuple[] dataset;
	public HashMap<String, HashMap<String, Integer>> attributes;
	
	public Dataset(DataTuple[] dataTuples) {
		dataset = dataTuples;
		attributes = new HashMap<>();
		fillMap();
	}
	
	private void fillMap() {
		// TODO Auto-generated method stub
		
	}

	public DataTuple[] getTuples() {
		return dataset;
	}
	

}
