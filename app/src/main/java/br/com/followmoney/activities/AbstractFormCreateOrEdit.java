package br.com.followmoney.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.GetEntityJson;
import br.com.followmoney.dao.remote.PostEntityJson;
import br.com.followmoney.dao.remote.PutEntityJson;

/**
 * Created by ruminiki.schmoeller on 18/09/17.
 */

public abstract class AbstractFormCreateOrEdit<T> extends AppCompatActivity {

    protected ImageButton saveButton;
    protected int entityID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveButton = (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persist();
            }
        });
        entityID = getIntent().getIntExtra(AbstractFormList.KEY_EXTRA_ID, 0);

    }

    private void loadObjectToEdit(){
        if (entityID > 0) {
            new GetEntityJson<T>(new GetEntityJson.OnLoadListener<T>() {
                @Override
                public void onLoaded(T entity) {
                    entityToEditLoaded(entity);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Error on get remote object. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(entityID, getRestContext());

        }
    }

    public void persist() {
        if(entityID > 0) {
            new PutEntityJson<T>(new PutEntityJson.OnLoadListener<T>() {
                @Override
                public void onLoaded(T t) {
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    returnToListActivity();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(getValueDataFieldsInView(entityID), entityID, getRestContext());

        }
        else {
            new PostEntityJson<T>(new PostEntityJson.OnLoadListener<T>() {
                @Override
                public void onLoaded(T t) {
                    Toast.makeText(getApplicationContext(), "Object Inserted", Toast.LENGTH_SHORT).show();
                    returnToListActivity();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Could not Insert object", Toast.LENGTH_SHORT).show();
                }
            }, this).execute(getValueDataFieldsInView(entityID), getRestContext());
        }
    }

    /**
     * After save or update, return to form list.
     */
    private void returnToListActivity(){
        Intent intent = new Intent(getApplicationContext(), getActivityClassList());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Call when entity Json request load entity to edit.
     * @param entity
     */
    protected abstract void entityToEditLoaded(T entity);

    /**
     * Call to get values from interface in a object T instance.
     * @param id
     * @return
     */
    protected abstract T getValueDataFieldsInView(Integer id);

    /**
     * Get the string of context to form URL rest request.
     * @return
     */
    protected abstract String getRestContext();

    /**
     * Return the activity class that list T entities.
     * @return
     */
    protected abstract Class getActivityClassList();

}
