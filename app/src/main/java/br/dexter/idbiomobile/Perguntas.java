package br.dexter.idbiomobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rey.material.app.Dialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.dexter.idbiomobile.SQlite.BD;
import br.dexter.idbiomobile.SQlite.Data;
import in.myinnos.customfontlibrary.TypefaceUtil;

public class Perguntas extends AppCompatActivity
{
    private Data usuario = new Data();

    private Button Resposta1, Resposta2, Proxima;
    private ImageView Image1, Image2;
    private TextView PalavraChave;
    private CardView card2;
    private int Prox = 1;

    private float latitude, longitude;
    private String city, state;

    private LinearLayout linear1, linear2;
    private Button PermitirLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/minhafonte.ttf");
        setContentView(R.layout.activity_perguntas);

        Resposta1 = findViewById(R.id.resposta1);
        Resposta2 = findViewById(R.id.resposta2);
        Proxima = findViewById(R.id.Proximo);

        linear1 = findViewById(R.id.Linear1);
        linear2 = findViewById(R.id.Linear2);
        PermitirLocation = findViewById(R.id.Permissao);

        Image1 = findViewById(R.id.Imagem1);
        Image2 = findViewById(R.id.Imagem2);

        card2 = findViewById(R.id.Card2);

        PalavraChave = findViewById(R.id.PalavraChave);

