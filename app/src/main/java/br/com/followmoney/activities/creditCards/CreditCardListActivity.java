package br.com.followmoney.activities.creditCards;

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
import br.com.followmoney.dao.remote.creditCards.GetCreditCards;
import br.com.followmoney.domain.CreditCard;

public class CreditCardListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_CREDIT_CARD_ID = "KEY_EXTRA_CREDIT_CARD_ID";

    private ListView listView;

    private List<HashMap<String, String>> mapList = new ArrayList<>();
    private static final String KEY_ID            = "id";
    private static final String KEY_DESCRIPTION   = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_list);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        Button button = (Button) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditCardListActivity.this, CreditCardCreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CREDIT_CARD_ID, 0);
                startActivity(intent);
            }
        });

        new GetCreditCards(new GetCreditCards.OnLoadListener() {
            @Override
            public void onLoaded(List<CreditCard> creditCards) {
                for (CreditCard creditCard : creditCards) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KEY_ID, String.valueOf(creditCard.getId()));
                    map.put(KEY_DESCRIPTION, creditCard.getDescricao());

                    mapList.add(map);
                }

                ListAdapter adapter = new SimpleAdapter(CreditCardListActivity.this, mapList, R.layout.credit_card_list_renderer,
                        new String[] { KEY_DESCRIPTION },
                        new int[] { R.id.description});

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
        Intent intent = new Intent(getApplicationContext(), CreditCardCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_CREDIT_CARD_ID, id);
        startActivity(intent);
    }

}
