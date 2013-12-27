package chu.eric.puzzletimer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static final String PREF_NAME = "pref_name";
	public static final String PREF_ACCELEROMETER = "pref_accelerometer";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		updateEditTextPreferenceSummary(PREF_NAME);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_NAME)) {
			updateEditTextPreferenceSummary(key);
		}

	}

	private void updateEditTextPreferenceSummary(String key) {
		EditTextPreference preference = (EditTextPreference) findPreference(key);
		preference.setSummary(preference.getText());
	}

	@Override
	public void onResume() {
		super.onResume();

		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

}
