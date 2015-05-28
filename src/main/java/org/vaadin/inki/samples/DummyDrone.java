package org.vaadin.inki.samples;

import java.util.Date;
import java.util.Random;

import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.ui.UI;

public class DummyDrone extends Thread {

	public interface Listener {
		public void newDroneDataEvent(DroneData data);
	}

	private Listener listener;
	protected boolean detached = false;

	public DummyDrone(DummyDrone.Listener listener) {
		this.listener = listener;
		UI.getCurrent().addDetachListener(new DetachListener() {
			@Override
			public void detach(DetachEvent event) {
				detached = true;
			}
		});
	}

	@Override
	public void run() {
		Random r = new Random();
		while (!detached) {
			DroneData d = new DroneData();
			d.setAltitude(r.nextInt(600));
			d.setBattery(r.nextInt(100));
			d.setDataTimestamp(new Date());
			d.setSpeed(r.nextInt(20));
			d.setTheta(r.nextFloat());
			d.setPhi(r.nextFloat());
			try {
				listener.newDroneDataEvent(d);
			} catch (Exception e) {
				System.err.println("something went wrong, e: " + e.getMessage());
				e.printStackTrace();
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// ignored
			}
		}
	}
}
