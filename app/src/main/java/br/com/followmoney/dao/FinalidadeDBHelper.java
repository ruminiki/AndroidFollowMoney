package br.com.followmoney.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.followmoney.domain.Finality;

import static android.R.attr.id;
import static br.com.followmoney.followmoney.R.string.descricao;

;

/**
 * Created by ruminiki on 08/09/2017.
 */

public class FinalidadeDBHelper extends AbstractGenericDao<Finality> {

    private static final String TABLE_NAME         = "finalidade";
    public static final  String COLUMN_DESCRIPTION = "descricao";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_DESCRIPTION + " TEXT);";

    public FinalidadeDBHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void insert(Finality finality) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, finality.getDescription());
        db.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void update(Finality finality) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, descricao);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
    }

    @Override
    public void delete(Finality finality) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { Integer.toString(finality.getId()) });
    }

    @Override
    public Finality get(Finality finality) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_ID + "=?", new String[] { Integer.toString(finality.getId()) } );

        res.moveToFirst();

        Finality f = new Finality();
        f.setId(res.getInt(res.getColumnIndex(COLUMN_ID)));
        f.setDescription(res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)));

        if (!res.isClosed()) { res.close(); }

        return f;
    }

    @Override
    public List<Finality> getAll() {
        List<Finality> result = new ArrayList<Finality>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );

        try {
            while (res.moveToNext()) {
                Finality f = new Finality();
                f.setId(res.getInt(res.getColumnIndex(COLUMN_ID)));
                f.setDescription(res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)));
                result.add(f);
            }
        } finally {
            res.close();
        }

        return result;
    }

}