package com.example.getafix.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public final static String cityVal = "com.example.getafix.myapplication.cityVal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TextView headingView = (TextView) findViewById(R.id.heading);
        headingView.setTypeface(null, Typeface.BOLD);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //// TODO: 11/16/2015 Change the colour of the text, Change size. and duplicate for all
    public void validateInput(View view) {
        //Intent intent= new Intent(this,Validation.class);
        EditText street = (EditText) findViewById(R.id.street);
        EditText city = (EditText) findViewById(R.id.city);
        Spinner state = (Spinner) findViewById(R.id.states);
        String cityValue = city.getText().toString();
        String streetValue = street.getText().toString();
        String stateValue = state.getSelectedItem().toString();


        if (streetValue.isEmpty()) {
            TextView streetView = (TextView) findViewById(R.id.errorText);
            streetView.setText("Please enter a Street");
        } else if (cityValue.isEmpty()) {
            TextView cityView = (TextView) findViewById(R.id.errorText);
            cityView.setText("Please enter a City");
        } else if (stateValue.equalsIgnoreCase("Select a State...")) {
            TextView stateView = (TextView) findViewById(R.id.errorText);
            stateView.setText("Please select a State");
        } else {
            cityValue = cityValue.replaceAll(" ", "+");
            streetValue = streetValue.replaceAll(" ", "+");
            RadioGroup rg = (RadioGroup)findViewById(R.id.clearButton);
            String radiovalue = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
            Extracter extract = new Extracter();
            try {
                URL url=null;
                if(radiovalue.equalsIgnoreCase("Fahrenheit")){
                    url = new URL("http://cs-server.usc.edu:22045/forecast2.php?myaddress=" + streetValue + "&mycity=" + cityValue + "&myStates=" + stateValue + "&degree=Fahrenheit");
                    Log.i("URL", url.toString());
                }else{
                    url = new URL("http://cs-server.usc.edu:22045/forecast2.php?myaddress=" + streetValue + "&mycity=" + cityValue + "&myStates=" + stateValue + "&degree=Celsius");
                    Log.i("URL", url.toString());
                }
                extract.execute(url);
            } catch (Exception e) {
                Log.e("Exeption URL", e.toString());
            }
        }
    }
    public void redirectForecast(View v){
        Intent viewIntent =new Intent("android.intent.action.VIEW",Uri.parse("http://forecast.io/"));
        startActivity(viewIntent);
    }

    public void aboutYourself(View view){
        Intent intent = new Intent(getApplicationContext(), WeatherApp.class);
        startActivity(intent);
    }
    public void clearData(View view){
        TextView error=(TextView) findViewById(R.id.errorText);
        error.setText("");
        EditText street = (EditText) findViewById(R.id.street);
        street.setText("");
        EditText city = (EditText) findViewById(R.id.city);
        city.setText("");
        Spinner state = (Spinner) findViewById(R.id.states);
        state.setSelection(((ArrayAdapter<String>) state.getAdapter()).getPosition("Select a State..."));
        RadioGroup rg = (RadioGroup)findViewById(R.id.clearButton);
        rg.check(R.id.FDegree);
    }
    public static String readStream(InputStream inputStream) throws java.io.IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
    private class Extracter extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... url) {
            String json="";
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url[0].openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                json=MainActivity.readStream(in);
                Log.i("JSON", json);
            }catch(Exception e){
                e.printStackTrace();
            }
            return json;
        }
        public String getTemp(){
            RadioGroup rg = (RadioGroup)findViewById(R.id.clearButton);
            String radiovalue = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
            Log.i("radioValue", radiovalue);
            if(radiovalue.equalsIgnoreCase("Fahrenheit")){
                return "us";
            }else{
                return "is";
            }
        }
        protected void onPostExecute(String jsonResult) {
            TextView error=(TextView) findViewById(R.id.errorText);
            error.setText("");
            EditText city = (EditText) findViewById(R.id.city);
            String cityValue = city.getText().toString();
            Spinner state = (Spinner) findViewById(R.id.states);
            String stateValue = state.getSelectedItem().toString();
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("jsonValues", jsonResult);
            intent.putExtra("city",cityValue);
            intent.putExtra("state",stateValue);
            intent.putExtra("degree",getTemp());
            startActivity(intent);
        }
    }
}
