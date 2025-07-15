package com.example.androidassignments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class WeatherForecast extends AppCompatActivity {
    // Declare required variables and constants
    private static final String ACTIVITY_NAME = "Weather Forecast";
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView current_temp;
    private TextView min_temp;
    private TextView max_temp;
    private List<String> cityList;
    private TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_forecast);

        // create variables for the view elements
        progressBar = findViewById(R.id.progress_bar);
        current_temp = findViewById(R.id.current_temp);
        min_temp = findViewById(R.id.min_temp);
        max_temp = findViewById(R.id.max_temp);
        imageView = findViewById(R.id.image_forecast);
        cityName = findViewById(R.id.cityName);

        select_city();

    }

    // function to load the cities array and assign the values to our spinner object
    public void select_city() {
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));

        final Spinner spinner = findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.cities,
                        android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView,
                                               View view, int i, long l) {
                        progressBar.setVisibility(View.VISIBLE);
                        new ForecastQuery(cityList.get(i)).execute();
                        cityName.setText(cityList.get(i) + " Weather");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
    }

    // declare a class to handle asynchronous operation of retrieving weather information from a remote server
    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        // declare variables
        private String currentTemp;
        private String minTemp;
        private String maxTemp;
        private Bitmap image;
        private String city;

        ForecastQuery(String city) {
            this.city = city;
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(
                        "https://api.openweathermap.org/" +
                                "data/2.5/weather?q=" + this.city + "," +
                                "ca&APPID=79cecf493cb6e52d25bb7b7050ff723c&" +
                                "mode=xml&units=metric");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream in = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);

                // iterate over the async results to extract the required weather information
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        switch (parser.getName()) {
                            case "temperature":
                                currentTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                                break;

                            case "weather":
                                String iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";
                                Log.i(ACTIVITY_NAME, "Looking for file: " + fileName);

                                if (fileExistence(fileName)) {
                                    try (FileInputStream fis = openFileInput(fileName)) {
                                        Log.i(ACTIVITY_NAME, "Found the file locally");
                                        image = BitmapFactory.decodeStream(fis);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    String iconUrl = "https://openweathermap.org/img/w/" + fileName;
                                    image = HTTPUtils.getImage(new URL(iconUrl));
                                    if (image != null) {
                                        try (FileOutputStream outputStream =
                                                     openFileOutput(fileName, Context.MODE_PRIVATE)) {
                                            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                            Log.i(ACTIVITY_NAME, "Downloaded the file from the Internet");
                                            outputStream.flush();
                                            outputStream.close();
                                        }
                                    }
                                }
                                publishProgress(100);
                                break;

                        }
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            // Clear old data when you select a new city from the list
            current_temp.setText("Loading...");
            min_temp.setText("");
            max_temp.setText("");
            imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);



            // Update UI with weather data
            current_temp.setText(currentTemp + "°C");
            min_temp.setText("Min: " + minTemp + "°C");
            max_temp.setText("Max: " + maxTemp + "°C");

            if (image != null) {
                imageView.setImageBitmap(image);
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }

        // function to check is the required file already exists
        private boolean fileExistence(String fileName) {
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }


    }

    // class to retrieve weather image either from device storage or from the remote server
    public static class HTTPUtils {
        public static Bitmap getImage(URL url) throws IOException {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(30000);
                connection.setConnectTimeout(30000);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStream inputStream = connection.getInputStream()) {
                        return BitmapFactory.decodeStream(inputStream);
                    }
                }
                return null;
            } catch (Exception e) {
                Log.e(ACTIVITY_NAME, "Error downloading image", e);
                return null;

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }


}