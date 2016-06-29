package com.example.getafix.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ResultActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String jsonString="";
    JSONObject jsonObj=null;
    JSONObject currentlyObj=null;
    String cityGlobal="";
    String stateGlobal="";
    String degreeGlobal="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        try {
            getSupportActionBar().setHomeButtonEnabled(false); // disable the button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
            getSupportActionBar().setDisplayShowHomeEnabled(false);

            final Intent intent = getIntent();
            jsonString = intent.getStringExtra("jsonValues");
            jsonObj= new JSONObject(jsonString);
            final String lat=jsonObj.getString("latitude");
            final String longitude=jsonObj.getString("longitude");
            currentlyObj= jsonObj.getJSONObject("currently");
            cityGlobal=intent.getStringExtra("city");
            stateGlobal=intent.getStringExtra("state");
            degreeGlobal=intent.getStringExtra("degree");
            //get the precipitation Value
            String precipitation=getPrecipitation((currentlyObj.getString("precipIntensity")));
            TextView precipView= (TextView) findViewById(R.id.precipValue);
            precipView.setText(precipitation);
            //get the rain chance Value
            String rainChance=getChanceOfRain(currentlyObj.getString("precipProbability"));
            TextView rainView= (TextView) findViewById(R.id.rainValue);
            rainView.setText(rainChance);
            //get wind speed
            String windSpeed=getWindSpeed(currentlyObj.getString("windSpeed"),intent);
            TextView windView= (TextView) findViewById(R.id.windValue);
            windView.setText(windSpeed);
            //get dew point value
            String dewPoint=getDewPoint(currentlyObj.getString("dewPoint"));
            TextView dewView= (TextView) findViewById(R.id.dewValue);
            dewView.setText(dewPoint);
            //get  humidity value
            String humidity=getHumidity(currentlyObj.getString("humidity"));
            TextView humidityView= (TextView) findViewById(R.id.humidityValue);
            humidityView.setText(humidity);
            //get daily.data[0]
            JSONObject dailyObj=jsonObj.getJSONObject("daily");
            JSONArray dailyZero= dailyObj.getJSONArray("data");
            JSONObject firstDay= dailyZero.getJSONObject(0);
            //get sunrise and sunset
            String sunriseTime=firstDay.getString("sunriseTime");
            String sunSetTime=firstDay.getString("sunsetTime");

            String timezone=jsonObj.getString("timezone");
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));

            long sunriselong=Integer.parseInt(sunriseTime);
            Date sunriseDate=new java.util.Date((long)sunriselong*1000);
            sunriseTime=dateFormat.format(sunriseDate);

            long sunsetlong= Integer.parseInt(sunSetTime);
            Date sunsetDate=new java.util.Date((long)sunsetlong*1000);
            sunSetTime=dateFormat.format(sunsetDate);

            TextView sunsetView= (TextView) findViewById(R.id.sunsetValue);
            sunsetView.setText(sunSetTime);

            TextView sunriseView= (TextView) findViewById(R.id.sunriseValue);
            sunriseView.setText(sunriseTime);
            //set summaryIconImage
            ImageView summaryIcon= (ImageView)findViewById(R.id.summaryImage);
            String icon=(currentlyObj.getString("icon"));
            setImageView(summaryIcon, icon);
            //set the summary value
            TextView summaryView= (TextView) findViewById(R.id.summaryDetails);
            String summaryStr= currentlyObj.getString("summary")+" in "+intent.getStringExtra("city")+", "+intent.getStringExtra("state");;
            summaryView.setText(summaryStr);
            //set the main temperature value
            TextView tempView= (TextView) findViewById(R.id.tempBig);
            String tempValue=currentlyObj.getString("temperature");
            tempView.setText(getFormatedTemp(tempValue));

            TextView visibilityView= (TextView) findViewById(R.id.visibilityValue);
            String visibilityValue;
            if((currentlyObj.has("visibility"))){
                visibilityValue= currentlyObj.getString("visibility");
            }else{
                visibilityValue= "NA";
            }
            if(intent.getStringExtra("degree").equalsIgnoreCase("us")){
                visibilityValue+=" mi";
            }else{
                visibilityValue+=" km";
            }
            visibilityView.setText((visibilityValue));
            //tempSmall
            TextView tempMinView= (TextView) findViewById(R.id.tempSmall);
            String tempMinStr= firstDay.getString("temperatureMin");
            String tempMaxStr= firstDay.getString("temperatureMax");
            String temp="L:"+(Math.round(Float.parseFloat(tempMinStr)))+"°| H:"+(Math.round(Float.parseFloat(tempMaxStr)))+"°";
            tempMinView.setText(temp);

            final Button button = (Button) findViewById(R.id.details);
            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intentDetails = new Intent(getApplicationContext(), DetailsActivity.class);
                    String jsonString = intent.getStringExtra("jsonValues");

                    Log.i("Json Result", jsonString);
                    String cityValue = intent.getStringExtra("city");
                    String stateValue = intent.getStringExtra("state");
                    String degree = intent.getStringExtra("degree");
                    intentDetails.putExtra("jsonValues", jsonString);
                    intentDetails.putExtra("city", cityValue);
                    intentDetails.putExtra("state", stateValue);
                    intentDetails.putExtra("degree", degree);
                    startActivity(intentDetails);
                }
            });
            final Button mapButton = (Button) findViewById(R.id.viewMap);
            mapButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intentMapDetails = new Intent(getApplicationContext(), MapActivity.class);
                    intentMapDetails.putExtra("lat",lat);
                    intentMapDetails.putExtra("long",longitude);
                    startActivity(intentMapDetails);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void callFaceBook(View view){
        String summary="";
        String description="";
        String url="";
        try{
            Log.i("facebook", "callFaceBook ");
            summary="Current Weather in "+cityGlobal+", "+stateGlobal;
            Log.i("summary", summary);
            description=currentlyObj.getString("summary")+", " + getDewPoint(currentlyObj.getString("temperature"));
            Log.i("description", description);
            url=getIconValue(currentlyObj.getString("icon"));
            Log.i("url", url);
        }catch(JSONException e){
            e.printStackTrace();
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Context context = getApplicationContext();
                CharSequence text = "";
                if (result.getPostId() == null) {
                    text = "Post Cancelled";
                } else {
                    text = "Facebook Post Successful";
                }
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {

            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(summary)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .setImageUrl(Uri.parse(url))
                    .build();
            shareDialog.show(content);
        }
    }
/*Log.i("resultCode",String.valueOf(resultCode));
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();*/
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
    public  String getFormatedTemp(String temp){
        int value = Math.round(Float.parseFloat(temp));
        String result= "";
        if(degreeGlobal.equalsIgnoreCase("us")){
            result =value+"°ᶠ";
        }else{
            result =value+"°ᶜ";
        }
        return result;
    }
    public void setImageView(ImageView summaryIcon, String icon){
        if(icon.equalsIgnoreCase("clear-day")){
            summaryIcon.setImageResource(R.drawable.clear);
        }
        else if(icon.equalsIgnoreCase("clear-night")){
            summaryIcon.setImageResource(R.drawable.clear_night);
        }
        else if(icon.equalsIgnoreCase("rain")){
            summaryIcon.setImageResource(R.drawable.rain);
        }
        else if(icon.equalsIgnoreCase("snow")){
            summaryIcon.setImageResource(R.drawable.snow);
        }
        else if(icon.equalsIgnoreCase("sleet")){
            summaryIcon.setImageResource(R.drawable.sleet);
        }
        else if(icon.equalsIgnoreCase("wind")){
            summaryIcon.setImageResource(R.drawable.wind);
        }
        else if(icon.equalsIgnoreCase("fog")){
            summaryIcon.setImageResource(R.drawable.fog);
        }
        else if(icon.equalsIgnoreCase("cloudy")){
            summaryIcon.setImageResource(R.drawable.cloudy);
        }
        else if(icon.equalsIgnoreCase("partly-cloudy-day")){
            summaryIcon.setImageResource(R.drawable.cloud_day);
        }
        else if(icon.equalsIgnoreCase("partly-cloudy-night")){
            summaryIcon.setImageResource(R.drawable.cloud_night);
        }
    }
    public String getTemp(Intent intent){
        String degree=intent.getStringExtra("degree");
        return degree;
    }
    public String getPrecipitation(String value){
        float val=Float.parseFloat(value);
        String precip="ERROR";
        if((val>=0) && (val<0.002)){
            precip="None";
        }else if((val>=0.002) && (val<0.017)){
            precip="Very Light";
        }else if((val>=0.017) && (val<0.1)){
            precip="Light";
        }else if((val>=0.1) && (val<0.4)){
            precip="Moderate";
        }else if(val>=0.4){
            precip="Heavy";
        }
        return precip;
    }

    public String getChanceOfRain(String val){
        Float value=Float.parseFloat(val);
        return (Math.round(value)*100)+" %";
    }

    public String getWindSpeed(String value,Intent intent){
        String unit= getTemp(intent);
        float val=(Float.parseFloat(value));
        String windSpeed="";
        if(unit.equalsIgnoreCase("us")){
            windSpeed=val+" mph";
        }else{
            windSpeed=val+" m/s";
        }
        return windSpeed;
    }
    public  String getDewPoint(String val){
        String unit= degreeGlobal;
        String dewPoint="";
        int value=Math.round(Float.parseFloat(val));
        if(unit.equalsIgnoreCase("us")){
            dewPoint=value+"° F";
        }else{
            dewPoint=value+"° C";
        }
        return dewPoint;
    }

    public String getHumidity(String val){
        float value= Float.parseFloat(val);
        return ((value)*100)+" %";
    }
    public String getIconValue(String val){
        String icon="";
        if(val.equalsIgnoreCase("clear-day")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/clear.png";
        }
        else if(val.equalsIgnoreCase("clear-night")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/clear_night.png";
        }
        else if(val.equalsIgnoreCase("rain")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/rain.png";
        }
        else if(val.equalsIgnoreCase("snow")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/snow.png";
        }
        else if(val.equalsIgnoreCase("sleet")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/sleet.png";
        }
        else if(val.equalsIgnoreCase("wind")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/wind.png";
        }
        else if(val.equalsIgnoreCase("fog")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/fog.png";
        }
        else if(val.equalsIgnoreCase("cloudy")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/cloudy.png";
        }
        else if(val.equalsIgnoreCase("partly-cloudy-day")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/cloud_day.png";
        }
        else if(val.equalsIgnoreCase("partly-cloudy-night")){
            icon="http://cs-server.usc.edu:45678/hw/hw8/images/cloud_night.png";
        }
        return icon;
    }


}
