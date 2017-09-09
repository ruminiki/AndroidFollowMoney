package br.com.followmoney.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import br.com.followmoney.dao.FinalidadeDBHelper;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.followmoney.R;

public class FinalityCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    private FinalidadeDBHelper dbHelper ;
    EditText descricaoEditText;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    int finalidadeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finality_create_or_edit);

        finalidadeID = getIntent().getIntExtra(FinalityListActivity.KEY_EXTRA_CONTACT_ID, 0);
        descricaoEditText = (EditText) findViewById(R.id.editTextDescricao);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new FinalidadeDBHelper(this);

        if (finalidadeID > 0) {
            saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            Finality f = dbHelper.get(new Finality(finalidadeID));

            descricaoEditText.setText(f.getDescription());
            descricaoEditText.setFocusable(false);
            descricaoEditText.setClickable(false);
        }
    }

    public void persist() {
        if(finalidadeID > 0) {
            try{
                dbHelper.update(getViewDataFields());
                Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FinalityListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            try{
                dbHelper.insert(getViewDataFields());
                Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Could not Insert object", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(getApplicationContext(), FinalityListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persist();
                return;
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                descricaoEditText.setEnabled(true);
                descricaoEditText.setFocusableInTouchMode(true);
                descricaoEditText.setClickable(true);

                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.delete(new Finality(finalidadeID));
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), FinalityListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Object?");
                d.show();
                return;
        }
    }

    private Finality getViewDataFields(){
        Finality f = new Finality();
        f.setDescription(descricaoEditText.getText().toString());
        return f;
    }
}
