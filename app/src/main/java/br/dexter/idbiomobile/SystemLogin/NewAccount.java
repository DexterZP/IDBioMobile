package br.dexter.idbiomobile.SystemLogin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.dexter.idbiomobile.R;
import in.myinnos.customfontlibrary.TypefaceUtil;

public class NewAccount extends AppCompatActivity
{
    private TextInputEditText emailTxt, senhaTxt, senhaTxt1;
    private TextInputLayout emailBox, senhaBox, senhaBox1;
    private Button cadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/minhafonte.ttf");
        setContentView(R.layout.activity_new_account);

        setTitle("Cadastrar uma conta");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();

        cadastrar = findViewById(R.id.cadastrar);

        emailBox = findViewById(R.id.textEmail);
        senhaBox = findViewById(R.id.textPassword);
        senhaBox1 = findViewById(R.id.textPassword1);

        emailTxt = findViewById(R.id.email);
        senhaTxt = findViewById(R.id.senha);
        senhaTxt1 = findViewById(R.id.senha1);

        Button();
    }

    public void Button()
    {
        cadastrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(emailTxt.getText().toString().isEmpty())
                {
                    emailBox.setError("Não deixe em branco");
                }
                else if(senhaTxt.getText().toString().isEmpty())
                {
                    senhaBox.setError("Não deixe em branco");
                }
                else if(senhaTxt1.getText().toString().isEmpty())
                {
                    senhaBox1.setError("Não deixe em branco");
                }
                else if(!senhaTxt1.getText().toString().equals(senhaTxt.getText().toString()))
                {
                    senhaBox1.setError("Senha não corresponde com a original");
                }
                else
                {
                    auth.createUserWithEmailAndPassword(emailTxt.getText().toString(), senhaTxt.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>()
                    {
                        @Override
                        public void onSuccess(AuthResult authResult)
                        {
                            Toast.makeText(NewAccount.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(NewAccount.this, "Erro na criação da conta!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
