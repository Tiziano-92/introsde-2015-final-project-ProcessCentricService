package introsde.model.rest;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LifeStatus {

	private String dateRegistered;
	private String measureName;
	private int measureValue;

	public LifeStatus() {

	}

	public LifeStatus(String dateRegistered, String measureName, int measureValue) {
		this.dateRegistered = dateRegistered;
		this.measureName = measureName;
		this.measureValue = measureValue;
	}

	public String getDateRegistered() {
		return this.dateRegistered;
	}

	public void setDateRegistered(String dateRegister) {
		this.dateRegistered = dateRegister;
	}

	public String getMeasureName() {
		return this.measureName;
	}

	public void setMeasureName (String name) {
		this.measureName = name;
	}

	public int getMeasureValue() {
		return this.measureValue;
	}

	public void setMeasureValue(int value) {
		this.measureValue = value;
	}

}