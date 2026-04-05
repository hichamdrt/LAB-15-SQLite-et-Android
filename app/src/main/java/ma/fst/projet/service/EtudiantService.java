package ma.fst.projet.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import ma.fst.projet.classes.Etudiant;
import ma.fst.projet.util.MySQLiteHelper;
public class EtudiantService {

    private static final String TABLE_NAME = "etudiant";
    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String[] COLUMNS = {KEY_ID, KEY_NOM, KEY_PRENOM};

    private final MySQLiteHelper helper;

    public EtudiantService(Context context) {
        this.helper = new MySQLiteHelper(context);
    }

    // CREATE : Ajouter un étudiant
    public void create(Etudiant e) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, e.getNom());
        values.put(KEY_PRENOM, e.getPrenom());

        db.insert(TABLE_NAME, null, values);
        Log.d("insert", "Ajout de : " + e.getNom());
        db.close();
    }

    // READ : Trouver par ID
    public Etudiant findById(int id) {
        Etudiant e = null;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, COLUMNS, "id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (c.moveToFirst()) {
            e = new Etudiant();
            e.setId(c.getInt(0));
            e.setNom(c.getString(1));
            e.setPrenom(c.getString(2));
        }
        c.close();
        db.close();
        return e;
    }

    // READ : Lister tout le monde
    public List<Etudiant> findAll() {
        List<Etudiant> eds = new ArrayList<>();
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Etudiant e = new Etudiant();
                e.setId(c.getInt(0));
                e.setNom(c.getString(1));
                e.setPrenom(c.getString(2));
                eds.add(e);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return eds;
    }

    // DELETE : Supprimer un étudiant
    public void delete(Etudiant e) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(e.getId())});
        db.close();
    }
}