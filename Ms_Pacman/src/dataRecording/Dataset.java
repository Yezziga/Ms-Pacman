package dataRecording;

import java.util.ArrayList;
import java.util.HashMap;

// TODO
public class Dataset {
	private DataTuple[] dataset;
	
	
	public Dataset(DataTuple[] dataTuples) {
		dataset = dataTuples;

	}
	
	public DataTuple[] getTuples() {
		return dataset;
	}
	

}
