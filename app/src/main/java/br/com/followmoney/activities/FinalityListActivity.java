package br.com.followmoney.activities;

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

import br.com.followmoney.dao.remote.finalities.GetFinalities;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.followmoney.R;

public class FinalityListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    private ListView listView;

    private List<HashMap<String, String>> mapList = new ArrayList<>();
    private static final String KEY_ID            = "id";
    private static final String KEY_DESCRIPTION   = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finality_list);

        listView = (ListView) findViewById(R.id.listViewFinalidade);
        listView.setOnItemClickListener(this);

        Button button = (Button) findViewById(R.id.addNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalityListActivity.this, FinalityCreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(intent);
            }
        });

        new GetFinalities(new GetFinalities.OnLoadListener() {
            @Override
            public void onLoaded(List<Finality> finalities) {
                for (Finality finality : finalities) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KEY_ID, String.valueOf(finality.getId()));
                    map.put(KEY_DESCRIPTION, finality.getDescricao());

                    mapList.add(map);
                }

                ListAdapter adapter = new SimpleAdapter(FinalityListActivity.this, mapList, R.layout.finality_info,
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
        Intent intent = new Intent(getApplicationContext(), FinalityCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_CONTACT_ID, id);
        startActivity(intent);
    }

}
