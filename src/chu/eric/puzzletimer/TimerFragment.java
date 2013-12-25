package chu.eric.puzzletimer;

import java.util.Random;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TimerFragment extends Fragment implements OnClickListener {

	public static final String ARG_SCRAMBLE = "scramble";

	enum State {
		Idle, Solving
	}

	private Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (state == State.Solving) {
				updateText(getDuration());
				handler.postDelayed(timerRunnable, 0);
			}
		}
	};

	private Runnable accelerometerRunnable = new Runnable() {
		@Override
		public void run() {
			timer.performClick();
		}
	};

	private long start;
	private String scramble;

	private SensorManager sManager;
	private Sensor accelerometer;

	private AccelerometerSensorEventListener accelerometerSensorEventListener = new AccelerometerSensorEventListener(
			accelerometerRunnable);

	private Random random = new Random();
	private Handler handler = new Handler();
	private Scrambler scrambler = new RubiksCubeScrambler();

	private State state = State.Idle;

	private Button timer;
	private TextView scrambleTextView;

	private float getDuration() {
		return (System.currentTimeMillis() - start) / 1000f;
	}

	public void setScramble(String scramble) {
		this.scramble = scramble;
		scrambleTextView.setText(scramble);
	}

	public String getScramble() {
		return scramble;
	}

	@Override
	public void onClick(View view) {
		float duration = getDuration();
		switch (state) {
		case Idle:
			state = State.Solving;
			start = System.currentTimeMillis();
			handler.postDelayed(timerRunnable, 0);
			sManager.registerListener(accelerometerSensorEventListener,
					accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
			break;
		case Solving:
			state = State.Idle;
			sManager.unregisterListener(accelerometerSensorEventListener);

			((MainActivity) getActivity()).getHistoryFragment().addSolve(
					duration);
			updateText(duration);

			setScramble(scrambler.generateScramble(random));
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_timer,
				container, false);

		scrambleTextView = (TextView) rootView.findViewById(R.id.scramble);
		setScramble(scrambler.generateScramble(random));

		sManager = (SensorManager) this.getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		timer = (Button) rootView.findViewById(R.id.timer);
		timer.setOnClickListener(this);

		return rootView;
	}

	private void updateText(float text) {
		timer.setText(String.format("%.2f", text));
	}
}