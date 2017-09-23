package br.com.followmoney.activities.bankAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.domain.BankAccount;

import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION;
import static br.com.followmoney.activities.KeyParams.KEY_EXTRA_BANK_ACCOUNT_ID;

public class BankAccountListActivity extends AbstractFormList<BankAccount>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_list);

        ImageButton extractButton = (ImageButton) findViewById(R.id.extractButton);
        extractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( selectedEntityPosition >= 0 ){
                    Intent intent = new Intent(getApplicationContext(), BankAccountExtractMovementListActivity.class);
                    intent.putExtra(KEY_EXTRA_BANK_ACCOUNT_ID, selectedEntity.getId());
                    intent.putExtra(KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION, selectedEntity.getDescricao());
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
        listView.setAdapter(new CustomListAdapter<BankAccount>(this, R.layout.bank_account_list_renderer, bankAccounts));
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
        selectedEntity = (BankAccount) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity != null ? selectedEntity.getId() : 0;
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
