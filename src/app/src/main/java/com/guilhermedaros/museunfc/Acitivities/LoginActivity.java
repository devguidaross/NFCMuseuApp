package com.guilhermedaros.museunfc.Acitivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.guilhermedaros.museunfc.Models.ResponseItem;
import com.guilhermedaros.museunfc.Models.ResponseUsuario;
import com.guilhermedaros.museunfc.Models.UsuarioLoginRequest;
import com.guilhermedaros.museunfc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    // UI references.
    private AutoCompleteTextView mLogin;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mLoginFormView.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mLogin = (AutoCompleteTextView) findViewById(R.id.login);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {

        // Reset errors.
        mLogin.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLogin.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(login)) {
            mLogin.setError(getString(R.string.error_field_required));
            focusView = mLogin;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            if (verificaConexao()){
                UsuarioLoginRequest loginRequest = new UsuarioLoginRequest(login, password);
                JSONObject loginRequestJson = new JSONObject();
                try {
                    loginRequestJson = loginRequest.toJSON();
                }
                catch (JSONException e){

                }
                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        "http://18.228.212.91:81/api/Usuario",
                        loginRequestJson,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String mJsonString = response.toString();
                                JsonParser parser = new JsonParser();
                                JsonElement mJson =  parser.parse(mJsonString);
                                Gson gson = new Gson();
                                final ResponseUsuario object = gson.fromJson(mJson, ResponseUsuario.class);
                                Log.e("ResponseUsuario:", response.toString());
                                if (object.getSucesso()){
                                    showProgress(false);
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    SharedPreferences lSharedPreferences = getSharedPreferences("museunfc", MODE_PRIVATE);
                                    SharedPreferences.Editor lEditor =  lSharedPreferences.edit();
                                    lEditor.putBoolean("usuariologado", true);
                                    lEditor.apply();


                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                    finish();
                                }
                                else{
                                    showProgress(false);
                                    Toast.makeText(getBaseContext(),object.getMensagem(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                }

                );
                requestQueue.add(objectRequest);
            }
            else
            {
                Toast.makeText(getBaseContext(),"Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        }
    }

    private boolean verificaConexao() {
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return conectivtyManager.getActiveNetworkInfo() != null && conectivtyManager.getActiveNetworkInfo().isAvailable() && conectivtyManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mLogin.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

