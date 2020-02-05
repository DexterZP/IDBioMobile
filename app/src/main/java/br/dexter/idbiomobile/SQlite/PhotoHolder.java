package br.dexter.idbiomobile.SQlite;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import br.dexter.idbiomobile.R;

public class PhotoHolder extends RecyclerView.ViewHolder
{
    public TextView local, localidade, data;
    public CardView linearLayout;

    PhotoHolder(View view)
    {
        super(view);

        local = view.findViewById(R.id.SetLocal);
        localidade = view.findViewById(R.id.SetLocalidade);
        data = view.findViewById(R.id.SetData);
        linearLayout = view.findViewById(R.id.SelectItem);
    }
}
