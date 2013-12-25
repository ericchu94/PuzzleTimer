package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerometerSensorEventListener implements SensorEventListener {

	class SensorValue {
		private long timestamp;
		private double value;

		public SensorValue(double value) {
			timestamp = System.currentTimeMillis();
			this.value = value;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public double getValue() {
			return value;
		}
	}

	private static final float CHANGE_THRESHHOLD = 2f;
	private static final int CONTACT_DURATION = 200;

	List<SensorValue> sensorValues = new ArrayList<SensorValue>();
	private Runnable runnable;

	public AccelerometerSensorEventListener(Runnable runnable) {
		this.runnable = runnable;
	}

	private double getAcceleration(float x, float y, float z) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	public double getChange() {
		long start = System.currentTimeMillis() - CONTACT_DURATION;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		ListIterator<SensorValue> iterator = sensorValues.listIterator();
		while (iterator.hasNext()) {
			SensorValue sensorValue = iterator.next();
			if (sensorValue.getTimestamp() < start) {
				iterator.remove();
			} else {
				double value = sensorValue.getValue();
				if (value < min)
					min = value;
				else if (value > max)
					max = value;
			}
		}

		return max - min;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		double acceleration = getAcceleration(event.values[0], event.values[1],
				event.values[2]);
		sensorValues.add(new SensorValue(acceleration));

		if (getChange() > CHANGE_THRESHHOLD) {
			runnable.run();
		}
	}
}
