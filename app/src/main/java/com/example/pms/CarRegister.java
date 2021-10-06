package com.example.pms;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CarRegister  extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView;
    private String jsonString;
    private EditText data1, data2, data3;
    private String data4;
    private static String IP_ADDRESS = "221.139.167.8";
    ArrayList<Customer> cusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        data1 = (EditText)findViewById(R.id.cName);
        data2 = (EditText)findViewById(R.id.cPnum);
        data3 = (EditText)findViewById(R.id.cCnum);
        data4 = "2021-10-06";

        Button button = (Button)findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = data1.getText().toString();
                String pnum = data2.getText().toString();
                String cnum = data3.getText().toString();
                String date = data4;

                data1.setText("");
                data2.setText("");
                data3.setText("");

                JsonParse jsonParse = new JsonParse();
                jsonParse.execute("http://" + IP_ADDRESS + "/register.php", name, pnum, cnum, date);

                Toast.makeText(getApplicationContext(), "id : "+name +" 님의 회원가입이 완료 되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        // toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    public class JsonParse extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        String TAG = "JsonParseTest";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String name = (String)params[1];
            String pnum = (String)params[2];
            String carNum = (String)params[3];
            String regDate = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&pnum=" + pnum + "&carNum=" + carNum + "&regDate=" + regDate;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Setting:
                Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_LONG).show();
                return true;

            case R.id.Mypage:
                Toast.makeText(getApplicationContext(), "마이페이지", Toast.LENGTH_LONG).show();
                return true;

            case R.id.Logout:
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}