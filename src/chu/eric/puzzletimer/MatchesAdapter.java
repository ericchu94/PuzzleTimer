package chu.eric.puzzletimer;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MatchesAdapter extends ArrayAdapter<Match> {

	private List<Match> objects;

	public MatchesAdapter(Context context, int resource, List<Match> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_match, null);
		}

		Match match = objects.get(position);

		ListView solves = (ListView) convertView
				.findViewById(R.id.solve_solves);
		TextView scramble = (TextView) convertView
				.findViewById(R.id.solve_scramble);

		SolvesAdapter adapter = new SolvesAdapter(getContext(),
				R.layout.item_solve, match.getSolves());
		solves.setAdapter(adapter);

		scramble.setText(match.getScramble());
		return convertView;
	}
}
