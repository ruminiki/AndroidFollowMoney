package br.com.followmoney.activities.movements;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

public class MovementListActivity extends AbstractFormList<Movement> {

    private static final String KEY_FINALITY      = "finality";
    private static final String KEY_BANK_ACCOUNT  = "bankAccount";
    private static final String KEY_EMISSION      = "emission";
    private static final String KEY_MATURITY      = "maturity";
    private static final String KEY_VALUE         = "value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_movement_list);

        FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fabSearch);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }else{
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

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

            map.put(KEY_EMISSION, "Emiss: " + DateUtil.format(movement.getEmissao(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_MATURITY, "Vencto: " + DateUtil.format(movement.getVencimento(), "yyyyMMdd", "dd/MM/yyyy"));
            map.put(KEY_VALUE, "R$ " + String.valueOf(movement.getValor()));

            mapList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(MovementListActivity.this, mapList, R.layout.movement_list_renderer,
                new String[] { KEY_DESCRIPTION, KEY_FINALITY, KEY_VALUE, KEY_BANK_ACCOUNT, KEY_EMISSION, KEY_MATURITY },
                new int[] { R.id.description, R.id.finality, R.id.value, R.id.bank_account, R.id.emission, R.id.maturity});

        listView.setAdapter(adapter);
    }

    @Override
    protected String getRestContext() {
        return "/movements/user/3/period/201708";//@TODO precisa pegar o mês selecionado na search panel
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) {
        Intent intent = new Intent(getApplicationContext(), MovementCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_ID, selectedEntityID);
        startActivity(intent);
    }

    @Override
    protected Type getType() {
        return new TypeToken<List<Movement>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int id = Integer.parseInt(mapList.get(i).get(KEY_ID));
        Intent intent = new Intent(getApplicationContext(), MovementCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_ID, id);
        startActivity(intent);
    }

}
