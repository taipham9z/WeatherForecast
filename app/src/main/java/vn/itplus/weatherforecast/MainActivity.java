package vn.itplus.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    String cityname = "Ha Noi";
    View decorView;
    LocationManager locationManager;
    private RelativeLayout homeRL, idRL;
    private LinearLayout idLLCloud, idLLHumidity, idLLWind;
    private ProgressBar loadingPB;
    ArrayList<WeatherRVModal> weatherRVModalArrayList;
    TextView idTVCityName, idTVTemperature, idTVCondition, idTVHumidity, idTVCloud, idTVWindSpeedCurrent;
    ImageView idIVSearch, idIVBack, idIVIcon, idIV10day;
    EditText idEdtCity;
    RecyclerView idRVWeather;
    WeatherRVAdapter weatherRVAdapter;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if (i == 0) {
                    decorView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });
        mapping();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBar());
        }
    }

    private int hideSystemBar() {
        return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }
    private void mapping() {
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        idRL = findViewById(R.id.idRL);
        idLLCloud = findViewById(R.id.idLLCloud);
        idLLHumidity = findViewById(R.id.idLLHumidity);
        idLLWind = findViewById(R.id.idLLWind);
        idIV10day = findViewById(R.id.idIV10day);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String[] split = currentDateandTime.split(":");
        int tgian = Integer.parseInt(split[0]) + 11;
        if (tgian >= 24) {
            tgian = tgian - 24;
        }
        if (tgian > 0 && tgian < 13) {
            idRL.setBackgroundResource(R.drawable.morning);
            for (int i = 0; i < idRL.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idRL.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLWind.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLWind.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLHumidity.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLHumidity.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLCloud.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLCloud.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }

        } else if (tgian >= 13 && tgian <= 18) {
            idRL.setBackgroundResource(R.drawable.afternoon);
            for (int i = 0; i < idRL.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idRL.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLWind.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLWind.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLHumidity.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLHumidity.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLCloud.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLCloud.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.BLACK); // Đổi màu cho TextView
                }
            }
        } else {
            idRL.setBackgroundResource(R.drawable.evening);
            for (int i = 0; i < idRL.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idRL.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.WHITE); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLWind.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLWind.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.WHITE); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLHumidity.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLHumidity.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.WHITE); // Đổi màu cho TextView
                }
            }
            for (int i = 0; i < idLLCloud.getChildCount(); i++) { // Lặp qua tất cả các child của LinearLayout
                View view = idLLCloud.getChildAt(i); // Lấy ra child thứ i
                if (view instanceof TextView) { // Kiểm tra xem child có phải là TextView hay không
                    TextView textView = (TextView) view; // Ép kiểu child thành TextView
                    textView.setTextColor(Color.WHITE); // Đổi màu cho TextView
                }
            }
        }

        idTVCondition = findViewById(R.id.idTVCondition);
        idRVWeather = findViewById(R.id.idRVWeather);
        idEdtCity = findViewById(R.id.idEdtCity);
        idIVBack = findViewById(R.id.idIVBack);
        idIVIcon = findViewById(R.id.idIVIcon);

        idTVCityName = findViewById(R.id.idTVCityName);
        idTVTemperature = findViewById(R.id.idTVTemperature);
        idIVSearch = findViewById(R.id.idIVSearch);

        idTVWindSpeedCurrent = findViewById(R.id.idTVWindSpeedCurrent);
        idTVCloud = findViewById(R.id.idTVCloud);
        idTVHumidity = findViewById(R.id.idTVHumidity);

        getWeatherCurrent();
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        idRVWeather.setAdapter(weatherRVAdapter);

        idIVSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = idEdtCity.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nhập tên thành phố trước khi ấn tìm kiếm", Toast.LENGTH_SHORT).show();
                } else {
                    getWeatherList(city);
                    getWeather(city);
                    idEdtCity.clearFocus();
                }
            }
        });
        idIV10day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idEdtCity.getText().toString().equals("")) {
                    Intent intent = new Intent(MainActivity.this, WeatherNext10day.class);
                    intent.putExtra("city", idEdtCity.getText().toString().trim());
                    intent.putExtra("cityname", idEdtCity.getText().toString().trim());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                } else {
                    Toast.makeText(MainActivity.this, "Tên thành phố không được để trống", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getWeatherCurrent() {
//        idTVCityName.setText("Hà Nội");
//        getWeatherList("Ha Noi");
        getLastLocation();
        getWeather(cityname);
        getWeatherList(cityname);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addressList = null;
                        try {
                            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            cityname = addressList.get(0).getLocality().toString().trim();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    }else{
                        askPermission();
                    }
                }
            });
            return;
        }

    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Vui lòng cấp quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getWeather(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&lang=vi&appid=5731ff9333bc65cd5fe285e14d01b352";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            idTVCityName.setText(name);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Nơi bạn vừa nhập không tồn tại ...", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void getWeatherList(String cityName){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=9946e28297874f61a90112012231203&q="+cityName+"&lang=vi&days=10&aqi=no&alerts=no";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        homeRL.setVisibility(View.VISIBLE);
                        weatherRVModalArrayList.clear();
                        try {
                            String temperature = response.getJSONObject("current").getString("temp_c") + "°C";
                            JSONObject current = response.getJSONObject("current");
                            String humidity = current.getString("humidity") +"%";idTVHumidity.setText(humidity);
                            String cloud = current.getString("cloud")+"%";idTVCloud.setText(cloud);
                            String windspeddcurrent = current.getString("wind_kph")+ "km/h";idTVWindSpeedCurrent.setText(windspeddcurrent);
                            idTVTemperature.setText(temperature+"°c");
                            int isDay = response.getJSONObject("current").getInt("is_day");
                            String  condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            String  conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Picasso.get().load("http:".concat(conditionIcon)).into(idIVIcon);
                            idTVCondition.setText(condition);
                            if(isDay == 1){
                                //moring
                                Picasso.get().load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.com%2Ffree-photos-vectors%2Fmorning-sky&psig=AOvVaw0KkxYh5A8YOD8TSRz_yzmt&ust=1678796154992000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCNjl1JXx2P0CFQAAAAAdAAAAABAE").into(idIVBack);

                            }else {
                                Picasso.get().load("https://www.google.com/url?sa=i&url=https%3A%2F%2Funsplash.com%2Fs%2Fphotos%2Fevening-sky&psig=AOvVaw3ElnYg7U2F-5QJpCUDAhdG&ust=1678796227887000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCPDnu7jx2P0CFQAAAAAdAAAAABAJ").into(idIVBack);
                            }
                            JSONObject forecastObj = response.getJSONObject("forecast");
                            JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray hourArray = forecast0.getJSONArray("hour");

                            for (int i = 0; i < hourArray.length(); i++) {
                                JSONObject hourObj = hourArray.getJSONObject(i);
                                String time = hourObj.getString("time");
                                String temper = hourObj.getString("temp_c");
                                String img = "https:"+hourObj.getJSONObject("condition").getString("icon");
                                String wind = hourObj.getString("wind_kph");


                                weatherRVModalArrayList.add(new WeatherRVModal(time, temper, img, wind, windspeddcurrent, humidity, cloud));
                            }
                            weatherRVAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(MainActivity.this, "Nơi bạn nhập không tồn tại'", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}