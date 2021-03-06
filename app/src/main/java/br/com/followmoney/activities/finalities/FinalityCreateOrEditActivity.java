package br.com.followmoney.activities.finalities;

import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormCreateOrEdit;
import br.com.followmoney.domain.Finality;

public class FinalityCreateOrEditActivity extends AbstractFormCreateOrEdit<Finality> {

    private EditText descricaoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finality_create_or_edit);
        descricaoEditText = (EditText) findViewById(R.id.editTextDescricao);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void entityToEditLoaded(Finality finality) {
        if ( finality != null ){
            descricaoEditText.setText(finality.getDescricao());
            descricaoEditText.setEnabled(true);
            descricaoEditText.setFocusableInTouchMode(true);
            descricaoEditText.setClickable(true);
        }
    }

    @Override
    protected Finality getValueDataFieldsInView(Integer id){
        Finality f = new Finality();
        f.setId(id);
        f.setDescricao(descricaoEditText.getText().toString());
        f.setUsuario(3);
        return f;
    }

    @Override
    protected String getRestContextGetOrPut() {
        return "/finalities/"+entityID;
    }

    @Override
    protected String getRestContextPost() {
        return "/finalities";
    }

    @Override
    protected Type getType() {
        return new TypeToken<Finality>(){}.getType();
    }

    @Override
    protected Class getActivityClassList() {
        return FinalityListActivity.class;
    }

}
