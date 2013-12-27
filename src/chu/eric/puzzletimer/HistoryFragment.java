package chu.eric.puzzletimer;

import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HistoryFragment extends Fragment implements OnItemClickListener {

	List<Match> matches;
	private MatchesAdapter adapter;
	private PuzzleTimerOpenHelper helper;
	private ListView matchesListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_history,
				container, false);

		helper = new PuzzleTimerOpenHelper(getActivity());
		matches = helper.getAllMatches();

		matchesListView = (ListView) rootView.findViewById(R.id.solves);
		adapter = new MatchesAdapter(getActivity(), R.layout.item_match,
				matches);
		matchesListView.setAdapter(adapter);

		matchesListView.setOnItemClickListener(this);

		return rootView;
	}

	public void addSolve(String scramble, float duration) {
		Match match = null;
		int index = 0;
		for (Iterator<Match> iterator = matches.iterator(); iterator.hasNext(); ++index) {
			Match m = iterator.next();

			if (m.getScramble().equals(scramble)) {
				match = m;
				break;
			}
		}

		boolean newMatch = false;
		if (match == null) {
			newMatch = true;

			match = new Match(scramble);
			match.setId(helper.addMatch(match));
			matches.add(0, match);
		}

		Solve solve = new Solve(match.getId(), duration, PreferenceManager
				.getDefaultSharedPreferences(getActivity()).getString(
						SettingsActivity.PREF_NAME, ""));
		solve.setId(helper.addSolve(solve));
		match.addSolve(solve);

		if (newMatch) {
			adapter.notifyDataSetChanged();
		} else {
			adapter.getView(index, matchesListView.getChildAt(index),
					matchesListView);
		}
	}

	public void clearSolves() {
		helper.clearMatches();
		matches.clear();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		final Match match = (Match) parent.getItemAtPosition(position);
		final Solve solve = match.getPersonalSolve();
		final boolean[] checkedItems = new boolean[] { solve.getPlusTwo(),
				solve.getDnf() };

		// popup dialog with 2 toggle buttons, one delete
		new AlertDialog.Builder(getActivity())
				.setMultiChoiceItems(
						new String[] { getString(R.string.plusTwo),
								getString(R.string.dnf) }, checkedItems,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								checkedItems[which] = isChecked;
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								solve.setPlusTwo(checkedItems[0]);
								solve.setDnf(checkedItems[1]);
								helper.updateSolve(solve);

								adapter.getView(position,
										matchesListView.getChildAt(position),
										matchesListView);
							}
						})
				.setNeutralButton(android.R.string.cancel, null)
				.setNegativeButton(R.string.delete,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								helper.deleteMatch(match);
								matches.remove(match);
								adapter.notifyDataSetChanged();
							}
						}).show();
	}
}