package com.example.homepc.webserviceweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button show;
    TextView id,main,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding objects to views
        show=findViewById(R.id.showBtn);
        id=findViewById(R.id.idTV);
        main=findViewById(R.id.mainTV);
        desc=findViewById(R.id.descTV);

        //our api url which contain json
        String url="http://samples.openweathermap.org/data/2.5/weather?q=India,uk&appid=b1b15e88fa797225412429c1c50c122a1";

        //using 3rd party httpclient, efficient one
        OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder().url(url).build(); //creating request url
        final HashMap<String,String> weatherInfo= new HashMap<String, String>(); //a hashmap to store weather details

        //using okHttp's async  => enqueue , which as OnFailure and OnResponse method
        client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string().toString();
                    try {
                        JSONObject jsonObject= new JSONObject(responseData);
                        JSONArray root=jsonObject.getJSONArray("weather");
                        JSONObject finalObject=root.getJSONObject(0);
                        while(!root.isNull(0)){
                            weatherInfo.put("id",Integer.toString(finalObject.getInt("id")));
                            weatherInfo.put("main",finalObject.getString("main").toString());
                            weatherInfo.put("desc",finalObject.getString("description").toString());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        //method to show results
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Desc : "+weatherInfo.get("description"), Toast.LENGTH_SHORT).show();
                id.setText(weatherInfo.getOrDefault("id","SHIT"));
                main.setText(weatherInfo.get("main"));
                desc.setText(weatherInfo.get("desc"));
            }
        });
    }
}
