package br.com.followmoney.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter<T> extends ArrayAdapter<T> {

    int layout;

    // View lookup cache
    private static class ViewHolder {
        TextView description, number, digit, status;
    }

    public CustomListAdapter(Context context, int layout, List<T> entities) {
        super(context, layout, entities);
        this.layout = layout;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        T entity = getItem(position);

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layout, parent, false);

            ArrayList<View> children = getAllChildren(view);
            for (View child : children) {
                if (child instanceof TextView) {
                    TextView childTextView = (TextView) child;
                    try{
                        String fieldName = view.getResources().getResourceEntryName(childTextView.getId());
                        String getName = "get" + fieldName.replaceFirst(fieldName.substring(0,1), fieldName.substring(0,1).toUpperCase());

                        Method method = entity.getClass().getMethod(getName);
                        childTextView.setText(String.valueOf(method.invoke(entity))); // pass arg

                    }catch (Exception e){  }
                }
            }
        }

        /*// Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (view == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.bank_account_list_renderer, parent, false);
            viewHolder.description = (TextView) view.findViewById(R.id.description);
            viewHolder.number = (TextView) view.findViewById(R.id.number);
            viewHolder.digit = (TextView) view.findViewById(R.id.digit);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            // Cache the viewHolder object inside the fresh view
            view.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) view.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.description.setText(entity.getDescricao());
        viewHolder.number.setText(entity.getNumero());
        viewHolder.digit.setText(String.valueOf(entity.getDigito()));
        viewHolder.status.setText(entity.getSituacao());*/
        // Return the completed view to render on screen
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