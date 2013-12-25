package chu.eric.puzzletimer;

import android.os.Parcel;
import android.os.Parcelable;

public class Solve {
	private int id;
	private float duration;
	private boolean plusTwo = false;
	private boolean dnf = false;
	private String scramble;

	@Override
	public String toString() {
		return String.format("%.2f", duration + (plusTwo ? 2 : 0));
	}

	public Solve(String scramble, float duration) {
		this.scramble = scramble;
		this.duration = duration;
	}

	public Solve(int id, String scramble, float duration, boolean plusTwo,
			boolean dnf) {
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

	public void setPlusTwo(boolean plusTwo) {
		this.plusTwo = plusTwo;
	}

	public void setDnf(boolean dnf) {
		this.dnf = dnf;
	}

	public String getScramble() {
		return scramble;
	}
}
