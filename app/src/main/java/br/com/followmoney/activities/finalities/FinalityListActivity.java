package br.com.followmoney.activities.finalities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
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
    protected void entityListLoaded(List<Finality> finalities) {
        listView.setAdapter(new CustomListAdapter<Finality>(this, R.layout.finality_list_renderer, finalities));
    }

    @Override
    protected String getRestContextList() {
        return "/finalities/user/3"; //@TODO get user logged in
    }

    @Override
    protected String getRestContextDelete() {
        return "/finalities/"+selectedEntityID;
    }

    @Override
    protected Type getType() {
        return new TypeToken<List<Finality>>(){}.getType();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedEntity = (Finality) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity != null ? selectedEntity.getId() : 0;
        if ( MODE == OPEN_TO_SELECT_MODE ){
            Intent intent = new Intent();
            intent.putExtra(KEY_ID, selectedEntity.getId());
            intent.putExtra(KEY_DESCRIPTION, selectedEntity.getDescricao());
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
