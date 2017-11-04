package br.com.followmoney.components.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.com.followmoney.domain.BankAccount;
import br.com.followmoney.domain.Movement;

public class BankAccountListAdapter extends ArrayAdapter<BankAccount> {

    int layout;

    public BankAccountListAdapter(Context context, int layout, List<BankAccount> entities) {
        super(context, layout, entities);
        this.layout = layout;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        BankAccount entity = getItem(position);

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layout, parent, false);
        }

        ArrayList<View> children = getAllChildren(view);
        for (View child : children) {
            if (child instanceof TextView) {
                TextView childTextView = (TextView) child;
                try{
                    String fieldName = view.getResources().getResourceEntryName(childTextView.getId());
                    String getName = "get" + fieldName.replaceFirst(fieldName.substring(0,1), fieldName.substring(0,1).toUpperCase());

                    Method method = entity.getClass().getMethod(getName);
                    Object response = method.invoke(entity);
                    childTextView.setText(response != null ? String.valueOf(method.invoke(entity)) : ""); // pass arg

                    if ( fieldName.equals("situacao") ){
                        if ( String.valueOf(method.invoke(entity)).equals(BankAccount.ACTIVE) ){
                            childTextView.setTextColor(Color.rgb(133, 224, 133));
                        }else{
                            childTextView.setTextColor(Color.rgb(255, 133, 102));
                        }
                    }

                    if ( fieldName.equals("operacaoResumida") ){
                        if ( String.valueOf(method.invoke(entity)).equals(Movement.CREDIT_INITIALS) ){
                            childTextView.setTextColor(Color.rgb(47, 182, 47));
                        }else{
                            childTextView.setTextColor(Color.rgb(255, 92, 51));
                        }
                    }

                }catch (Exception e){  }
            }
        }
        return view;
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}