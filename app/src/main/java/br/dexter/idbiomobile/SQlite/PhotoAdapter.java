package br.dexter.idbiomobile.SQlite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.dexter.idbiomobile.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>
{
    private final List<Data> dataList;
    private Context context;

    private CallbackInterface callBack;

    public interface CallbackInterface {
        void onHandleSelection(String local, String city, String state);
    }

    public PhotoAdapter(Context context, List<Data> list){
        this.dataList = list;
        this.context = context;

        try{
            callBack = (CallbackInterface) context;
        }catch (ClassCastException ex){
            Log.e("MyAdapter", "Must implement", ex);
        }
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PhotoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_sendphoto_card, viewGroup, false));
    }

    @SuppressLint("SetTextI18n") @Override
    public void onBindViewHolder(@NonNull final PhotoHolder holder, final int i)
    {
        holder.local.setText("Local: " + dataList.get(i).getLocal());
        holder.localidade.setText("Localidade: " + dataList.get(i).getLocalidade());
        holder.data.setText(dataList.get(i).getData());

        holder.linearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(isOnline())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Local: " + dataList.get(i).getLocal() + "\nLocalidade: " + dataList.get(i).getLocalidade());

                    builder.setPositiveButton("Enviar foto", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            String local = dataList.get(holder.getAdapterPosition()).getLocal();
                            String city = dataList.get(holder.getAdapterPosition()).getCity(), state = dataList.get(holder.getAdapterPosition()).getState();
                            callBack.onHandleSelection(local, city, state);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    Toast.makeText(context, "Conecte-se em uma internet para enviar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return false;
    }
}