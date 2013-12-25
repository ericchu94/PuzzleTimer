package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.text.TextUtils;

public class RubiksCubeScrambler extends Scrambler {

	private static final int MOVES = 20;

	@Override
	public String generateScramble(Random random) {

		List<Move> scramble = new ArrayList<Move>();

		for (int i = 0; i < MOVES; ++i) {
			Face[] faces = Face.values();
			List<Face> validFaces = new ArrayList<Face>(Arrays.asList(faces));
			if (i > 0) {
				Face face1 = scramble.get(i - 1).face;
				validFaces.remove(face1);

				if (i > 1) {
					Face face2 = scramble.get(i - 2).face;

					int length = faces.length;
					if ((face2.ordinal() + length / 2) % length == face1
							.ordinal()) {
						validFaces.remove(face2);
					}
				}
			}
			Face face = validFaces.get(random.nextInt(validFaces.size()));
			Turn[] turns = Turn.values();
			Turn turn = turns[random.nextInt(turns.length)];
			scramble.add(new Move(face, turn));
		}

		return TextUtils.join(" ", scramble);
	}

	@Override
	public ScrambleTarget getScrambleTarget() {
		return ScrambleTarget.RubiksCube;
	}

	enum Face {
		Up("U"), Front("F"), Right("R"), Down("D"), Back("B"), Left("L");

		private Face(String notation) {
			this.notation = notation;
		}

		private final String notation;

		public String getNotation() {
			return notation;
		}
	}

	enum Turn {
		Clockwise(""), CounterClockwise("'"), Half("\"");

		private Turn(String notation) {
			this.notation = notation;
		}

		private final String notation;

		public String getNotation() {
			return notation;
		}
	}

	class Move {
		private Face face;
		private Turn turn;

		public Move(Face face, Turn turn) {
			this.face = face;
			this.turn = turn;
		}

		public Face getFace() {
			return face;
		}

		public Turn getTurn() {
			return turn;
		}

		@Override
		public String toString() {
			return String
					.format("%s%s", face.getNotation(), turn.getNotation());
		}
	}
}
