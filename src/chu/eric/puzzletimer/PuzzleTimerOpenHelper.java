package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PuzzleTimerOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION = 9;
	private static final String NAME = "puzzleTimer";

	private static final String MATCHES = "matches";
	private static final String MATCHES_ID = "_id";
	private static final String MATCHES_SCRAMBLE = "scramble";
	private static final String[] MATCHES_COLUMNS = { MATCHES_ID,
			MATCHES_SCRAMBLE };

	private static final String SOLVES = "solves";
	private static final String SOLVES_ID = "_id";
	private static final String SOLVES_MATCHID = "matchId";
	private static final String SOLVES_DURATION = "duration";
	private static final String SOLVES_PLUSTWO = "plusTwo";
	private static final String SOLVES_DNF = "dnf";
	private static final String SOLVES_PERSONAL = "personal";
	private static final String SOLVES_NAME = "name";
	private static final String[] SOLVES_COLUMNS = { SOLVES_ID, SOLVES_MATCHID,
			SOLVES_DURATION, SOLVES_PLUSTWO, SOLVES_DNF, SOLVES_PERSONAL,
			SOLVES_NAME };

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(String
				.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT);",
						MATCHES, MATCHES_ID, MATCHES_SCRAMBLE));
		db.execSQL(String
				.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s FLOAT, %s BIT, %s BIT, %s BIT, %s TEXT);",
						SOLVES, SOLVES_ID, SOLVES_MATCHID, SOLVES_DURATION,
						SOLVES_PLUSTWO, SOLVES_DNF, SOLVES_PERSONAL,
						SOLVES_NAME));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MATCHES);
		db.execSQL("DROP TABLE IF EXISTS " + SOLVES);

		onCreate(db);
	}

	PuzzleTimerOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	public int addMatch(Match match) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MATCHES_SCRAMBLE, match.getScramble());

		int id = (int) db.insert(MATCHES, null, values);
		db.close();

		return id;
	}

	public List<Solve> getAllSolves() {
		List<Solve> solves = new ArrayList<Solve>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(SOLVES, SOLVES_COLUMNS, null, null, null,
				null, MATCHES_ID + " DESC");
		if (cursor.moveToFirst()) {
			do {
				Solve solve = new Solve(
						cursor.getInt(cursor.getColumnIndex(SOLVES_ID)),
						cursor.getInt(cursor.getColumnIndex(SOLVES_MATCHID)),
						cursor.getFloat(cursor.getColumnIndex(SOLVES_DURATION)),
						cursor.getInt(cursor.getColumnIndex(SOLVES_PLUSTWO)) != 0,
						cursor.getInt(cursor.getColumnIndex(SOLVES_DNF)) != 0,
						cursor.getInt(cursor.getColumnIndex(SOLVES_PERSONAL)) != 0,
						cursor.getString(cursor.getColumnIndex(SOLVES_NAME)));
				solves.add(solve);
			} while (cursor.moveToNext());
		}
		db.close();
		return solves;
	}

	public List<Match> getAllMatches() {
		List<Match> matches = new ArrayList<Match>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(MATCHES, MATCHES_COLUMNS, null, null, null,
				null, MATCHES_ID + " DESC");
		if (cursor.moveToFirst()) {

			List<Solve> allSolves = getAllSolves();
			ListIterator<Solve> iterator = allSolves.listIterator();

			Solve solve = iterator.next();
			do {
				Match match = new Match(cursor.getInt(cursor
						.getColumnIndex(MATCHES_ID)), cursor.getString(cursor
						.getColumnIndex(MATCHES_SCRAMBLE)));

				do {
					match.addSolve(solve);

					if (!iterator.hasNext())
						break;

					solve = iterator.next();
				} while (solve.getMatchId() == match.getId());

				matches.add(match);
			} while (cursor.moveToNext());
		}
		db.close();
		return matches;
	}

	public void deleteMatch(Match match) {
		SQLiteDatabase db = getWritableDatabase();
		String[] whereArgs = new String[] { Integer.toString(match.getId()) };
		db.delete(MATCHES, MATCHES_ID + " = ?", whereArgs);
		db.delete(SOLVES, SOLVES_MATCHID + " = ?", whereArgs);
		db.close();
	}

	public void clearMatches() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(MATCHES, null, null);
		db.delete(SOLVES, null, null);
		db.close();
	}

	public void updateSolve(Solve solve) {
		// only support updating flags
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SOLVES_PLUSTWO, solve.getPlusTwo() ? 1 : 0);
		values.put(SOLVES_DNF, solve.getDnf() ? 1 : 0);

		db.update(SOLVES, values, SOLVES_ID + " = ?",
				new String[] { Integer.toString(solve.getId()) });

		db.close();
	}

	public int addSolve(Solve solve) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SOLVES_MATCHID, solve.getMatchId());
		values.put(SOLVES_DURATION, solve.getDuration());
		values.put(SOLVES_PLUSTWO, solve.getPlusTwo() ? 1 : 0);
		values.put(SOLVES_DNF, solve.getDnf() ? 1 : 0);
		values.put(SOLVES_PERSONAL, solve.getPersonal() ? 1 : 0);
		values.put(SOLVES_NAME, solve.getName());

		int id = (int) db.insert(SOLVES, null, values);
		db.close();

		return id;
	}

}
