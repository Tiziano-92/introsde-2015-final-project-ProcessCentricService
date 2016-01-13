package introsde.model.rest;



import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Goal {

	private String measureName;
	private int goalValue;

	public Goal() {

	}

	public Goal(String measureName, int goalValue) {
		this.measureName = measureName;
		this.goalValue = goalValue;
	}

	public String getMeasureName() {
		return this.measureName;
	}

	public void setMeasureName (String name) {
		this.measureName = name;
	}

	public int getGoalValue() {
		return this.goalValue;
	}

	public void setGoalValue(int goal) {
		this.goalValue = goal;
	}

}