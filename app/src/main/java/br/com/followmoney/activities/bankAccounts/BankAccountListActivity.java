package br.com.followmoney.activities.bankAccounts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.SelectableActivity;
import br.com.followmoney.dao.remote.bankAccounts.GetBankAccounts;
import br.com.followmoney.dao.remote.creditCards.DeleteCreditCard;
import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.domain.Finality;

public class BankAccountListActivity extends AppCompatActivity implements SelectableActivity<BankAccount>, AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_BANK_ACCOUNT_ID = "KEY_EXTRA_BANK_ACCOUNT_ID";
    public       static int    MODE                      = OPEN_TO_EDIT_MODE;
    private List<HashMap<String, String>> mapList        = new ArrayList<>();
    private static final String KEY_NUMBER               = "number";
    private static final String KEY_DIGIT                = "digit";
    private static final String KEY_STATUS               = "status";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_list);

        MODE = getIntent().getIntExtra(KEY_MODE, OPEN_TO_EDIT_MODE);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        ImageButton addButton = (ImageButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BankAccountListActivity.this, BankAccountCreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_BANK_ACCOUNT_ID, 0);
                startActivity(intent);
            }
        });

        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
        });

        ImageButton extractButton = (ImageButton) findViewById(R.id.extractButton);
        extractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BankAccountListActivity.this, BankAccountCreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_BANK_ACCOUNT_ID, 0);
                startActivity(intent);
            }
        });

        new GetBankAccounts(new GetBankAccounts.OnLoadListener() {
            @Override
            public void onLoaded(List<BankAccount> bankAccounts) {
                for (BankAccount bankAccount : bankAccounts) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KEY_ID, String.valueOf(bankAccount.getId()));
                    map.put(KEY_DESCRIPTION, bankAccount.getDescricao());
                    map.put(KEY_NUMBER, String.valueOf(bankAccount.getNumero()));
                    map.put(KEY_DIGIT, String.valueOf(bankAccount.getDigito()));
                    map.put(KEY_STATUS, "("+bankAccount.getSituacao()+")");

                    mapList.add(map);
                }

                ListAdapter adapter = new SimpleAdapter(BankAccountListActivity.this, mapList, R.layout.bank_account_list_renderer,
                        new String[] { KEY_DESCRIPTION, KEY_NUMBER, KEY_DIGIT, KEY_STATUS },
                        new int[] { R.id.description, R.id.number, R.id.digit, R.id.status});

                listView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not get list of objects.", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(3);

    }

    private void confirmDelete(){
        new DeleteCreditCard(new DeleteCreditCard.OnLoadListener() {
            @Override
            public void onLoaded(String response) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BankAccountListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Could not Delete object", Toast.LENGTH_SHORT).show();
            }
        }, this).execute(((Finality)listView.getSelectedItem()).getId());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        listView.setSelection(i);
        listView.setSelected(true);

        /*if ( MODE == OPEN_TO_SELECT_MODE ){
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, Integer.parseInt(mapList.get(i).get(KEY_ID)));
            intent.putExtra(KEY_DESCRIPTION, mapList.get(i).get(KEY_DESCRIPTION));
            setResult(RESULT_OK, intent);
            finish();
        }else {
            int id = Integer.parseInt(mapList.get(i).get(KEY_ID));
            Intent intent = new Intent(getApplicationContext(), BankAccountCreateOrEditActivity.class);
            intent.putExtra(KEY_EXTRA_BANK_ACCOUNT_ID, id);
            startActivity(intent);
        }*/

    }

}
