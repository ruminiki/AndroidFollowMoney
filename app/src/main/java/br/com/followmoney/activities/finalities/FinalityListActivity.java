package br.com.followmoney.activities.finalities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.domain.Finality;

public class FinalityListActivity extends AbstractFormList<Finality> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finality_list);

        ImageButton extractButton = (ImageButton) findViewById(R.id.extractButton);
        extractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //@TODO implement detaisl finality movements
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(List<Finality> entities) {
        for (Finality finality : entities) {
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
    protected String getRestContext() {
        return "finalities";
    }

    @Override
    protected Type getType() {
        return new TypeToken<List<Finality>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntityID = Integer.parseInt(mapList.get(i).get(KEY_ID));
        if ( MODE == OPEN_TO_SELECT_MODE ){
            Finality f = (Finality) listView.getSelectedItem();
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, f.getId());
            intent.putExtra(KEY_DESCRIPTION, f.getDescricao());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void showCreateOrEditForm(int selectedEntityID) {
        Intent intent = new Intent(FinalityListActivity.this, FinalityCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_ID, selectedEntityID);
        startActivity(intent);
    }

}
