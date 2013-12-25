package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryFragment extends Fragment {

	List<Solve> solves;
	private ArrayAdapter<Solve> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_history,
				container, false);

		preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

		helper = new SolvesOpenHelper(getActivity());
		solves = helper.getAllSolves();

		ListView solvesListView = (ListView) rootView.findViewById(R.id.solves);
		adapter = new ArrayAdapter<Solve>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, solves);
		solvesListView.setAdapter(adapter);

		return rootView;
	}

	private SolvesOpenHelper helper;

	public void addSolve(Solve solve) {
		solve.setId(helper.addSolve(solve));
		solves.add(solve);

		if (adapter != null) {
			// TODO Find out why adapter can be null
			adapter.notifyDataSetChanged();
		}
	}

	public void clearSolves() {
		helper.clearSolves();
		solves.clear();
		adapter.notifyDataSetChanged();
	}
}