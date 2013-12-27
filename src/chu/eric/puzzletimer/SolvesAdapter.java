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

public class SolvesAdapter extends ArrayAdapter<Solve> {

	private List<Solve> objects;

	public SolvesAdapter(Context context, int resource, List<Solve> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_solve, null);
		}

		Solve solve = objects.get(position);

		TextView duration = (TextView) convertView
				.findViewById(R.id.solve_duration);
		TextView name = (TextView) convertView.findViewById(R.id.solve_name);

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

		return convertView;

	}
}
