package com.zzcoin.taca;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Contactez-Nous");
        setContentView(R.layout.activity_contact_us);

        final EditText mailText = (EditText) findViewById(R.id.mailText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);

        Button submitButton = (Button) findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String phone = phoneText.getText().toString();
                String mail = mailText.getText().toString();
                String delims = "[ ]+";
                String[] tokens = name.split(delims);
                name = tokens[0].toString();
                String surname = tokens[1].toString();

                if(name.matches("") || phone.matches("") || mail.matches("") || surname.matches("")){
                    AlertDialog emptyDialog = new AlertDialog.Builder(ContactUs.this).create();
                    emptyDialog.setTitle("Erreur");
                    emptyDialog.setMessage("Merci de remplir l'ensemble des champs.");
                }


               /* if(!accepterEmail(mail,mailText)){
                    final AlertDialog errorMail = new AlertDialog.Builder(ContactUs.this).create();
                    errorMail.setTitle("Erreur dans le mail !");
                    errorMail.setMessage("Erreur dans le format du mail. Merci de le modifier.");
                    errorMail.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    errorMail.dismiss();
                                    mailText.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(mailText,InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                    errorMail.show();
                }*/

                String url = "http://www.lifehand-technology.fr/interface_mail.php?mail=" + mail +"&name=" + name + "&surname=" + surname + "&phone=" + phone;

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), (CharSequence) error, Toast.LENGTH_LONG);
                    }
                });
                queue.add(stringRequest);

                AlertDialog alertDialog = new AlertDialog.Builder(ContactUs.this).create();
                alertDialog.setTitle("Envoyé !");
                alertDialog.setMessage("Votre demande de contact a bien été effectuée !");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(ContactUs.this,WelcomeActivity.class);
                                startActivity(intent);
                            }
                        });
                alertDialog.show();
            }
        });

    }

    public boolean accepterEmail(String mail, final EditText mailEdit) {
        boolean ok = false;
        String message = "", tex = mail.toString().trim();
        int posiArrobase = 0, posiPoint = 0, posi2 = 0;

        if (tex.indexOf(" ") > -1) {                          // signaler l'espace
            message = " il y a un blanc dans l''adresse email ";
        }
        if (message.length() == 0) {
            posiArrobase = tex.indexOf("@");
            if (posiArrobase < 0) {
                message = " arrobase (@) manque dans l'adresse email ";
            }
            if ((posiArrobase == 0) || (tex.endsWith("@"))) {
                message = " arrobase (@) mal placé dans l'adresse email ";
            }
            if ((posiArrobase > 0) && (posiArrobase < tex.length())) {
                posi2 = tex.indexOf("@",posiArrobase+1);
                if (posi2 > posiArrobase) {
                    message = " double arrobase (@) dans l'adresse email ";
                }
            }
            if (message.length() == 0) {
                posiPoint = tex.indexOf(".");
                if (posiPoint == -1) {
                    message = " on doit trouver au moins un point dans l'adresse email ";
                }
                if ((posiPoint == 0) || (tex.endsWith(".")))  {
                    message = " point mal placé dans l'adresse email ";
                }
            }
            if (message.length() == 0) {
                if (tex.length() == 0) {
                    message = " l'adresse email est vide ";
                }
            }
        }
        if (message.length() == 0) {
            ok = true;
        }
        else {
        }
        return(ok);
    }
}
