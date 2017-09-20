package br.com.followmoney.activities.bankAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.domain.BankAccount;

public class BankAccountListActivity extends AbstractFormList<BankAccount>{

    private static final String KEY_NUMBER = "number";
    private static final String KEY_DIGIT  = "digit";
    private static final String KEY_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_list);

        ImageButton extractButton = (ImageButton) findViewById(R.id.extractButton);
        extractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( selectedEntityPosition >= 0 ){
                    Intent intent = new Intent(getApplicationContext(), BankAccountExtractMovementListActivity.class);
                    intent.putExtra(BankAccountExtractMovementListActivity.KEY_EXTRA_BANK_ACCOUNT_ID, selectedEntityID);
                    intent.putExtra(BankAccountExtractMovementListActivity.KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION, mapList.get(selectedEntityPosition).get(KEY_DESCRIPTION));
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Please, you need select an object to show extract!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(List<BankAccount> bankAccounts) {
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
    protected String getRestContextList() {
        return "/bankAccounts/user/3"; //@TODO get user logged in
    }

    @Override
    protected String getRestContextDelete() {
        return "/bankAccounts/"+selectedEntityID;
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) {
        Intent intent = new Intent(BankAccountListActivity.this, BankAccountCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_ID, selectedEntityID);
        startActivity(intent);
    }

    @Override
    protected Type getType() {
        return new TypeToken<List<BankAccount>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntityID = Integer.parseInt(mapList.get(i).get(KEY_ID));
        selectedEntityPosition = i;
        if ( MODE == OPEN_TO_SELECT_MODE ){
            BankAccount b = (BankAccount) listView.getSelectedItem();
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, b.getId());
            intent.putExtra(KEY_DESCRIPTION, b.getDescricao());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
