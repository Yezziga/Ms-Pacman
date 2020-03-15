package dataRecording;

//Do not touch, continue work tomorrow
public class FeatureValue {
	private String name;
	private int occurences;
	public FeatureValue(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public int getOccurences() {
		return occurences;
	}
	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}
	public int hashCode() {
		return name.hashCode();
	}
	public boolean equals(Object object) {
		boolean returnValue = true;
		if(object == null || (getClass() != object.getClass()))
			returnValue = false;
		if(name == null) {
			if(((FeatureValue) object).name != null) {
				returnValue = false;
			}
			else if(!name.equals(((FeatureValue) object).name)) {
				returnValue = false;
			}
		}
		return returnValue;
	}
	public String toString() {
		return name;
	}
}
