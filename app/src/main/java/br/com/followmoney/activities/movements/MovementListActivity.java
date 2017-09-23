package br.com.followmoney.activities.movements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.activities.CustomListAdapter;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.globals.GlobalParams;

public class MovementListActivity extends AbstractFormList<Movement> {

    YearMonthPickerDialog yearMonthPickerDialog;
    TextView mesReferenciaTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_movement_list);

        mesReferenciaTextView = (TextView) findViewById(R.id.mesReferenciaTextView);

        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
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

        ImageButton selectMonthButton = (ImageButton) findViewById(R.id.selectMonthButton);
        selectMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearMonthPickerDialog.show();
            }
        });

        yearMonthPickerDialog = new YearMonthPickerDialog(MovementListActivity.this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
                GlobalParams.getInstance().setSelectedMonthReference(dateFormat.format(calendar.getTime()));
                loadList();
            }
        }, R.style.MonthPickerTheme);

        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReference());

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(List<Movement> movements) {
        listView.setAdapter(new CustomListAdapter<Movement>(this, R.layout.movement_list_renderer, movements));
    }

    @Override
    protected String getRestContextList() {
        return "/movements/user/"+GlobalParams.getInstance().getUserOnLineID()+"/period/"+GlobalParams.getInstance().getSelectedMonthReference();
    }

    @Override
    protected String getRestContextDelete() {
        return "/movements/"+selectedEntityID;
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
        selectedEntity = (Movement) listView.getItemAtPosition(i);
        selectedEntityID = selectedEntity != null ? selectedEntity.getId() : 0;
        /*Intent intent = new Intent(getApplicationContext(), MovementCreateOrEditActivity.class);
        intent.putExtra(KEY_EXTRA_ID, selectedEntityID);
        startActivity(intent);*/
    }

}
