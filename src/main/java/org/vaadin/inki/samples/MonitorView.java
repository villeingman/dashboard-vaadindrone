package org.vaadin.inki.samples;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;

/*
 * A very simple view that just displays an "about text". The view also has 
 * a button to reset the demo date in the database.
 */
public class MonitorView extends GridLayout implements View,
		DroneDataListener.Listener {

	public interface MonitorEventListener {
		public void newDroneEvent(DroneDataSample data);
	}

	private List<MonitorEventListener> listeners = new ArrayList<MonitorView.MonitorEventListener>();
	protected boolean attached = true;
	private DroneDataListener droneDataListener;

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "Monitor";

	public MonitorView() {
		setColumns(2);
		setRows(2);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		addComponent(new AltitudePanel(this), 0, 0, 1, 0);
		addComponent(new BatteryPanel(this));
		addComponent(new TiltPanel(this));

		droneDataListener = DroneDataListener.getInstance();
	}

	@SuppressWarnings("serial")
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		getUI().addDetachListener(new DetachListener() {
			@Override
			public void detach(DetachEvent event) {
				attached = false;
				droneDataListener.stop();
			}
		});
		droneDataListener.listen(this);
	}

	@Override
	public void newDroneDataEvent(final DroneDataSample newData) {
		if (getUI() == null) {
			return;
		}
		getUI().access(new Runnable() {
			@Override
			public void run() {
				for (MonitorEventListener monitorEventListener : listeners) {
					monitorEventListener.newDroneEvent(newData);
				}
			}
		});
	}

	public void addMonitorEventListener(MonitorEventListener listener) {
		this.listeners.add(listener);
	}
}
