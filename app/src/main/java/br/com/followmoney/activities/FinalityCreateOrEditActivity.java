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

import br.com.followmoney.dao.remote.finalities.DeleteFinality;
import br.com.followmoney.dao.remote.finalities.GetFinality;
import br.com.followmoney.dao.remote.finalities.PostFinality;
import br.com.followmoney.dao.remote.finalities.PutFinality;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.followmoney.R;

public class FinalityCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

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

        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        if (finalidadeID > 0) {
            saveButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);

            new GetFinality(new GetFinality.OnLoadListener() {
                @Override
                public void onLoaded(Finality finalityList) {
                    descricaoEditText.setText(finalityList.getDescricao());
                    descricaoEditText.setFocusable(false);
                    descricaoEditText.setClickable(false);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(finalidadeID);

        }
    }

    public void persist() {
        if(finalidadeID > 0) {
            new PutFinality(new PutFinality.OnLoadListener() {
                @Override
                public void onLoaded(Finality finality) {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FinalityListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(getValueDataFieldsInView());

        }
        else {
            new PostFinality(new PostFinality.OnLoadListener() {
                @Override
                public void onLoaded(Finality finality) {
                    Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FinalityListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Could not Insert object", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(getValueDataFieldsInView());
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
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);

                descricaoEditText.setEnabled(true);
                descricaoEditText.setFocusableInTouchMode(true);
                descricaoEditText.setClickable(true);

                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                confirmDelete();
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

    private void confirmDelete(){
        new DeleteFinality(new DeleteFinality.OnLoadListener() {
            @Override
            public void onLoaded(Finality finality) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FinalityListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(finalidadeID);
    }

    private Finality getValueDataFieldsInView(){
        Finality f = new Finality();
        f.setId(finalidadeID);
        f.setDescricao(descricaoEditText.getText().toString());
        f.setUsuario(3);
        return f;
    }

}
