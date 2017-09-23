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
import br.com.followmoney.util.DateUtil;

import static br.com.followmoney.activities.AbstractFormList.KEY_EXTRA_ID;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_MOVEMENT_ID;

public class MovementDetailActivity extends AppCompatActivity {

    TextView descricaoTextView, emissaoTextView, vencimentoTextView, operacaoTextView, finalidadeTextView,
            formaPagamentoTextView, cartaoCreditoTextView, contaBancariaTextView, valorTextView;
    int movementID;

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
        formaPagamentoTextView = (TextView) findViewById(R.id.formaPagamentoTextView);
        cartaoCreditoTextView = (TextView) findViewById(R.id.cartaoCreditoTextView);
        contaBancariaTextView = (TextView) findViewById(R.id.contaBancariaTextView);
        valorTextView = (TextView) findViewById(R.id.valorTextView);

        new GetEntityJson<Movement>(new GetEntityJson.OnLoadListener<Movement>() {
            @Override
            public void onLoaded(Movement movement) {
                descricaoTextView.setText(movement.getDescricao());
                emissaoTextView.setText(DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
                vencimentoTextView.setText(DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
                operacaoTextView.setText(movement.getOperacao());
                finalidadeTextView.setText(movement.getFinalidade().getDescricao());
                formaPagamentoTextView.setText(movement.getFormaPagamento().getDescricao());
                cartaoCreditoTextView.setText(movement.getCartaoCredito() != null ? movement.getCartaoCredito().getDescricao() : "");
                contaBancariaTextView.setText(movement.getCartaoCredito() != null ? movement.getContaBancaria().getDescricao() : "");
                valorTextView.setText("R$ " + movement.getValor());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(getApplicationContext(), MovementCreateOrEditActivity.class);
                        intent.putExtra(KEY_EXTRA_ID, movementID);
                        startActivity(intent);
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
                        Toast.makeText(getApplicationContext(), "Please, you need select an object to delete!", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
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
            });
        }
    }

    private void confirmDelete(){
        new DeleteEntityJson(new DeleteEntityJson.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute("/movements/"+movementID);
    }

}
