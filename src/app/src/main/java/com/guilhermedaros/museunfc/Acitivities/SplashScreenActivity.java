package com.guilhermedaros.museunfc.Acitivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.guilhermedaros.museunfc.Entities.ItemMuseu;
import com.guilhermedaros.museunfc.Models.ResponseItem;
import com.guilhermedaros.museunfc.R;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private Button btn_tentarnovamente;
    private View mProgressView;
    private Rollbar rollbar;

    private boolean verificaConexao() {
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return conectivtyManager.getActiveNetworkInfo() != null && conectivtyManager.getActiveNetworkInfo().isAvailable() && conectivtyManager.getActiveNetworkInfo().isConnected();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            btn_tentarnovamente.setVisibility(show ? View.GONE : View.VISIBLE);
            btn_tentarnovamente.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    btn_tentarnovamente.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            btn_tentarnovamente.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Rollbar.init(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);


        setContentView(R.layout.activity_splash_screen);

        btn_tentarnovamente = findViewById(R.id.btn_tentarnovamente);
        mProgressView = findViewById(R.id.splash_progress);
        showProgress(true);

        btn_tentarnovamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarApp();
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        iniciarApp();
    }

    private void iniciarApp() {

        if (verificaConexao()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://18.228.212.91:81/api/item?aativo=true",
                    null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String mJsonString = response.toString();
                            JsonParser parser = new JsonParser();
                            JsonElement mJson = parser.parse(mJsonString);
                            Gson gson = new Gson();
                            final ResponseItem object = gson.fromJson(mJson, ResponseItem.class);
                            Log.d("ResponseItem:", response.toString());
                            if (object.getSucesso()) {
                                Realm realm = Realm.getDefaultInstance();

                                RealmResults<ItemMuseu> results = realm.where(ItemMuseu.class).findAll();

                                realm.beginTransaction();

                                results.deleteAllFromRealm();

                                realm.commitTransaction();

                                for (final ItemMuseu lItem :
                                        object.getData()) {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm bgRealm) {
                                            bgRealm.copyToRealmOrUpdate(lItem);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                        }
                                    });
                                }
                            }
                            mostrarEscolhaUsuario();
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ResponseItem:", error.toString());
                }
            }
            );
            requestQueue.add(objectRequest);
        } else {
            Realm realm = Realm.getDefaultInstance();
            List<ItemMuseu> lItens = realm.where(ItemMuseu.class).findAll();

            if (lItens.size() > 0) {
                mostrarEscolhaUsuario();
            } else {
                Toast.makeText(this, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        }

    }

    private void mostrarEscolhaUsuario() {
        finish();
        SharedPreferences lSharedPreferences = getSharedPreferences("museunfc", MODE_PRIVATE);
        if (lSharedPreferences.getBoolean("usuariologado", false) || !lSharedPreferences.getBoolean("primeiravez", true)) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
            if (lSharedPreferences.getBoolean("primeiravez", true)) {
                Intent intent = new Intent(getBaseContext(), EscolhaUsuarioActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        }
    }

}
