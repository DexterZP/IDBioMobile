package br.dexter.idbiomobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rey.material.widget.Spinner;

import java.util.Calendar;
import java.util.TimeZone;

import in.myinnos.customfontlibrary.TypefaceUtil;

public class Adicionar extends AppCompatActivity
{
    public EditText mLocal, mLocalidade;
    public Spinner mDia, mMes, mAno;
    public Button mGerar;

    private Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

    //private int currentYear = calendar.get(Calendar.YEAR);
    private int currentMonth = calendar.get(Calendar.MONTH) + 1;
    private int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/minhafonte.ttf");
        setContentView(R.layout.activity_adicionar);

        setTitle("Adicionar");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mLocal = findViewById(R.id.local);
        mLocalidade = findViewById(R.id.localidade);

        mDia = findViewById(R.id.spinner_dia);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Dia, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDia.setSelection(currentDay-1);
        mDia.setAdapter(adapter);

        mMes = findViewById(R.id.spinner_mes);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Mes, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMes.setSelection(currentMonth-1);
        mMes.setAdapter(adapter1);

        mAno = findViewById(R.id.spinner_ano);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Ano, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAno.setAdapter(adapter2);

        mGerar = findViewById(R.id.gerar_relatorio);
        mGerar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mLocal.getText().toString().matches("") || mLocalidade.getText().toString().matches(""))
                {
                    Toast.makeText(Adicionar.this, "Não pode deixar o campo vazio.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(Adicionar.this, Perguntas.class);

                intent.putExtra("local_get", mLocal.getText().toString());
                intent.putExtra("localidade_get", mLocalidade.getText().toString());
                intent.putExtra("data_get", mDia.getSelectedItem().toString() + "/" + mMes.getSelectedItem().toString() + "/" + mAno.getSelectedItem().toString());

                finish();
                startActivity(intent);
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