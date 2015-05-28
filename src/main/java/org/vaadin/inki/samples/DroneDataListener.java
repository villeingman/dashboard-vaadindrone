package org.vaadin.inki.samples;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.GsonBuilder;

public class DroneDataListener implements MqttCallback {

	private static final String IOT_BROKER_HOST = "kwxdxh.messaging.internetofthings.ibmcloud.com";
	private static final Integer IOT_BROKER_HOST_PORT = 1883;
	private static final String IOT_USERNAME = "a-kwxdxh-efzfbllxbb";
	private static final String IOT_PASSWORD = "ZW)O6BErdAR3e35cSt";
	private static final String IOT_ID = "a:kwxdxh:"
			+ MqttClient.generateClientId();
	private static final String IOT_URI = "tcp://" + IOT_BROKER_HOST + ":"
			+ IOT_BROKER_HOST_PORT;

//	private static final String IOT_TOPIC_FLIGHT_DATA = "iot-2/evt/vaadindrone-data/fmt/json";
	private static final String IOT_TOPIC_FLIGHT_DATA = "iot-2/type/+/id/+/evt/+/fmt/+";
//	private static final String IOT_TOPIC_LED_ANIMATION = "iot-2/cmd/vaadindrone-blink/fmt/json";
	private static final String IOT_TOPIC_LED_ANIMATION = "iot-2/type/dronedatalaptop/id/vaadin-villeingman-1/cmd/cid/fmt/json";

	private static final String MY_JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

	public interface Listener {
		public void newDroneDataEvent(DroneDataSample data);
	}

	private static DroneDataListener instance;

	private Listener listener;
	private MqttClient client;
	private MqttConnectOptions opts;
	private boolean useLocal;

	public static DroneDataListener getInstance() {
		if (instance == null) {
			instance = new DroneDataListener();
		}
		return instance;
	}
	
	private DroneDataListener() {
		if (System.getenv("VCAP_SERVICES") == null) {
			// Not running in Bluemix, use local broker
			useLocal = true;
		} else {
			useLocal = false;
		}
		
		
		try {
			if (useLocal) {
				client = new MqttClient("tcp://127.0.0.1:1883", IOT_ID);
			} else {
				client = new MqttClient(IOT_URI, IOT_ID);
			}
			opts = new MqttConnectOptions();
			opts.setUserName(IOT_USERNAME);
			opts.setPassword(IOT_PASSWORD.toCharArray());
		} catch (MqttException e) {
			System.err.println("Data listen init failed");
		}
	}

	public void listen(Listener listener) {
		this.listener = listener;
		try {
			if (!this.client.isConnected()) {
				client.connect(opts);
			}
			client.setCallback(this);
			if (useLocal) {
				client.subscribe("data");
			} else {
				client.subscribe(IOT_TOPIC_FLIGHT_DATA);
			}
		} catch (Exception e) {
			System.err.println("UI drone data listener: subscribe failed");
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {

		DroneDataSample newData = new GsonBuilder()
				.setDateFormat(MY_JSON_DATE_FORMAT).create()
				.fromJson(message.toString(), DroneDataSample.class);

		this.listener.newDroneDataEvent(newData);
	}

	public void sendBlinkCommand() {
		try {
			if (!this.client.isConnected()) {
				this.client.connect(opts);
			}
			MqttMessage msg = new MqttMessage();
			msg.setQos(0);
			msg.setPayload("Blink please".getBytes());
			if (useLocal) {
				this.client.publish("command", msg);
			} else {
				this.client.publish(IOT_TOPIC_LED_ANIMATION, msg);
			}
		} catch (Exception e) {
			// fail silently
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		try {
			if (!this.client.isConnected()) {
				this.client.connect(opts);
			}
		} catch (Exception e) {
			System.err.println("UI data listener: connection lost");
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("UI: delivery complete");
	}

	public void stop() {
		try {
			client.close();
		} catch (Exception e) {
			// ignored
		}
	}
}
