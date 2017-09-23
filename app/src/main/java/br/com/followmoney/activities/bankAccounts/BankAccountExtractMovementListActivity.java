package br.com.followmoney.activities.bankAccounts;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.util.DateUtil;

public class BankAccountExtractMovementListActivity extends AbstractFormList<Movement>{

    public static final String KEY_EXTRA_BANK_ACCOUNT_ID          = "KEY_EXTRA_BANK_ACCOUNT_ID";
    public static final String KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION = "KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION";

    private static final String KEY_FINALITY     = "finality";
    private static final String KEY_BANK_ACCOUNT = "bankAccount";
    private static final String KEY_EMISSION     = "emission";
    private static final String KEY_MATURITY     = "maturity";
    private static final String KEY_VALUE        = "value";

    int bankAccountID = 0;
    String bankAccountDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_extract_movement_list);

        bankAccountID          = getIntent().getIntExtra(KEY_EXTRA_BANK_ACCOUNT_ID, 0);
        bankAccountDescription = getIntent().getStringExtra(KEY_EXTRA_BANK_ACCOUNT_DESCRIPTION);

        TextView bankAccountDescriptionTextView = (TextView) findViewById(R.id.bankAccountDescriptionTextView);
        bankAccountDescriptionTextView.setText(bankAccountDescription);

        TextView atualBalanceTextView = (TextView) findViewById(R.id.atualBalanceTextView);
        atualBalanceTextView.setText("Saldo Atual: R$ 0,00");//@TODO buscar o saldo da conta bancaria no servidor

        TextView foreseenBalanceTextView = (TextView) findViewById(R.id.foreseenBalanceTextView);
        foreseenBalanceTextView.setText("Previsto: R$ 0,00");//@TODO buscar o saldo da conta bancaria no servidor

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(List<Movement> movements) {
        for (Movement movement : movements) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(movement.getId()));
            map.put(KEY_DESCRIPTION, movement.getDescricao());
            map.put(KEY_FINALITY, movement.getFinalidade().getDescricao());
            map.put(KEY_EMISSION, "E: " + DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_MATURITY, "V: " + DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_VALUE, "R$ " + String.valueOf(movement.getValor()));
            mapList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(getApplicationContext(), mapList, R.layout.extract_movement_list_renderer,
                new String[] { KEY_DESCRIPTION, KEY_FINALITY, KEY_VALUE, KEY_BANK_ACCOUNT, KEY_EMISSION, KEY_MATURITY },
                new int[] { R.id.description, R.id.finality, R.id.value, R.id.bank_account, R.id.emission, R.id.maturity});

        listView.setAdapter(adapter);
    }

    @Override
    protected String getRestContextList() {
        return "/movements/extract/"+bankAccountID+"/period/201708";
    }

    @Override
    protected String getRestContextDelete() {
        return null;
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) { }

    @Override
    protected Type getType() {
        return new TypeToken<List<Movement>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       /* selectedEntityID = Integer.parseInt(mapList.get(i).get(KEY_ID));
        if ( MODE == OPEN_TO_SELECT_MODE ){
            BankAccount b = (BankAccount) listView.getSelectedItem();
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, b.getId());
            intent.putExtra(KEY_DESCRIPTION, b.getDescricao());
            setResult(RESULT_OK, intent);
            finish();
        }*/
    }

}
