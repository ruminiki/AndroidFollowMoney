package br.com.followmoney.activities.movements;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.finalities.DeleteFinality;
import br.com.followmoney.dao.remote.movements.GetMovement;
import br.com.followmoney.dao.remote.movements.PostMovement;
import br.com.followmoney.dao.remote.movements.PutMovement;
import br.com.followmoney.domain.Movement;

public class MovementCreateOrEditActivity extends AppCompatActivity implements View.OnClickListener{

    EditText descricaoEditText, emissaoEditText, vencimentoEditText;
    RadioButton creditoRadio, debitoRadio;
    Button saveButton, deleteButton;

    int movimentoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_create_or_edit);

        movimentoID = getIntent().getIntExtra(MovementListActivity.KEY_EXTRA_MOVEMENT_ID, 0);
        descricaoEditText = (EditText) findViewById(R.id.descricaoEditText);
        emissaoEditText = (EditText) findViewById(R.id.emissaoEditText);
        vencimentoEditText = (EditText) findViewById(R.id.vencimentoEditText);
        creditoRadio = (RadioButton) findViewById(R.id.creditoRadio);
        debitoRadio = (RadioButton) findViewById(R.id.debitoRadio);

        saveButton = (Button) findViewById( R.id.saveButton);
        saveButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        if (movimentoID > 0) {
            new GetMovement(new GetMovement.OnLoadListener() {
                @Override
                public void onLoaded(Movement movement) {
                    saveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    descricaoEditText.setText(movement.getDescricao());
                    descricaoEditText.setEnabled(true);
                    descricaoEditText.setFocusableInTouchMode(true);
                    descricaoEditText.setClickable(true);

                    emissaoEditText.setText(movement.getEmissao());
                    emissaoEditText.setEnabled(true);
                    emissaoEditText.setClickable(true);

                    vencimentoEditText.setText(movement.getVencimento());
                    vencimentoEditText.setEnabled(true);
                    vencimentoEditText.setClickable(true);

                    creditoRadio.setSelected(movement.getOperacao().equals(Movement.CREDIT));
                    debitoRadio.setSelected(movement.getOperacao().equals(Movement.DEBIT));
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(movimentoID);

        }else{
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        }
    }

    public void persist() {
        if(movimentoID > 0) {
            new PutMovement(new PutMovement.OnLoadListener() {
                @Override
                public void onLoaded(Movement movement) {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MovementListActivity.class);
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
            new PostMovement(new PostMovement.OnLoadListener() {
                @Override
                public void onLoaded(Movement movement) {
                    Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MovementListActivity.class);
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
            public void onLoaded(String response) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MovementListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(movimentoID);
    }

    private Movement getValueDataFieldsInView(){
        Movement m = new Movement();
        m.setId(movimentoID);
        m.setDescricao(descricaoEditText.getText().toString());
        m.setEmissao(emissaoEditText.getText().toString());
        m.setVencimento(vencimentoEditText.getText().toString());
        m.setOperacao(creditoRadio.isSelected() ? Movement.CREDIT : Movement.DEBIT);
        m.setUsuario(3);
        return m;
    }

}
