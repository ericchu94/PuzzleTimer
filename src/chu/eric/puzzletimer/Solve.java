package chu.eric.puzzletimer;

import android.os.Parcel;
import android.os.Parcelable;

public class Solve implements Parcelable {
	private int id;
	private float duration;
	private boolean plusTwo = false;
	private boolean dnf = false;

	@Override
	public String toString() {
		return String.format("%.2f", duration);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(duration);
		dest.writeBooleanArray(new boolean[] { plusTwo, dnf });

	}

	public static final Parcelable.Creator<Solve> CREATOR = new Parcelable.Creator<Solve>() {
		public Solve createFromParcel(Parcel in) {
			return new Solve(in);
		}

		public Solve[] newArray(int size) {
			return new Solve[size];
		}
	};

	public Solve(Parcel in) {
		duration = in.readFloat();
		boolean[] booleanArray = new boolean[2];
		in.readBooleanArray(booleanArray);
		plusTwo = booleanArray[0];
		dnf = booleanArray[1];
	}

	public Solve(float duration) {
		this.duration = duration;
	}

	public Solve(int id, float duration, boolean plusTwo, boolean dnf) {
		this.id = id;
		this.duration = duration;
		this.plusTwo = plusTwo;
		this.dnf = dnf;
	}

	public float getDuration() {
		return duration;
	}

	public boolean getPlusTwo() {
		return plusTwo;
	}

	public boolean getDnf() {
		return dnf;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
