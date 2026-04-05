package ma.fst.projet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ma.fst.projet.classes.Etudiant;
import ma.fst.projet.service.EtudiantService;

public class MainActivity extends AppCompatActivity {

    private EditText nom;
    private EditText prenom;
    private Button add;

    private EditText id;
    private Button rechercher;
    private Button supprimer;
    private TextView res;

    // Méthode pour vider les champs après l’ajout
    void clear() {
        nom.setText("");
        prenom.setText("");
        nom.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EtudiantService es = new EtudiantService(this);

        // Liaison avec l'interface XML
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        add = findViewById(R.id.bn);

        id = findViewById(R.id.id);
        rechercher = findViewById(R.id.load);
        supprimer = findViewById(R.id.delete);
        res = findViewById(R.id.res);

        // Bouton AJOUTER
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nom.getText().toString().isEmpty() || prenom.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez remplir le nom et le prénom", Toast.LENGTH_SHORT).show();
                    return;
                }

                es.create(new Etudiant(nom.getText().toString(), prenom.getText().toString()));
                clear();

                for (Etudiant e : es.findAll()) {
                    Log.d("SQLITE_APP", e.getId() + " : " + e.getNom() + " " + e.getPrenom());
                }

                Toast.makeText(MainActivity.this, "Étudiant ajouté !", Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton CHERCHER
        rechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = id.getText().toString().trim();
                if (txt.isEmpty()) {
                    res.setText("");
                    Toast.makeText(MainActivity.this, "Saisir un ID d'abord", Toast.LENGTH_SHORT).show();
                    return;
                }

                Etudiant e = es.findById(Integer.parseInt(txt));
                if (e == null) {
                    res.setText("");
                    Toast.makeText(MainActivity.this, "Étudiant introuvable", Toast.LENGTH_SHORT).show();
                    return;
                }

                res.setText(e.getNom() + " " + e.getPrenom());
            }
        });

        // Bouton SUPPRIMER
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = id.getText().toString().trim();
                if (txt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Saisir un ID d'abord", Toast.LENGTH_SHORT).show();
                    return;
                }

                Etudiant e = es.findById(Integer.parseInt(txt));
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Aucun étudiant à supprimer avec cet ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                es.delete(e);
                res.setText("");
                id.setText("");
                Toast.makeText(MainActivity.this, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();
            }
        });
    }
}