        setTitle("Perguntas");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        VeficarButton();
        Verificar();
    }

    public void Verificar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);

            Inicializar();
        }

        PermitirLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    linear1.setVisibility(View.GONE);
                    linear2.setVisibility(View.VISIBLE);

                    Inicializar();
                }
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (coarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);

            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Inicializar();
    }

    private void Inicializar() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = (float) location.getLatitude();
                longitude = (float) location.getLongitude();

                city = null;
                state = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    city = addresses.get(0).getSubAdminArea();
                    state = addresses.get(0).getAdminArea();

                    dialog.dismiss();
                } catch (IOException e) {
                    city = "null";
                    state = "null";
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
            dialog.dismiss();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

            dialog.show();
        }
    }

    private void showGPSDisabledAlertToUser()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Percebemos que seu GPS está desativado, para uma melhor experiência, precisamos que ative seu GPS")
                .setCancelable(false)
                .setPositiveButton("Vamos ativar",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @SuppressLint("SetTextI18n")
    private void VeficarButton()
    {
        if(Prox == 1)
        {
            Resposta1.setText("1 Comunidade cuja forma de vida predominante é a arbórea (floresta)");
            Resposta2.setText("1' Comunidade cuja forma de vida predominante não é a arbórea (savana ou campo)");

            Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.savana).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 2;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 11;
                    VeficarButton();
                }
            });
        }

        if(Prox == 2)
        {
            Resposta1.setText("2 Comunidade submetida a clima úmido; sem estação seca e sem deciduidade de copa");
            Resposta2.setText("2' Comunidade submetida a clima sazonalmente marcante ao longo do ano (seca e chuva)");

            Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 3;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 8;
                    VeficarButton();
                }
            });
        }

        if(Prox == 3)
        {
            Resposta1.setText("3 Comunidade sem influência marítima");
            Resposta2.setText("3' Comunidade com influência marítima");

            Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.manguezal).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 5;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 4;
                    VeficarButton();
                }
            });
        }

        if(Prox == 4)
        {
            Resposta1.setText("4 Comunidade desenvolvida sobre solo arenoso");
            Resposta2.setText("4' Comunidade desenvolvida sobre solo argiloso, permanentemente encharcado");

            Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta de Restinga (Floresta Ombrófila)");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.restinga).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nManguezal");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.manguezal).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 5)
        {
            Resposta1.setText("5 Comunidade com elevada altura de copa (próximo de 25 m)");
            Resposta2.setText("5' Comunidade submetida a um clima úmido/nebuloso; com menor altura de copa; localizada em elevadas altitudes");

            Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 6;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta Ombrófila de altitude");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.ombrofila_densa).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 6)
        {
            Resposta1.setText("6 Comunidade sem a presença marcante de gimnosperma");
            Resposta2.setText("6' Comunidade dominada por árvores; com a presença marcante de gimnosperma (Araucaria angustifolia)");

            Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.ombrofila_mista).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 7;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta Ombrófila Mista");
                    Picasso.get().load(R.drawable.ombrofila_mista).resize(400,170).into(Image1);
                    Image1.setVisibility(View.GONE);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 7)
        {
            Resposta1.setText("7 Comunidade com árvores afastadas e dossel descontínuo (estrutura aberta), geralmente associada à bambus espinhosos");
            Resposta2.setText("7' Comunidade com árvores próximas e dossel contínuo (estrutura densa); com árvores emergentes sobre a copa, ultrapassando 30 m");

            Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.ombrofila_densa).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta Ombrófila Aberta");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.floresta_ombrofila).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta Ombrófila Densa");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.ombrofila_densa).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 8)
        {
            Resposta1.setText("8 Comunidade sem deciduidade da copa na estação seca, com lençol freático elevado, impedindo o défice hídrico da vegetação");
            Resposta2.setText("8' Comunidade com deciduidade da copa evidente na estação seca");

            Picasso.get().load(R.drawable.estacional_sempreverde).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta Estacional Sempre-Verde");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.estacional_sempreverde).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 9;
                    VeficarButton();
                }
            });
        }

        if(Prox == 9)
        {
            Resposta1.setText("9 Comunidade com deciduidade da copa entre 20 a 50 % na estação seca");
            Resposta2.setText("9' Comunidade com deciduidade da copa entre 50 a 100 % na estação seca; com elevado défice hídrico edáfico");

            Picasso.get().load(R.drawable.cerradao).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 10;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta Estacional Decidual");
                    Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 10)
        {
            Resposta1.setText("10 Comunidade predominada por elementos florísticos de cerrado");
            Resposta2.setText("10' Comunidade sem predomínio de elementos florísticos de cerrado");

            Picasso.get().load(R.drawable.cerradao).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nCerradão (Floresta Estacional Semidecidual)");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.cerradao).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nFloresta Estacional Semidecidual");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.floresta_estacional).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 11)
        {
            Resposta1.setText("11 Comunidade cujas formas de vida são variavelmente árvores, arbustos e ervas (savana)");
            Resposta2.setText("11' Comunidade cuja forma de vida predominante é herbácea (campo)");

            Picasso.get().load(R.drawable.savana).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.campos_sulinos).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 12;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 15;
                    VeficarButton();
                }
            });
        }

        if(Prox == 12)
        {
            Resposta1.setText("12 Comunidade submetida a um clima sazonal tropical; pode ter espécies de aspecto xeromórfico");
            Resposta2.setText("12' Comunidade submetida a um clima semiárido; com deciduidade completa na estação seca; sobre solo raso e pedregoso; com presença marcante de cactos");

            Picasso.get().load(R.drawable.savana).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.savana_rupestre).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 13;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nSavana estépica/caatinga s.s.");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.savana).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 13)
        {
            Resposta1.setText("13 Comunidade sobre solo profundo ou sazonalmente encharcado");
            Resposta2.setText("13' Comunidade sobre solo raso e pedregoso/couraçado");

            Picasso.get().load(R.drawable.savana).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.savana_rupestre).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 14;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nCerrado rupestre (Savana)");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.savana_rupestre).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 14)
        {
            Resposta1.setText("14 Comunidade sobre solo profundo e distrófico; sem presença marcante de palmeiras");
            Resposta2.setText("14' Comunidade sobre solo sazonalmente encharcado; com presença marcante de palmeiras, geralmente de uma única espécie (Mauritia flexuosa – Buriti; Acrocomia aculeata – Macaúba; Attalea speciosa – Babaçu; Copernicia prunifera – Carnaubal)");

            Picasso.get().load(R.drawable.savana).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.palmeira).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nSavana/cerrado s.s.");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.savana).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nPalmeiral");
                    Picasso.get().load(R.drawable.palmeira).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 15)
        {
            Resposta1.setText("15 Comunidade desenvolvida sobre solo raso e pedregoso/couraçado");
            Resposta2.setText("15' Comunidade desenvolvida sobre solo profundo");

            Picasso.get().load(R.drawable.campo_rupestre).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.campo_de_altitude).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 16;
                    VeficarButton();
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Prox = 17;
                    VeficarButton();
                }
            });
        }

        if(Prox == 16)
        {
            Resposta1.setText("16 Comunidade cujo embasamento é quartzo");
            Resposta2.setText("16' Comunidade cujo embasamento é granito ou gnaisse");

            Picasso.get().load(R.drawable.campo_rupestre).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.campo_de_altitude).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nCampo rupestre");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.campo_rupestre).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nCampo de altitude");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.campo_de_altitude).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }

        if(Prox == 17)
        {
            Resposta1.setText("17 Comunidade submetida a um clima sazonal tropical, sem geadas anuais");
            Resposta2.setText("17' Comunidade submetida a um clima sazonal subtropical, com geadas anuais");

            Picasso.get().load(R.drawable.cerrado_limpo).resize(400,170).into(Image1);
            Picasso.get().load(R.drawable.campos_sulinos).resize(400,170).into(Image2);

            Resposta1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nCampo limpo");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.cerrado_limpo).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });

            Resposta2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Resposta1.setText("Resultado:\nCampo sulino");
                    Resposta1.setClickable(false);
                    Picasso.get().load(R.drawable.campos_sulinos).resize(400,170).into(Image1);
                    card2.setVisibility(View.GONE);
                    PalavraChave.setVisibility(View.GONE);

                    Proxima.setVisibility(View.VISIBLE);
                    Proxima.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            VerificarUsers();
                        }
                    });
                }
            });
        }
    }

    private void VerificarUsers()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            Bundle bundle = intent.getExtras();
            if(bundle != null)
            {
                usuario.setId(bundle.getLong("id"));
                usuario.setLocal(bundle.getString("local"));
                usuario.setLocalidade(bundle.getString("localidade"));
                usuario.setLatitude(bundle.getFloat("latitude"));
                usuario.setLongitude(bundle.getFloat("longitude"));
                usuario.setCity(bundle.getString("city"));
                usuario.setState(bundle.getString("state"));
                usuario.setData(bundle.getString("data"));
                usuario.setResultado(bundle.getString("resultado"));
            }
        }
        SalvarUsuario();
    }

    private void SalvarUsuario()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            String local = intent.getStringExtra("local_get");
            String localidade = intent.getStringExtra("localidade_get");
            String data = intent.getStringExtra("data_get");

            usuario.setLocal(local);
            usuario.setLocalidade(localidade);
            usuario.setLatitude(latitude);
            usuario.setLongitude(longitude);
            usuario.setCity(city);
            usuario.setState(state);
            usuario.setData(data);
            usuario.setResultado(Resposta1.getText().toString());

            BD bd = new BD(this);
            bd.inserir(usuario);
        }

        Intent intent1 = new Intent(Perguntas.this, MainActivity.class);
        startActivity(intent1);
        finish();
        Toast.makeText(Perguntas.this, "Salvo com sucesso!!!", Toast.LENGTH_LONG).show();
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