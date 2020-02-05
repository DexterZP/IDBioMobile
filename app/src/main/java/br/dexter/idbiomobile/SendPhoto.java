package br.dexter.idbiomobile;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.app.Dialog;

import br.dexter.idbiomobile.SQlite.BD;
import br.dexter.idbiomobile.SQlite.PhotoAdapter;
import in.myinnos.customfontlibrary.TypefaceUtil;

public class SendPhoto extends AppCompatActivity implements PhotoAdapter.CallbackInterface
{
    private String local, city, state;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/minhafonte.ttf");
        setContentView(R.layout.activity_sendphoto);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null) {

            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        setTitle("Enviar fotos");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        BD bd = new BD(this);
        PhotoAdapter adapter = new PhotoAdapter(this, bd.buscar());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        if(bd.buscar().size() == 0) {
            Toast.makeText(this, "Você não tem índices salvos", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 86 && resultCode == RESULT_OK && data.getClipData() != null)
        {
            final ClipData clipData = data.getClipData();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            ValueEventListener valueEventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.hasChild("Imagem 4")) {
                        Toast.makeText(SendPhoto.this, "Você não pode mais enviar imagens", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(dataSnapshot.hasChild("Imagem 3") && clipData.getItemCount() > 1) {
                        Toast.makeText(SendPhoto.this, "Você só pode enviar mais uma imagem", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(dataSnapshot.hasChild("Imagem 2") && clipData.getItemCount() > 2) {
                        Toast.makeText(SendPhoto.this, "Você só pode enviar mais duas imagem", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(dataSnapshot.hasChild("Imagem 1") && clipData.getItemCount() > 3) {
                        Toast.makeText(SendPhoto.this, "Você só pode enviar mais três imagem", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(clipData.getItemCount() > 4) {
                        Toast.makeText(SendPhoto.this, "Você só pode enviar até 4 imagens", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (int count = 0; count < clipData.getItemCount(); count++)
                    {
                        Uri imageUri = clipData.getItemAt(count).getUri();

                        EnviarFile(imageUri);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            };
            reference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).child(local).addListenerForSingleValueEvent(valueEventListener);
        }
        else if(data.getData() != null)
        {
            Uri imagePath = data.getData();

            EnviarFile(imagePath);
        }
        else {
            Toast.makeText(this, "Por favor, selecione pelo menos um arquivo", Toast.LENGTH_LONG).show();
        }
    }

    public void EnviarFile(final Uri uri)
    {
        if(user != null && uri.getLastPathSegment() != null)
        {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_login);
            dialog.setCancelable(false);
            dialog.show();

            final StorageReference ref = FirebaseStorage.getInstance().getReference().child(user.getUid()).child(local).child(uri.getLastPathSegment());
            final UploadTask uploadTask = ref.putFile(uri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        assert task.getException() != null;
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull final Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        ref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>()
                        {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata)
                            {
                                final Uri download = task.getResult();

                                ValueEventListener valueEventListener = new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        if(download != null)
                                        {
                                            if(dataSnapshot.hasChild("Imagem 4")) {
                                                Toast.makeText(SendPhoto.this, "Você não pode mais enviar imagens", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                            else if(dataSnapshot.hasChild("Imagem 3")) {
                                                databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).child(local).child("Imagem 4")
                                                        .setValue(download.toString());
                                                dialog.dismiss();
                                            }
                                            else if(dataSnapshot.hasChild("Imagem 2")) {
                                                databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).child(local).child("Imagem 3")
                                                        .setValue(download.toString());
                                                dialog.dismiss();
                                            }
                                            else if(dataSnapshot.hasChild("Imagem 1")) {
                                                databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).child(local).child("Imagem 2")
                                                        .setValue(download.toString());
                                                dialog.dismiss();
                                            }
                                            else {
                                                databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).child(local).child("Imagem 1")
                                                        .setValue(download.toString());
                                                dialog.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                };
                                databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).child(local).addListenerForSingleValueEvent(valueEventListener);
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(SendPhoto.this, "Falha no envio", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onHandleSelection(String local, String city, String state)
    {
        Intent intent;
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Escolha o seu Gerenciador de Arquivos"), 86);

        this.local = local;
        this.city = city;
        this.state = state;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}