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

import br.com.followmoney.R;
import br.com.followmoney.domain.Movement;

public class MovementListAdapter extends ArrayAdapter<Movement> {

    private int layout;
    private Context context;

    public class ViewHolder {

        final TextView descricao;
        final TextView finalidade;
        final TextView fontePagadora;
        final TextView emissaoFormatado;
        final TextView vencimentoFormatado;
        final TextView valorFormatado;
        final TextView operacaoResumida;
        final TextView status;

        public ViewHolder(View view) {
            descricao = (TextView) view.findViewById(R.id.descricao);
            finalidade = (TextView) view.findViewById(R.id.finalidade);
            fontePagadora = (TextView) view.findViewById(R.id.fontePagadora);
            emissaoFormatado = (TextView) view.findViewById(R.id.emissaoFormatado);
            vencimentoFormatado = (TextView) view.findViewById(R.id.vencimentoFormatado);
            valorFormatado = (TextView) view.findViewById(R.id.valorFormatado);
            operacaoResumida = (TextView) view.findViewById(R.id.operacaoResumida);
            status = (TextView) view.findViewById(R.id.status);
        }
    }

    public MovementListAdapter(Context context, int layout, List<Movement> entities) {
        super(context, layout, entities);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;

        if( convertView == null) {
            view = LayoutInflater.from(context).inflate(layout, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        // Get the data item for this position
        Movement entity = getItem(position);

        if ( holder.descricao != null )
            holder.descricao.setText(entity.getDescricao());
        if ( holder.finalidade != null )
            holder.finalidade.setText(entity.getFinality().getDescricao());
        if ( holder.fontePagadora != null )
            holder.fontePagadora.setText(entity.getFontePagadora());
        if ( holder.emissaoFormatado != null )
            holder.emissaoFormatado.setText(entity.getEmissaoFormatado());
        if ( holder.vencimentoFormatado != null )
            holder.vencimentoFormatado.setText(entity.getVencimentoFormatado());
        if ( holder.valorFormatado != null )
            holder.valorFormatado.setText(entity.getValorFormatado());
        if ( holder.operacaoResumida != null ) {
            holder.operacaoResumida.setText(entity.getOperacaoResumida());
            if ( entity.getOperacao().equals(Movement.CREDIT) ){
                holder.operacaoResumida.setTextColor(Color.rgb(47, 182, 47));
            }else{
                holder.operacaoResumida.setTextColor(Color.rgb(255, 92, 51));
            }
        }
        if ( holder.status != null ) {
            holder.status.setText(entity.getStatus());
            if ( entity.getStatus().equals(Movement.PAYD) ){
                holder.status.setTextColor(Color.rgb(133, 224, 133));
            }else{
                holder.status.setTextColor(Color.rgb(255, 133, 102));
            }
        }

        return view;

    }

}