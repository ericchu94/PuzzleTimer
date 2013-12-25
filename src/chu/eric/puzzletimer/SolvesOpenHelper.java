package chu.eric.puzzletimer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SolvesOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "puzzleTimer";
	private static final String TABLE_SOLVES = "solves";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_SCRAMBLE = "scramble";
	private static final String COLUMN_DURATION = "duration";
	private static final String COLUMN_PLUSTWO = "plusTwo";
	private static final String COLUMN_DNF = "dnf";
	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_SCRAMBLE,
			COLUMN_DURATION, COLUMN_PLUSTWO, COLUMN_DNF };

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(String
				.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s FLOAT, %s BIT, %s BIT);",
						TABLE_SOLVES, COLUMN_ID, COLUMN_SCRAMBLE,
						COLUMN_DURATION, COLUMN_PLUSTWO, COLUMN_DNF));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLVES);

		onCreate(db);
	}

	SolvesOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public int addSolve(Solve solve) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_DURATION, solve.getDuration());
		values.put(COLUMN_PLUSTWO, solve.getPlusTwo() ? 1 : 0);
		values.put(COLUMN_DNF, solve.getDnf() ? 1 : 0);

		int id = (int) db.insert(TABLE_SOLVES, null, values);
		db.close();

		return id;
	}

	public List<Solve> getAllSolves() {
		List<Solve> solves = new ArrayList<Solve>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_SOLVES, COLUMNS, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				Solve solve = new Solve(
						cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
						cursor.getString(cursor.getColumnIndex(COLUMN_SCRAMBLE)),
						cursor.getFloat(cursor.getColumnIndex(COLUMN_DURATION)),
						cursor.getInt(cursor.getColumnIndex(COLUMN_PLUSTWO)) != 0,
						cursor.getInt(cursor.getColumnIndex(COLUMN_DNF)) != 0);
				solves.add(solve);
			} while (cursor.moveToNext());
		}
		db.close();
		return solves;
	}

	public void deleteSolve(Solve solve) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_SOLVES, COLUMN_ID + " = ?",
				new String[] { Integer.toString(solve.getId()) });
		db.close();
	}

	public void clearSolves() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_SOLVES, null, null);
		db.close();
	}

	public void updateSolve(Solve solve) {
		// only support updating flags
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_PLUSTWO, solve.getPlusTwo() ? 1 : 0);
		values.put(COLUMN_DNF, solve.getDnf() ? 1 : 0);

		db.update(TABLE_SOLVES, values, COLUMN_ID + " = ?",
				new String[] { Integer.toString(solve.getId()) });

		db.close();
	}
}
