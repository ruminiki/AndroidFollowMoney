package br.com.followmoney.activities.paymentForms;

import android.content.Intent;
import android.os.Bundle;
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
import br.com.followmoney.dao.remote.paymentForms.GetPaymentForms;
import br.com.followmoney.domain.PaymentForm;

public class PaymentFormListActivity extends AppCompatActivity implements SelectableActivity<PaymentForm>, AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_PAYMENT_FORM_ID = "KEY_EXTRA_PAYMENT_FORM_ID";
    public       static int    MODE                 = OPEN_TO_EDIT_MODE;

    private ListView listView;

    private List<HashMap<String, String>> mapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form_list);

        MODE = getIntent().getIntExtra(KEY_MODE, OPEN_TO_EDIT_MODE);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        ImageButton button = (ImageButton) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentFormListActivity.this, PaymentFormCreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_PAYMENT_FORM_ID, 0);
                startActivity(intent);
            }
        });

        new GetPaymentForms(new GetPaymentForms.OnLoadListener() {
            @Override
            public void onLoaded(List<PaymentForm> paymentForms) {
                for (PaymentForm paymentForm : paymentForms) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KEY_ID, String.valueOf(paymentForm.getId()));
                    map.put(KEY_DESCRIPTION, paymentForm.getDescricao());

                    mapList.add(map);
                }

                ListAdapter adapter = new SimpleAdapter(PaymentFormListActivity.this, mapList, R.layout.payment_form_list_renderer,
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
        if ( MODE == OPEN_TO_SELECT_MODE ){
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, Integer.parseInt(mapList.get(i).get(KEY_ID)));
            intent.putExtra(KEY_DESCRIPTION, mapList.get(i).get(KEY_DESCRIPTION));
            setResult(RESULT_OK, intent);
            finish();
        }else {
            int id = Integer.parseInt(mapList.get(i).get(KEY_ID));
            Intent intent = new Intent(getApplicationContext(), PaymentFormCreateOrEditActivity.class);
            intent.putExtra(KEY_EXTRA_PAYMENT_FORM_ID, id);
            startActivity(intent);
        }
    }

}
