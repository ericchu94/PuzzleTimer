package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SolvesOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "puzzleTimer";
	private static final String SOLVES_TABLE_NAME = "solves";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_DURATION = "duration";
	private static final String COLUMN_PLUSTWO = "plusTwo";
	private static final String COLUMN_DNF = "dnf";
	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_DURATION,
			COLUMN_PLUSTWO, COLUMN_DNF };
	private static final String SOLVES_TABLE_CREATE = "CREATE TABLE "
			+ SOLVES_TABLE_NAME + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DURATION
			+ " FLOAT, " + COLUMN_PLUSTWO + " BIT, " + COLUMN_DNF + " BIT);";

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SOLVES_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

	SolvesOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public int addSolve(Solve solve) {
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_DURATION, solve.getDuration());
		values.put(COLUMN_PLUSTWO, solve.getPlusTwo() ? 1 : 0);
		values.put(COLUMN_DNF, solve.getDnf() ? 1 : 0);

		int id = (int) database.insert(SOLVES_TABLE_NAME, null, values);
		database.close();

		return id;
	}

	public List<Solve> getAllSolves() {
		List<Solve> solves = new ArrayList<Solve>();

		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(SOLVES_TABLE_NAME, COLUMNS, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Solve solve = new Solve(cursor.getInt(cursor
						.getColumnIndex(COLUMN_ID)), cursor.getFloat(cursor
						.getColumnIndex(COLUMN_DURATION)), cursor.getInt(cursor
						.getColumnIndex(COLUMN_PLUSTWO)) != 0,
						cursor.getInt(cursor.getColumnIndex(COLUMN_DNF)) != 0);
				solves.add(solve);
			} while (cursor.moveToNext());
		}
		database.close();
		return solves;
	}

	public void deleteSolve(Solve solve) {
		SQLiteDatabase database = getWritableDatabase();
		database.delete(SOLVES_TABLE_NAME, COLUMN_ID + " = ?",
				new String[] { Integer.toString(solve.getId()) });
		database.close();
	}
}
