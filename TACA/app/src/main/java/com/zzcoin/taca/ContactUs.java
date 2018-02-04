package com.zzcoin.taca;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

                String url = "http://www.lifehand-technology.fr/interface_mail.php?mail=" + mail +"&name=" + name + "&surname=" + surname + "&phone=" + phone;

               /* try{
                    URI uri = new URI(url);
                    HttpGet getHttpRe = new HttpGet();
                    getHttpRe.setURI(uri);
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(getHttpRe);
                    Toast.makeText(getApplicationContext(), httpResponse.toString(), Toast.LENGTH_LONG);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

              /*  try {
                    getRequete(url);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
                }*/

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

            }
        });


    }

    public void getRequete(String url) throws IOException {
        InputStream is = null;
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            is = conn.getInputStream();

        }
        finally {
            if(is != null) {
                is.close();
            }
        }

    }
}
