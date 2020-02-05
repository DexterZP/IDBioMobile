package br.dexter.idbiomobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;
import com.rey.material.widget.Button;

import br.dexter.idbiomobile.SystemLogin.Login;
import in.myinnos.customfontlibrary.TypefaceUtil;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView userTxt;
    private Button logout, Adicionar, Salvos, SendPhoto, Sobre;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/minhafonte.ttf");
        setContentView(R.layout.activity_main);

        setTitle("Menu principal");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userTxt = findViewById(R.id.User);
        logout = findViewById(R.id.logout);

        Adicionar = findViewById(R.id.Adicionar);
        Salvos = findViewById(R.id.Salvos);
        SendPhoto = findViewById(R.id.SendPhoto);
        Sobre = findViewById(R.id.Sobre);

        Button();
    }

    @SuppressLint("SetTextI18n")
    public void Button()
    {
        if(user != null) {
            userTxt.setText("Conectado em\n" + user.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        Adicionar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Adicionar.class);
                startActivity(intent);
            }
        });

        Salvos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Visualizar.class);
                startActivity(intent);
            }
        });

        SendPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, SendPhoto.class);
                startActivity(intent);
            }
        });

        Sobre.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, SobreNos.class);
                startActivity(intent);
            }
        });
    }
}