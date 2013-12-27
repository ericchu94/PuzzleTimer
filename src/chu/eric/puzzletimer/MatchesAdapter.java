package chu.eric.puzzletimer;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import chu.eric.puzzletimer.dal.Match;
import chu.eric.puzzletimer.dal.Solve;

public class MatchesAdapter extends ArrayAdapter<Match> {

	private List<Match> objects;

	public MatchesAdapter(Context context, int resource, List<Match> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_match, null);
		}

		Match match = objects.get(position);

		Collections.sort(match.getSolves());

		LinearLayout solves = (LinearLayout) convertView
				.findViewById(R.id.solves);
		solves.removeAllViews();

		for (Iterator<Solve> iterator = match.getSolves().iterator(); iterator
				.hasNext();) {
			Solve solve = iterator.next();

			View view = inflater.inflate(R.layout.item_solve, null);

			setupSolveView(view, solve);

			solves.addView(view, 0);
		}

		TextView scramble = (TextView) convertView
				.findViewById(R.id.solve_scramble);

		scramble.setText(match.getScramble());
		return convertView;
	}

	private void setupSolveView(View view, Solve solve) {
		TextView duration = (TextView) view.findViewById(R.id.solve_duration);
		TextView name = (TextView) view.findViewById(R.id.solve_name);

		duration.setText(solve.getDurationString());
		int flags = duration.getPaintFlags();
		if (solve.getDnf()) {
			flags = flags | Paint.STRIKE_THRU_TEXT_FLAG;
		}
		if (solve.getPlusTwo()) {
			duration.setTextColor(Color.RED);
		}
		if (solve.getPersonal()) {
			name.setTypeface(null, Typeface.BOLD);
		}
		duration.setPaintFlags(flags);
		name.setText(solve.getName());
	}
}
