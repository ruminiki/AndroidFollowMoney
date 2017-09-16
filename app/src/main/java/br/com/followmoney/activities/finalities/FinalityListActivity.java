package br.com.followmoney.activities.finalities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.SelectableActivity;
import br.com.followmoney.dao.remote.finalities.GetFinalities;
import br.com.followmoney.domain.Finality;

public class FinalityListActivity extends AppCompatActivity implements SelectableActivity<Finality>, AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";
    public       static int    MODE                 = OPEN_TO_EDIT_MODE;

    private ListView listView;
    private List<HashMap<String, String>> mapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finality_list);

        MODE = getIntent().getIntExtra(KEY_MODE, OPEN_TO_EDIT_MODE);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
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

                ListAdapter adapter = new SimpleAdapter(FinalityListActivity.this, mapList, R.layout.finality_list_renderer,
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
        }else{
            int id = Integer.parseInt(mapList.get(i).get(KEY_ID));
            Intent intent = new Intent(getApplicationContext(), FinalityCreateOrEditActivity.class);
            intent.putExtra(KEY_EXTRA_CONTACT_ID, id);
            startActivity(intent);
        }

    }

}
