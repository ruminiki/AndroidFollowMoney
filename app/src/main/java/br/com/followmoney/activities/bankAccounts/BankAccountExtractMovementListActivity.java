package br.com.followmoney.activities.bankAccounts;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.util.DateUtil;

public class BankAccountExtractMovementListActivity extends AbstractFormList<Movement>{

    private static final String KEY_FINALITY     = "finality";
    private static final String KEY_BANK_ACCOUNT = "bankAccount";
    private static final String KEY_EMISSION     = "emission";
    private static final String KEY_MATURITY     = "maturity";
    private static final String KEY_VALUE        = "value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_account_extract_movement_list);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(List<Movement> movements) {
        for (Movement movement : movements) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(movement.getId()));
            map.put(KEY_DESCRIPTION, movement.getDescricao());
            map.put(KEY_FINALITY, "Finalidade: " + movement.getFinalidade().getDescricao());

            if ( movement.getContaBancaria() != null && movement.getContaBancaria().getDescricao() != null ){
                map.put(KEY_BANK_ACCOUNT, "Conta Bancária: " + movement.getContaBancaria().getDescricao());
            }else{
                if ( movement.getCartaoCredito() != null && movement.getCartaoCredito().getDescricao() != null ){
                    map.put(KEY_BANK_ACCOUNT, "Cartão Crédito: " + movement.getCartaoCredito().getDescricao());
                }
            }

            map.put(KEY_EMISSION, "E: " + DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_MATURITY, "V: " + DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_VALUE, "R$ " + String.valueOf(movement.getValor()));

            mapList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(getApplicationContext(), mapList, R.layout.bank_account_extract_list_renderer,
                new String[] { KEY_DESCRIPTION, KEY_FINALITY, KEY_VALUE, KEY_BANK_ACCOUNT, KEY_EMISSION, KEY_MATURITY },
                new int[] { R.id.description, R.id.finality, R.id.value, R.id.bank_account, R.id.emission, R.id.maturity});

        listView.setAdapter(adapter);
    }

    @Override
    protected String getRestContext() {
        return "movements/extract";
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
