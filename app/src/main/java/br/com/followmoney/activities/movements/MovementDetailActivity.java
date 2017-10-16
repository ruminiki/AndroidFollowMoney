package br.com.followmoney.activities.movements;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.DeleteEntityJson;
import br.com.followmoney.dao.remote.GetEntityJson;
import br.com.followmoney.domain.Movement;

import static br.com.followmoney.activities.AbstractFormList.KEY_EXTRA_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_MOVEMENT_ID;

public class MovementDetailActivity extends AppCompatActivity {

    TextView descricaoTextView, emissaoTextView, vencimentoTextView, operacaoTextView, finalidadeTextView,
             fontePagadoraTextView, valorTextView;
    int movementID;
    Movement movement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_detail);

        movementID = getIntent().getIntExtra(KEY_EXTRA_MOVEMENT_ID, 0);
        descricaoTextView = (TextView) findViewById(R.id.descricaoTextView);
        emissaoTextView = (TextView) findViewById(R.id.emissaoTextView);
        vencimentoTextView = (TextView) findViewById(R.id.vencimentoTextView);
        operacaoTextView = (TextView) findViewById(R.id.operacaoTextView);
        finalidadeTextView = (TextView) findViewById(R.id.finalidadeTextView);
        fontePagadoraTextView = (TextView) findViewById(R.id.fontePagadoraTextView);
        valorTextView = (TextView) findViewById(R.id.valorTextView);

        new GetEntityJson<Movement>(new GetEntityJson.OnLoadListener<Movement>() {
            @Override
            public void onLoaded(Movement entity) {
                movement = entity;
                descricaoTextView.setText(movement.getDescricao());
                emissaoTextView.setText(movement.getEmissaoFormatado());
                vencimentoTextView.setText(movement.getVencimentoFormatado());
                operacaoTextView.setText(movement.getOperacao());
                finalidadeTextView.setText(movement.getFinality().getDescricao());
                fontePagadoraTextView.setText(movement.getFontePagadora());
                valorTextView.setText(movement.getValorFormatado());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, this).execute("/movements/"+movementID, new TypeToken<Movement>(){}.getType());

        ImageButton editButton = (ImageButton) findViewById(R.id.editButton);
        if ( editButton != null ) {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movementID <= 0) {
                        Toast.makeText(getApplicationContext(), "Please, you need select an object to edit!", Toast.LENGTH_SHORT).show();
                    } else {
                        if ( !movement.canEdit() ){
                            Toast.makeText(getApplicationContext(), movement.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(getApplicationContext(), MovementCreateOrEditActivity.class);
                            intent.putExtra(KEY_EXTRA_ID, movementID);
                            startActivityForResult(intent, RESULT_OK);
                        }
                    }
                }
            });
        }

        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        if ( deleteButton != null ) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movementID <= 0) {
                        Toast.makeText(getApplicationContext(), "Please, select an object to delete!", Toast.LENGTH_SHORT).show();
                    } else {
                        if ( !movement.canDelete() ){
                            Toast.makeText(getApplicationContext(), movement.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
                            builder.setMessage(R.string.delete)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        confirmDelete();
                                    }
                                });
                            AlertDialog d = builder.create();
                            d.setTitle("Delete object?");
                            d.show();
                            return;
                        }
                    }
                }
            });
        }
    }

    private void confirmDelete(){
        new DeleteEntityJson(new DeleteEntityJson.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Deleted successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, this).execute("/movements/"+movementID);
    }

}
