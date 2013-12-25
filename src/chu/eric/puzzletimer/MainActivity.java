package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_clear:
			historyFragment.solves.clear();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	private Fragment timerFragment = new TimerFragment();
	private HistoryFragment historyFragment = new HistoryFragment();

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return timerFragment;
			case 1:
				return historyFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	public static class TimerFragment extends Fragment implements
			OnClickListener {

		enum State {
			Idle, Solving
		}

		class AccelerometerSensorEventListener implements SensorEventListener {

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

			List<SensorValue> sensorValues = new ArrayList<SensorValue>();

			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			private final int contactDuration = 200;

			public double getChange() {
				long start = System.currentTimeMillis() - contactDuration;
				double min = Double.MAX_VALUE;
				double max = Double.MIN_VALUE;

				ListIterator<SensorValue> iterator = sensorValues
						.listIterator();
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

			private static final float changeThreshhold = 0.5f;

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				double acceleration = getAcceleration(event.values[0],
						event.values[1], event.values[2]);
				sensorValues.add(new SensorValue(acceleration));

				if (getChange() > changeThreshhold) {
					timer.performClick();
				}

			}

			private double getAcceleration(float x, float y, float z) {
				return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
						+ Math.pow(z, 2));
			}
		}

		private SensorManager sManager;
		private Sensor accelerometer;

		private AccelerometerSensorEventListener accelerometerSensorEventListener = new AccelerometerSensorEventListener();

		private Scrambler scrambler = new RubiksCubeScrambler();
		private long start;
		private State state = State.Idle;
		private Random random = new Random();
		private Handler handler = new Handler();
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
		private Button timer;
		private TextView scramble;

		public TimerFragment() {
		}

		private float getDuration() {
			return (System.currentTimeMillis() - start) / 1000f;
		}

		private void updateText(float text) {
			timer.setText(String.format("%.2f", text));
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_timer,
					container, false);

			scramble = (TextView) rootView.findViewById(R.id.scramble);
			scramble.setText(scrambler.generateScramble(random));

			sManager = (SensorManager) this.getActivity().getSystemService(
					SENSOR_SERVICE);
			accelerometer = sManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

			timer = (Button) rootView.findViewById(R.id.timer);
			timer.setOnClickListener(this);

			return rootView;
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

				((MainActivity) getActivity()).historyFragment
						.addSolve(duration);
				updateText(duration);

				scramble.setText(scrambler.generateScramble(random));
				break;
			}
		}
	}

	public static class HistoryFragment extends Fragment {

		public HistoryFragment() {
		}

		List<String> solves = new ArrayList<String>();
		private ArrayAdapter<String> adapter;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_history,
					container, false);

			ListView solves = (ListView) rootView.findViewById(R.id.solves);
			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, android.R.id.text1,
					this.solves);
			solves.setAdapter(adapter);

			return rootView;
		}

		public void addSolve(float duration) {
			// TODO: Accept scramble. Maybe create a solve class, etc
			solves.add(String.format("%.2f", duration));
			adapter.notifyDataSetChanged();
		}
	}
}
