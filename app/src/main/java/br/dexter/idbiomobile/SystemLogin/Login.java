package br.dexter.idbiomobile.SystemLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.app.Dialog;

import br.dexter.idbiomobile.MainActivity;
import br.dexter.idbiomobile.R;
import in.myinnos.customfontlibrary.TypefaceUtil;

public class Login extends AppCompatActivity
{
    private TextInputEditText emailTxt, senhaTxt;
    private TextInputLayout emailBox, senhaBox;
    private Button Entrar;
    private TextView cadastrar;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/minhafonte.ttf");
        setContentView(R.layout.activity_login);

        setTitle("Login");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        emailTxt = findViewById(R.id.email);
        senhaTxt = findViewById(R.id.senha);

        emailBox = findViewById(R.id.textEmail);
        senhaBox = findViewById(R.id.textPassword);

        Entrar = findViewById(R.id.login);

        cadastrar = findViewById(R.id.CreateAccount);

        Button();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(user != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void Button()
    {
        cadastrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this, NewAccount.class);
                startActivity(intent);
            }
        });

        Entrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(emailTxt.getText() != null && emailTxt.getText().toString().isEmpty())
                {
                    emailBox.setError("Não deixe em branco");
                }
                else if(senhaTxt.getText() != null && senhaTxt.getText().toString().isEmpty())
                {
                    senhaBox.setError("Não deixe em branco");
                }
                else
                {
                    final Dialog dialog = new Dialog(Login.this);
                    dialog.setContentView(R.layout.dialog_login);
                    dialog.setCancelable(false);
                    dialog.show();

                    auth.signInWithEmailAndPassword(emailTxt.getText().toString(), senhaTxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(Login.this, "Erro ao efetuar login", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(Login.this, "Erro ao efetuar login", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}