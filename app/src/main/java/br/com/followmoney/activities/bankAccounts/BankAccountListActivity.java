package br.com.followmoney.activities.bankAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.bankAccounts.GetBankAccounts;
import br.com.followmoney.domain.BankAccount;

public class BankAccountListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_BANK_ACCOUNT_ID = "KEY_EXTRA_BANK_ACCOUNT_ID";

    private ListView listView;

    private List<HashMap<String, String>> mapList = new ArrayList<>();
    private static final String KEY_ID            = "id";
    private static final String KEY_DESCRIPTION   = "description";
    private static final String KEY_NUMBER        = "number";
    private static final String KEY_DIGIT         = "digit";
    private static final String KEY_STATUS        = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_list);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        Button button = (Button) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int id = Integer.parseInt(mapList.get(i).get(KEY_ID));
        Intent intent = new Intent(getApplicationContext(), BankAccountCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_BANK_ACCOUNT_ID, id);
        startActivity(intent);
    }

}
