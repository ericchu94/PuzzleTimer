package chu.eric.puzzletimer;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryFragment extends Fragment implements
		OnItemLongClickListener, OnItemClickListener {

	List<Solve> solves;
	private ArrayAdapter<Solve> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_history,
				container, false);

		helper = new SolvesOpenHelper(getActivity());
		solves = helper.getAllSolves();

		ListView solvesListView = (ListView) rootView.findViewById(R.id.solves);
		adapter = new ArrayAdapter<Solve>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, solves);
		solvesListView.setAdapter(adapter);

		solvesListView.setOnItemClickListener(this);
		solvesListView.setOnItemLongClickListener(this);

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

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		final Solve solve = (Solve) parent.getItemAtPosition(position);
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
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								solve.setPlusTwo(checkedItems[0]);
								solve.setDnf(checkedItems[1]);
								helper.updateSolve(solve);
								adapter.notifyDataSetChanged();
							}
						})
				.setNeutralButton(R.string.cancel, null)
				.setNegativeButton(R.string.delete,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								helper.deleteSolve(solve);
								solves.remove(solve);
								adapter.notifyDataSetChanged();
							}
						}).show();
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Solve solve = (Solve) parent.getItemAtPosition(position);

		new AlertDialog.Builder(getActivity()).setMessage(
				String.format("%s: %s\n%s: %.2f\n%s: %s\n%s: %s",
						getString(R.string.scramble), solve.getScramble(),
						getString(R.string.duration), solve.getDuration(),
						getString(R.string.plusTwo), solve.getPlusTwo(),
						getString(R.string.dnf), solve.getDnf())).show();
	}
}