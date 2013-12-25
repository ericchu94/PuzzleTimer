package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryFragment extends Fragment {

	private static final String ARG_SOLVES = "solves";

	ArrayList<String> solves = null;
	private ArrayAdapter<String> adapter;

	private SharedPreferences preferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_history,
				container, false);

		preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

		if (savedInstanceState != null) {
			solves = savedInstanceState.getStringArrayList(ARG_SOLVES);
		} else {
			Set<String> set = preferences.getStringSet(ARG_SOLVES, null);
			if (set != null) {
				solves = new ArrayList<String>(set);
			}
		}

		if (solves == null) {
			solves = new ArrayList<String>();
		}

		ListView solvesListView = (ListView) rootView.findViewById(R.id.solves);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, solves);
		solvesListView.setAdapter(adapter);

		return rootView;
	}

	public void addSolve(float duration) {
		// TODO: Accept scramble. Maybe create a solve class, etc
		solves.add(String.format("%.2f", duration));

		if (adapter != null) {
			// TODO Find out why adapter can be null
			adapter.notifyDataSetChanged();
		}
	}

	public void clearSolves() {
		solves.clear();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putStringArrayList(ARG_SOLVES, solves);
	}

	@Override
	public void onPause() {
		super.onPause();

		// Save commit
		Editor editor = preferences.edit();
		editor.putStringSet(ARG_SOLVES, new HashSet<String>(solves));
		editor.commit();
	}
}