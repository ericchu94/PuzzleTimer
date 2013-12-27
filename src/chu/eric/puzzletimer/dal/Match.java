package chu.eric.puzzletimer.dal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Match {

	private int id;
	private String scramble;
	private List<Solve> solves = new ArrayList<Solve>();

	public Match(int id, String scramble) {
		this.id = id;
		this.scramble = scramble;
	}

	public Match(String scramble) {
		this.scramble = scramble;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getScramble() {
		return scramble;
	}

	public void setScramble(String scramble) {
		this.scramble = scramble;
	}

	public void addSolve(Solve solve) {
		solves.add(solve);
	}

	public Solve getPersonalSolve() {
		Solve solve = null;
		for (Iterator<Solve> iterator = solves.iterator(); iterator.hasNext()
				&& !(solve = iterator.next()).getPersonal();)
			;
		return solve;
	}

	public List<Solve> getSolves() {
		return solves;
	}
}
