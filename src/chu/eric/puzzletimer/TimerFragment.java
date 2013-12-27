package chu.eric.puzzletimer;

import java.util.Random;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TimerFragment extends Fragment implements OnClickListener {

	public static final String ARG_SCRAMBLE = "scramble";
	public static final String ARG_STATE = "state";
	public static final String ARG_START = "start";
	public static final String ARG_DURATION = "duration";

	enum State {
		Idle, Solving
	}

	private Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (state == State.Solving) {
				float duration = getDuration();
				setDuration(duration);
				Log.d("TIMER_RUNNABLE", String.format("%.2f", duration));
				handler.postDelayed(timerRunnable, 0);
			} else {
				// Save shit moved from button click (off ui thread)
				timer.setKeepScreenOn(false);
				sManager.unregisterListener(accelerometerSensorEventListener);

				((MainActivity) getActivity()).getHistoryFragment().addSolve(
						TextUtils.join(" ", scramble), duration);

				setScramble(scrambler.generateScramble(random));

				timer.setClickable(false);
				handler.postDelayed(timerClickableRunnable, INITIAL_DELAY);
			}
		}
	};

	private Runnable accelerometerRunnable = new Runnable() {
		@Override
		public void run() {
			timer.performClick();
		}
	};

	private Runnable accelerometerStartRunnable = new Runnable() {
		@Override
		public void run() {
			sManager.registerListener(accelerometerSensorEventListener,
					accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		}
	};

	private Runnable timerClickableRunnable = new Runnable() {
		@Override
		public void run() {
			timer.setClickable(true);
		}
	};

	private static final int INITIAL_DELAY = 500;

	private long start;
	private String[] scramble;
	private float duration;

	private SensorManager sManager;
	private Sensor accelerometer;

	private AccelerometerSensorEventListener accelerometerSensorEventListener = new AccelerometerSensorEventListener(
			accelerometerRunnable);

	private Random random = new Random();
	private Handler handler = new Handler();
	private Scrambler scrambler = new RubiksCubeScrambler();

	private State state;

	private Button timer;
	private TextView scrambleTextView;

	private float getDuration() {
		return (System.currentTimeMillis() - start) / 1000f;
	}

	public void setScramble(String[] scramble) {
		this.scramble = scramble;

		String[] first = new String[scramble.length / 2];
		String[] second = new String[scramble.length / 2];
		System.arraycopy(scramble, 0, first, 0, first.length);
		System.arraycopy(scramble, second.length, second, 0, second.length);

		scrambleTextView.setText(String.format("%s\n%s",
				TextUtils.join(" ", first), TextUtils.join(" ", second)));
	}

	@Override
	public void onClick(View view) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				switch (state) {
				case Idle:
					state = State.Solving;
					timer.setKeepScreenOn(true);
					start = System.currentTimeMillis();
					handler.postDelayed(timerRunnable, 0);
					if (PreferenceManager.getDefaultSharedPreferences(
							getActivity()).getBoolean(
							SettingsActivity.PREF_ACCELEROMETER, true)) {
						handler.postDelayed(accelerometerStartRunnable,
								INITIAL_DELAY);
					}
					break;
				case Solving:
					state = State.Idle;
					break;
				}
			}
		}, 0);
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

		if (savedInstanceState != null) {
			setScramble(savedInstanceState.getStringArray(ARG_SCRAMBLE));
			state = State.values()[savedInstanceState.getInt(ARG_STATE, 0)];
			start = savedInstanceState.getLong(ARG_START);

			// Timer running states need to start timer
			if (state == State.Solving) {
				handler.postDelayed(timerRunnable, 0);
				timer.setKeepScreenOn(true);
			} else {
				// Only restore text state when static
				setDuration(savedInstanceState.getFloat(ARG_DURATION, 0));
			}
		} else {
			// defaults
			setScramble(scrambler.generateScramble(random));
			state = State.Idle;
			setDuration(0);
		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putStringArray(ARG_SCRAMBLE, scramble);
		outState.putInt(ARG_STATE, state.ordinal());
		outState.putLong(ARG_START, start);
		outState.putFloat(ARG_DURATION, duration);
	}

	private void setDuration(float duration) {
		this.duration = duration;
		timer.setText(String.format("%.2f", duration));
	}
}