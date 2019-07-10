package br.com.followmoney.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.followmoney.R;
import br.com.followmoney.activities.movements.MovementListActivity;
import br.com.followmoney.dao.remote.GetEntitiesJson;
import br.com.followmoney.domain.Movement;
import br.com.followmoney.globals.GlobalParams;

public class SearchMovementFragment extends DialogFragment {

    EditText descricaoEditText;
    Button searchButton;

    MovementListActivity activity;

    private String text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_movement_search_panel, container, false);

        descricaoEditText = (EditText) view.findViewById(R.id.descricaoEditText);
        descricaoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( start > 0 && start % 3 == 0 ) {
                    text = descricaoEditText.getText().toString();
                    load();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}

        });

        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = descricaoEditText.getText().toString();
                load();
                dismiss();
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        if ( getDialog() == null ){
            return;
        }

        getDialog().setTitle("Search Movement");

        descricaoEditText.setText(text);

       /* Window window = getDialog().getWindow();
        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 400;
        params.height = 400;
        window.setAttributes(params);*/

    }

    public void setText(String text){
        this.text = text;
    }

    public void setActivity(MovementListActivity activity){
        this.activity = activity;
    }

    public void load(){
        new GetEntitiesJson<Movement>(new GetEntitiesJson.OnLoadListener<Movement>() {
            @Override
            public void onLoaded(List<Movement> entities) {
                activity.fillEntityListLoaded(entities);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }, getContext()).execute(new TypeToken<List<Movement>>(){}.getType(), getSearchRestContext());
    }

    private String getSearchRestContext(){
        return "/movements/user/"+GlobalParams.getInstance().getUserOnLineID()+
                "/period/"+GlobalParams.getInstance().getSelectedMonthReference()+
                "/fill/"+text;
    }

}
