package chu.eric.puzzletimer.scramblers;

import java.util.Random;


public abstract class Scrambler {
	
	public abstract String[] generateScramble(Random random);
	
	
	public abstract ScrambleTarget getScrambleTarget();
	
	enum ScrambleTarget {
		RubiksCube
	}
}
