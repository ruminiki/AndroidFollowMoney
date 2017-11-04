package br.com.followmoney.activities.movements;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormList;
import br.com.followmoney.components.adapters.MovementListAdapter;
import br.com.followmoney.components.MyGestureListener;
import br.com.followmoney.dao.remote.StringValueRequest;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.fragments.SearchMovementFragment;
import br.com.followmoney.globals.GlobalParams;

public class MovementListActivity extends AbstractFormList<Movement> implements MyGestureListener.OnGestureListener {

    YearMonthPickerDialog yearMonthPickerDialog;
    TextView mesReferenciaTextView, receitasTextView, despesasTextView, saldoMesTextView, saldoAnteriorTextView, saldoPrevistoTextView;

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private GestureDetector gestureDetector;
    private LinearLayout resumeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_movement_list);

        //BIND COMPONENTS VIEW
        mesReferenciaTextView = (TextView) findViewById(R.id.mesReferenciaTextView);
        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
        mesReferenciaTextView.setPaintFlags(mesReferenciaTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mesReferenciaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearMonthPickerDialog.show();
            }
        });

        receitasTextView = (TextView) findViewById(R.id.receitasTextView);
        despesasTextView = (TextView) findViewById(R.id.despesasTextView);
        saldoMesTextView = (TextView) findViewById(R.id.saldoMesTextView);
        resumeLayout     = (LinearLayout) findViewById(R.id.resumeLayout);
        //saldoPrevistoTextView = (TextView) findViewById(R.id.saldoPrevistoTextView);
        //saldoAnteriorTextView = (TextView) findViewById(R.id.saldoAnteriorTextView);

        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(Gravity.END)) {
                    drawer.closeDrawer(Gravity.END);
                }else{
                    drawer.openDrawer(Gravity.END);
                }*/

                FragmentManager fm = getSupportFragmentManager();
                SearchMovementFragment fragment = new SearchMovementFragment();
                fragment.setActivity(MovementListActivity.this);
                fragment.show(fm, "Search Movement");

            }
        });

        //SELECT MONTH
        yearMonthPickerDialog = new YearMonthPickerDialog(MovementListActivity.this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
                GlobalParams.getInstance().setSelectedMonthReference(dateFormat.format(calendar.getTime()));
                mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
                loadList();
            }
        }, R.style.MonthPickerTheme);

        View balanceView = findViewById(R.id.balanceLinearLayout);
        gestureDetector = new GestureDetector(this, new MyGestureListener(MovementListActivity.this));
        balanceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityListLoaded(final List<Movement> movements) {
        //GET BALANCE
        String context = "/movements/previousBalance" +
                "/user/"+GlobalParams.getInstance().getUserOnLineID() +
                "/period/"+GlobalParams.getInstance().getSelectedMonthReference();

        new StringValueRequest(new StringValueRequest.OnLoadListener() {
            @Override
            public void onLoaded(String result) {
                updateListAndResume(movements, result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, this).execute(context);

    }

    private void updateListAndResume(List<Movement> movements, String result) {
        double receitas = 0, despesas = 0, saldoPrevisto = 0, saldoAnterior = 0;

        for ( Movement movement : movements ){
            if ( movement.getOperacao().equals(Movement.CREDIT) ){
                receitas += movement.getValor();
            }else{
                despesas += movement.getValor();
            }
        }

        despesasTextView.setText(numberFormat.format(despesas));
        receitasTextView.setText(numberFormat.format(receitas));
        saldoMesTextView.setText(numberFormat.format(receitas-despesas));
        try{
            saldoAnterior = Double.parseDouble(result);
            saldoPrevisto = saldoAnterior + receitas - despesas;
        }catch (Exception e ){
            saldoAnterior = 0;
            saldoPrevisto = saldoAnterior + receitas - despesas;
        }
        //saldoAnteriorTextView.setText(numberFormat.format(saldoAnterior));
        //saldoPrevistoTextView.setText(numberFormat.format(saldoPrevisto));
        listView.setAdapter(new MovementListAdapter(MovementListActivity.this, R.layout.movement_list_renderer, movements));
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
        if ( selectedEntityID > 0 ){
            if ( !selectedEntity.canEdit() ){
                Toast.makeText(getApplicationContext(), selectedEntity.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
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
    }

    @Override
    public void leftToRightGesture() {
        GlobalParams.getInstance().setPreviousMonthReference();
        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
        loadList();
    }

    @Override
    public void rightToLeftGesture() {
        GlobalParams.getInstance().setNextMonthReference();
        mesReferenciaTextView.setText(GlobalParams.getInstance().getSelectedMonthReferenceFormated());
        loadList();
    }

    public void fillEntityListLoaded(List<Movement> entities) {
        entityListLoaded(entities);
    }
}
