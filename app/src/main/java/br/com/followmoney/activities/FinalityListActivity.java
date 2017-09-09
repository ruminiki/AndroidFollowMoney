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

import br.com.followmoney.dao.remote.FinalityAsyncHttpTask;
import br.com.followmoney.domain.Finality;
import br.com.followmoney.followmoney.R;

public class FinalityListActivity extends AppCompatActivity implements FinalityAsyncHttpTask.Callback, AdapterView.OnItemClickListener{

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    private ListView listView;

    private List<HashMap<String, String>> mapList = new ArrayList<>();
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

        new FinalityAsyncHttpTask(this).execute();

        /*FinalidadeDBHelper dbHelper = new FinalidadeDBHelper(this);

        final List<Finality> list = dbHelper.getAll();

        String [] columns = new String[] {
                FinalidadeDBHelper.COLUMN_ID,
                FinalidadeDBHelper.COLUMN_DESCRIPTION
        };

       int [] widgets = new int[] {
                R.id.finalidadeID,
                R.id.finalidadeDescricao
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.finality_info, cursor, columns, widgets, 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView listView, View view, int position, long id) {
                Cursor itemCursor = (Cursor) FinalityListActivity.this.listView.getItemAtPosition(position);
                int finalidadeID = itemCursor.getInt(itemCursor.getColumnIndex(FinalidadeDBHelper.COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), FinalityCreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, finalidadeID);
                startActivity(intent);
            }
        });*/

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, mapList.get(i).get(KEY_DESCRIPTION),Toast.LENGTH_LONG).show();
    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(FinalityListActivity.this, mapList, R.layout.finality_info,
                new String[] { KEY_DESCRIPTION },
                new int[] { R.id.description});

        listView.setAdapter(adapter);

    }

    @Override
    public void onLoaded(List<Finality> finalityList) {
        for (Finality finality : finalityList) {
            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_DESCRIPTION, finality.getDescription());

            mapList.add(map);
        }

        loadListView();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

}
