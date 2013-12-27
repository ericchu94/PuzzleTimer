package chu.eric.puzzletimer;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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

		LinearLayout layout = (LinearLayout) convertView;

		for (Iterator<Solve> iterator = match.getSolves().iterator(); iterator
				.hasNext();) {
			Solve solve = iterator.next();

			View view = inflater.inflate(R.layout.item_solve, null);

			TextView duration = (TextView) view
					.findViewById(R.id.solve_duration);
			TextView name = (TextView) view.findViewById(R.id.solve_name);

			duration.setText(solve.getFormattedDuration());
			int flags = duration.getPaintFlags();
			if (solve.getDnf()) {
				flags = flags | Paint.STRIKE_THRU_TEXT_FLAG;
			} else {
				flags = flags & (~Paint.STRIKE_THRU_TEXT_FLAG);
			}
			if (solve.getPlusTwo()) {
				duration.setTextColor(Color.RED);
			} else {
				duration.setTextColor(Color.BLACK);
			}
			duration.setPaintFlags(flags);
			name.setText(solve.getName());

			layout.addView(view, 0);
		}

		TextView scramble = (TextView) convertView
				.findViewById(R.id.solve_scramble);

		scramble.setText(match.getScramble());
		return convertView;
	}
}
