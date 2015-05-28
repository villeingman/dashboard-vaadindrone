package org.vaadin.inki.samples;

import java.io.Serializable;
import java.util.Date;

/**
 * A standard JPA entity, like in any other Java application.
 */
public class DroneData implements Serializable {

	private static final long serialVersionUID = 1L;

	private float theta;
	private float phi;
	private int battery;
	private int altitude;
	private int speed;
	private Date dataTimestamp;

	public float getTheta() {
		return theta;
	}

	public void setTheta(float theta) {
		this.theta = theta;
	}

	public float getPhi() {
		return phi;
	}

	public void setPhi(float phi) {
		this.phi = phi;
	}

	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Date getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(Date dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}

}
