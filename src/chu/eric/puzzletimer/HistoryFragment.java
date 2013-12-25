package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryFragment extends Fragment {

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

		if (adapter != null) {
			// TODO Find out why adapter can be null
			adapter.notifyDataSetChanged();
		}
	}

	public void clearSolves() {
		solves.clear();
		adapter.notifyDataSetChanged();
	}
}