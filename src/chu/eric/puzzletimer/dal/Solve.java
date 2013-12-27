package chu.eric.puzzletimer.dal;

import java.util.Locale;

public class Solve implements Comparable<Solve> {
	private int id;
	private int matchId;
	private float duration;
	private boolean plusTwo = false;
	private boolean dnf = false;
	private boolean personal = true;
	private String name;

	public Solve(int matchId, float duration, String name) {
		this.matchId = matchId;
		this.duration = duration;
		this.name = name;
	}

	public Solve(int id, int matchId, float duration, boolean plusTwo,
			boolean dnf, boolean personal, String name) {
		this.id = id;
		this.matchId = matchId;
		this.duration = duration;
		this.plusTwo = plusTwo;
		this.dnf = dnf;
		this.personal = personal;
		this.name = name;
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

	private float getFormattedDuration() {
		return duration + (plusTwo ? 2 : 0);
	}

	public String getDurationString() {
		return String.format(Locale.US, "%.2f", getFormattedDuration());
	}

	public int getMatchId() {
		return matchId;
	}

	public boolean getPersonal() {
		return personal;
	}

	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Solve another) {
		return Float.compare(another.getFormattedDuration(),
				getFormattedDuration());

	}
}
