package vn.itplus.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherNext10day extends AppCompatActivity {
    ImageView imgback;
    TextView txtName;
    ListView lv;
    CustomAdapter10 customAdapter;
    ArrayList<ThoiTiet10day> thoiTietdays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_next10day);
        mapping();

    }
    private void getWeather(String cityName){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&units=metric&lang=vi&appid=5731ff9333bc65cd5fe285e14d01b352";
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherNext10day.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            txtName.setText(name);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(WeatherNext10day.this, "Nơi bạn vừa nhập không tồn tại ...", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    private void getWeather10day(String cityName){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=9946e28297874f61a90112012231203&q="+cityName+"&lang=vi&days=10&aqi=no&alerts=no";
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherNext10day.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject forecast = response.getJSONObject("forecast");
                            JSONArray forecastday = forecast.getJSONArray("forecastday");
                            for (int i = 0; i < forecastday.length(); i++) {
                                JSONObject item = forecastday.getJSONObject(i);
                                String x = item.getString("date");

//                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
//                                String date = simpleDateFormat.format(x);
                                Locale vn = new Locale("vi", "VN");
                                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat output = new SimpleDateFormat("EEEE yyyy-MM-dd", vn);
                                String date="";
                                try{
                                    Date t = input.parse(x);
                                    date = output.format(t);
                                } catch (ParseException e){
                                    e.printStackTrace();
                                }

                                JSONObject day = item.getJSONObject("day");
                                String maxtemp_c = day.getString("maxtemp_c");
                                String mintemp_c = day.getString("mintemp_c");

                                JSONObject condition = day.getJSONObject("condition");
                                String icon = "https:" + condition.getString("icon");
                                String status = condition.getString("text");

                                thoiTietdays.add(new ThoiTiet10day(date, status, icon, maxtemp_c, mintemp_c));
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(WeatherNext10day.this, "Nơi bạn vừa nhập không tồn tại ...", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    private void mapping() {
        imgback = findViewById(R.id.imageviewBack);
        txtName = findViewById(R.id.txtTenThanhPho);
        lv = findViewById(R.id.listView);

        thoiTietdays = new ArrayList<>();
        customAdapter = new CustomAdapter10(WeatherNext10day.this, thoiTietdays);
        lv.setAdapter(customAdapter);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        getWeather10day(city);
        getWeather(city);
    }

}