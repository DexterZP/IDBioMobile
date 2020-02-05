package br.dexter.idbiomobile.SQlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.app.Dialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.dexter.idbiomobile.R;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioHolder>
{
    private final List<Data> dataList;
    private Context context;

    private DatabaseReference databaseReference;

    public UsuarioAdapter(Context context, List<Data> list) {
        this.dataList = list;
        this.context = context;
    }

    @NonNull @Override
    public UsuarioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UsuarioHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_visualizar, viewGroup, false));
    }

    @SuppressLint("SetTextI18n") @Override
    public void onBindViewHolder(@NonNull final UsuarioHolder holder, int i)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("IDB Mobile").child(user.getUid()).child(dataList.get(i).getState()).child(dataList.get(i).getCity()).child(dataList.get(i).getLocal());
        }

        holder.local.setText("Local: " + dataList.get(i).getLocal());
        holder.localidade.setText("Localidade: " + dataList.get(i).getLocalidade());
        holder.latitude.setText("Latitude: " + dataList.get(i).getLatitude());
        holder.longitude.setText("Longitude: " + dataList.get(i).getLongitude());
        holder.resultado.setText(dataList.get(i).getResultado());
        holder.data.setText(dataList.get(i).getData());

        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild("Imagem 1")) {

                    holder.ContainerImagens.setVisibility(View.VISIBLE);

                    final String url1 = dataSnapshot.child("Imagem 1").getValue(String.class);
                    final String url2 = dataSnapshot.child("Imagem 2").getValue(String.class);
                    final String url3 = dataSnapshot.child("Imagem 3").getValue(String.class);
                    final String url4 = dataSnapshot.child("Imagem 4").getValue(String.class);

                    Picasso.get().load(url1).networkPolicy(NetworkPolicy.OFFLINE).resize(200,200).into(holder.SetImage1, new Callback()
                    {
                        @Override
                        public void onSuccess() { }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(url1).resize(200,200).into(holder.SetImage1);
                        }
                    });

                    Picasso.get().load(url2).networkPolicy(NetworkPolicy.OFFLINE).resize(200,200).into(holder.SetImage2, new Callback()
                    {
                        @Override
                        public void onSuccess() { }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(url2).resize(200,200).into(holder.SetImage2);
                        }
                    });

                    Picasso.get().load(url3).networkPolicy(NetworkPolicy.OFFLINE).resize(200,200).into(holder.SetImage3, new Callback()
                    {
                        @Override
                        public void onSuccess() { }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(url3).resize(200,200).into(holder.SetImage3);
                        }
                    });

                    Picasso.get().load(url4).networkPolicy(NetworkPolicy.OFFLINE).resize(200,200).into(holder.SetImage4, new Callback()
                    {
                        @Override
                        public void onSuccess() { }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(url4).resize(200,200).into(holder.SetImage4);
                        }
                    });

                    if(dataSnapshot.hasChild("Imagem 4"))
                        holder.SetImagens.setText("Imagens 4/4");
                    else if(dataSnapshot.hasChild("Imagem 3"))
                        holder.SetImagens.setText("Imagens 3/4");
                    else if(dataSnapshot.hasChild("Imagem 2"))
                        holder.SetImagens.setText("Imagens 2/4");
                    else if(dataSnapshot.hasChild("Imagem 1"))
                        holder.SetImagens.setText("Imagens 1/4");
                }
                else {
                    holder.ContainerImagens.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
        databaseReference.keepSynced(true);

        if(isOnline()) {
            SendFirebase(i);
        }
        SendExport(holder, i);
    }

    private void SendExport(UsuarioHolder holder, final int position)
    {
        holder.SendExport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String TXTLocal = "Local: " + dataList.get(position).getLocal() + "\n";
                String TXTLocalidade = "Localidade: " + dataList.get(position).getLocalidade() + "\n\n";
                String TXTLatitude = "Latitude: " + dataList.get(position).getLatitude() + "\n";
                String TXTLongitude = "Longitude: " + dataList.get(position).getLongitude() + "\n\n";
                String TXTResultado = "Resultado: " + dataList.get(position).getResultado() + "\n\n";
                String TXTData = "Data: " + dataList.get(position).getData() + "\n\n";
                String TXTGerar = "Chave de identificação de fitofisionomias gerado pelo Aplicativo: IDBio Mobile.";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, TXTLocal + TXTLocalidade + TXTLatitude + TXTLongitude + TXTResultado + TXTData + TXTGerar);
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, "Enviar Chave de identificação de fitofisionomias"));
            }
        });
    }

    private void SendFirebase(int position)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.sincronizar);
        dialog.setCancelable(false);
        dialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if(user != null)
        {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("IDB Mobile").child(user.getUid());

            String city;
            String state;
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses;
            try
            {
                addresses = gcd.getFromLocation(dataList.get(position).getLatitude(), dataList.get(position).getLongitude(), 1);

                city = addresses.get(0).getSubAdminArea();
                state = addresses.get(0).getAdminArea();

                databaseReference.child(state).child(city).child(dataList.get(position).getLocal()).child("Local").setValue(dataList.get(position).getLocal());
                databaseReference.child(state).child(city).child(dataList.get(position).getLocal()).child("Localidade").setValue(dataList.get(position).getLocalidade());
                databaseReference.child(state).child(city).child(dataList.get(position).getLocal()).child("Resultado").setValue(dataList.get(position).getResultado());
                databaseReference.child(state).child(city).child(dataList.get(position).getLocal()).child("Latitude").setValue(dataList.get(position).getLatitude());
                databaseReference.child(state).child(city).child(dataList.get(position).getLocal()).child("Longitude").setValue(dataList.get(position).getLongitude());
                databaseReference.child(state).child(city).child(dataList.get(position).getLocal()).child("Data").setValue(dataList.get(position).getData());

                dialog.dismiss();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null; NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
        {
            int networkType = activeNetwork.getType();
            return networkType == ConnectivityManager.TYPE_WIFI || networkType == ConnectivityManager.TYPE_MOBILE;
        } else { return false; }
    }
}