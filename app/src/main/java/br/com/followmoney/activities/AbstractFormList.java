package br.com.followmoney.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.dao.remote.DeleteEntityJson;
import br.com.followmoney.dao.remote.GetEntitiesJson;

/**
 * Created by ruminiki.schmoeller on 18/09/17.
 */

public abstract class AbstractFormList<T> extends AppCompatActivity implements SelectableActivity<T>, AdapterView.OnItemClickListener{

    public final  static String KEY_EXTRA_ID    = "KEY_EXTRA_ID";
    public static final  String KEY_ID          = "id";
    public static final  String KEY_DESCRIPTION = "description";

    public static int MODE = OPEN_TO_EDIT_MODE;
    protected ListView listView;
    protected List<HashMap<String, String>> mapList = new ArrayList<>();
    protected int selectedEntityID, selectedEntityPosition;
    protected T selectedEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MODE = getIntent().getIntExtra(KEY_MODE, OPEN_TO_EDIT_MODE);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        ImageButton addButton = (ImageButton) findViewById(R.id.addButton);
        if ( addButton != null ) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCreateOrEditForm(0);
                }
            });
        }

        ImageButton editButton = (ImageButton) findViewById(R.id.editButton);
        if ( editButton != null ) {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedEntityID <= 0) {
                        Toast.makeText(getApplicationContext(), "Please, you need select an object to edit!", Toast.LENGTH_SHORT).show();
                    } else {
                        showCreateOrEditForm(selectedEntityID);
                    }
                }
            });
        }

        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        if ( deleteButton != null ) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedEntityID <= 0) {
                        Toast.makeText(getApplicationContext(), "Please, you need select an object to delete!", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
                        builder.setMessage(R.string.delete)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        confirmDelete();
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        AlertDialog d = builder.create();
                        d.setTitle("Delete Object?");
                        d.show();
                        return;
                    }
                }
            });
        }

       this.loadList();

    }

    protected void loadList(){

        new GetEntitiesJson<T>(new GetEntitiesJson.OnLoadListener<T>() {
            @Override
            public void onLoaded(List<T> entities) {
                entityListLoaded(entities);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, this).execute(getType(), getRestContextList());

    }

    private void confirmDelete(){
        new DeleteEntityJson(new DeleteEntityJson.OnResponseListener() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                for ( int i = 0; i < listView.getAdapter().getCount(); i++ ){
                    try {
                        Object entity = listView.getItemAtPosition(i);
                        Integer id = (Integer) entity.getClass().getMethod("getId").invoke(entity);
                        if ( selectedEntityID == id ) {
                            ((ArrayAdapter)listView.getAdapter()).remove(entity);
                            ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        }, this).execute(getRestContextDelete());

    }

    protected void updateObjectInListView(T updated){
        for ( int i = 0; i < listView.getAdapter().getCount(); i++ ){
            try {
                Object entity = listView.getItemAtPosition(i);
                Integer id = (Integer) entity.getClass().getMethod("getId").invoke(entity);
                if ( selectedEntityID == id ) {
                    ((ArrayAdapter)listView.getAdapter()).remove(entity);
                    ((ArrayAdapter)listView.getAdapter()).insert(updated, i);
                    ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //=========ABSTRACT METHODS==========

    /**
     * Call when entity Json request load entity to edit.
     * @param entities
     */
    protected abstract void entityListLoaded(List<T> entities);

    /**
     * Get the string of context to form URL rest request.
     * @return
     */
    protected abstract String getRestContextList();
    protected abstract String getRestContextDelete();
    protected abstract void   showCreateOrEditForm(int selectedEntityID);
    protected abstract Type   getType();

}
