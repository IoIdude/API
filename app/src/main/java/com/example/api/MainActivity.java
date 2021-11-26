package com.example.api;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    Button btn;
    String futureJokeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txtJoke);
        btn = findViewById(R.id.btnClick);

        btn.setOnClickListener(view -> new JokeLoad().execute());
    }

    private class JokeLoad extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            String hrefAPI = "https://api.chucknorris.io/jokes/random";
            String jsonString = getJson(hrefAPI);

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                futureJokeString = String.format("%s\n%s\n%s\n%s\n%s\n%s",
                        jsonObject.getString("created_at"), jsonObject.getString("icon_url"),
                        jsonObject.getString("id"), jsonObject.getString("updated_at"),
                        jsonObject.getString("url"), jsonObject.getString("value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();

            futureJokeString = "";
            txt.setText("Загрузка...");
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            if (!futureJokeString.equals(""))
            {
                txt.setText(futureJokeString);
            }
        }
    }

    private String getJson(String href)
    {
        String data = "";

        try {
            URL url = new URL(href);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                data = bufferedReader.readLine();
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}