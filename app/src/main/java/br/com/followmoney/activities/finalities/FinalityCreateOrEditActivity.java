package br.com.followmoney.activities.finalities;

import android.os.Bundle;
import android.widget.EditText;

import br.com.followmoney.R;
import br.com.followmoney.activities.AbstractFormCreateOrEdit;
import br.com.followmoney.domain.Finality;

public class FinalityCreateOrEditActivity extends AbstractFormCreateOrEdit<Finality> {

    private EditText descricaoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finality_create_or_edit);
        descricaoEditText = (EditText) findViewById(R.id.editTextDescricao);
    }

    @Override
    protected void entityToEditLoaded(Finality finality) {
        descricaoEditText.setText(finality.getDescricao());
        descricaoEditText.setEnabled(true);
        descricaoEditText.setFocusableInTouchMode(true);
        descricaoEditText.setClickable(true);
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
    protected String getRestContext() {
        return "finalities";
    }

    @Override
    protected Class getActivityClassList() {
        return FinalityListActivity.class;
    }

}
