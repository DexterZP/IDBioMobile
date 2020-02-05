package br.dexter.idbiomobile.SQlite;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.dexter.idbiomobile.R;

public class UsuarioHolder extends RecyclerView.ViewHolder
{
    public TextView local, localidade, latitude, longitude, data, resultado, SetImagens;
    public Button SendExport;
    public LinearLayout ContainerImagens;
    public ImageView SetImage1, SetImage2, SetImage3, SetImage4;

    public UsuarioHolder(View view)
    {
        super(view);

        local = view.findViewById(R.id.SetLocal);
        localidade = view.findViewById(R.id.SetLocalidade);
        latitude = view.findViewById(R.id.SetLatitude);
        longitude = view.findViewById(R.id.SetLongitude);
        data = view.findViewById(R.id.SetData);
        resultado = view.findViewById(R.id.SetResultado);

        ContainerImagens = view.findViewById(R.id.ContainerImagens);
        SetImage1 = view.findViewById(R.id.SetImage1);
        SetImage2 = view.findViewById(R.id.SetImage2);
        SetImage3 = view.findViewById(R.id.SetImage3);
        SetImage4 = view.findViewById(R.id.SetImage4);
        SetImagens = view.findViewById(R.id.SetImagens);

        SendExport = view.findViewById(R.id.SetExportar);
    }
}
