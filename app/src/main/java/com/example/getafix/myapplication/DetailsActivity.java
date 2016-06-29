package com.example.getafix.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Remove Action bar back button
        getSupportActionBar().setHomeButtonEnabled(false); // disable the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //summary
        final RelativeLayout hourly=(RelativeLayout)this.findViewById(R.id.hourlyLayout);
        final RelativeLayout weekly=(RelativeLayout)this.findViewById(R.id.weeklyLayout);
        weekly.setVisibility(RelativeLayout.GONE);
        hourly.setVisibility(RelativeLayout.VISIBLE);
        Log.i("RESULTNAM", "in details");
        final Intent intent = getIntent();
        String jsonString= intent.getStringExtra("jsonValues");
        Log.i("jsonVal", jsonString);
        String degree= intent.getStringExtra("degree");
        TextView detail= (TextView) findViewById(R.id.stateInfo);
        String summaryStr= "More Details for "+intent.getStringExtra("city")+", "+intent.getStringExtra("state");
        detail.setText(summaryStr);
        detail.setTypeface(null,Typeface.BOLD);
        setHourlyData(jsonString, degree);
        setWeeklyData(jsonString, degree);
        final Button buttonHourly = (Button) findViewById(R.id.hourly);
        final Button buttonWeekly = (Button) findViewById(R.id.weekly);
        buttonHourly.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                buttonHourly.setBackgroundResource(R.drawable.buttons);
                buttonWeekly.setBackgroundResource(R.drawable.normalbutton);
                weekly.setVisibility(RelativeLayout.GONE);
                hourly.setVisibility(RelativeLayout.VISIBLE);
            }
        });
        buttonWeekly.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                buttonWeekly.setBackgroundResource(R.drawable.buttons);
                buttonHourly.setBackgroundResource(R.drawable.normalbutton);
                weekly.setVisibility(RelativeLayout.VISIBLE);
                hourly.setVisibility(RelativeLayout.GONE);
            }
        });
    }
    public void setHourlyData(String jsonString, String degree){
        try{
            int[] imageIDs = new int[] {R.id.image1, R.id.image2, R.id.image3,R.id.image4,R.id.image5,R.id.image6,R.id.image7, R.id.image8, R.id.image9, R.id.image10, R.id.image11, R.id.image12, R.id.image13, R.id.image14,R.id.image15, R.id.image16, R.id.image17, R.id.image18, R.id.image19, R.id.image20, R.id.image21, R.id.image22, R.id.image23,R.id.image24,R.id.image25,R.id.image26,R.id.image27,R.id.image28,R.id.image29,R.id.image30,R.id.image31,R.id.image32,R.id.image33,R.id.image34,R.id.image35,R.id.image36,R.id.image37,R.id.image38,R.id.image39,R.id.image40,R.id.image41,R.id.image42,R.id.image43,R.id.image44,R.id.image45,R.id.image46,R.id.image47,R.id.image48,R.id.image49};
            int[] hourIDs = new int[] {R.id.hour1, R.id.hour2, R.id.hour3,R.id.hour4,R.id.hour5,R.id.hour6,R.id.hour7, R.id.hour8, R.id.hour9, R.id.hour10, R.id.hour11, R.id.hour12, R.id.hour13, R.id.hour14,R.id.hour15, R.id.hour16, R.id.hour17, R.id.hour18, R.id.hour19, R.id.hour20, R.id.hour21, R.id.hour22, R.id.hour23,R.id.hour24,R.id.hour25,R.id.hour26,R.id.hour27,R.id.hour28,R.id.hour29,R.id.hour30,R.id.hour31,R.id.hour32,R.id.hour33,R.id.hour34,R.id.hour35,R.id.hour36,R.id.hour37,R.id.hour38,R.id.hour39,R.id.hour40,R.id.hour41,R.id.hour42,R.id.hour43,R.id.hour44,R.id.hour45,R.id.hour46,R.id.hour47,R.id.hour48,R.id.hour49};
            int[] tempIDs = new int[] {R.id.temp1, R.id.temp2, R.id.temp3,R.id.temp4,R.id.temp5,R.id.temp6,R.id.temp7, R.id.temp8, R.id.temp9, R.id.temp10, R.id.temp11, R.id.temp12, R.id.temp13, R.id.temp14,R.id.temp15, R.id.temp16, R.id.temp17, R.id.temp18, R.id.temp19, R.id.temp20, R.id.temp21, R.id.temp22, R.id.temp23,R.id.temp24,R.id.temp25,R.id.temp26,R.id.temp27,R.id.temp28,R.id.temp29,R.id.temp30,R.id.temp31,R.id.temp32,R.id.temp33,R.id.temp34,R.id.temp35,R.id.temp36,R.id.temp37,R.id.temp38,R.id.temp39,R.id.temp40,R.id.temp41,R.id.temp42,R.id.temp43,R.id.temp44,R.id.temp45,R.id.temp46,R.id.temp47,R.id.temp48,R.id.temp49};
            final int[] rowIDs= new int[] {R.id.row1, R.id.row2, R.id.row3,R.id.row4,R.id.row5,R.id.row6,R.id.row7, R.id.row8, R.id.row9, R.id.row10, R.id.row11, R.id.row12, R.id.row13, R.id.row14,R.id.row15, R.id.row16, R.id.row17, R.id.row18, R.id.row19, R.id.row20, R.id.row21, R.id.row22, R.id.row23,R.id.row24,R.id.row25,R.id.row26,R.id.row27,R.id.row28,R.id.row29,R.id.row30,R.id.row31,R.id.row32,R.id.row33,R.id.row34,R.id.row35,R.id.row36,R.id.row37,R.id.row38,R.id.row39,R.id.row40,R.id.row41,R.id.row42,R.id.row43,R.id.row44,R.id.row45,R.id.row46,R.id.row47,R.id.row48,R.id.row49};
            JSONObject jsonObj= new JSONObject(jsonString);
            JSONObject hourlyObj= jsonObj.getJSONObject("hourly");
            JSONArray dataObj= hourlyObj.getJSONArray("data");
            TextView hourlyHeadingTemp= (TextView) findViewById(R.id.hourlyTemp);
            String hourlyHeadStr="";
            if(degree.equalsIgnoreCase("us")){
                hourlyHeadStr= "Temp(°"+"F)";
            }else{
                hourlyHeadStr= "Temp(°"+"C)";
            }
            hourlyHeadingTemp.setText(hourlyHeadStr);
            TextView hourlyTime= (TextView) findViewById(R.id.hourlyTime);
            hourlyTime.setTypeface(null, Typeface.BOLD);
            TextView hourlyTemp= (TextView) findViewById(R.id.hourlyTemp);
            hourlyTemp.setTypeface(null, Typeface.BOLD);
            TextView hourlySummary= (TextView) findViewById(R.id.hourlySummary);
            hourlySummary.setTypeface(null, Typeface.BOLD);
            for (int i=0;i<49;i++){
                ((TableRow)findViewById(rowIDs[i])).setPadding(0,10,0,10);
                JSONObject firstDay= dataObj.getJSONObject(i);
                ImageView summaryIcon= (ImageView)findViewById(imageIDs[i]);
                setImageView(summaryIcon, firstDay.getString("icon"));
                TextView summaryView= (TextView) findViewById(hourIDs[i]);
                String time=firstDay.getString("time");
                String timeStr=getFormattedTime(time, jsonObj.getString("timezone"));
                summaryView.setText(timeStr);
                summaryView.setTypeface(null, Typeface.BOLD);
                TextView tempView= (TextView) findViewById(tempIDs[i]);
                String tempStr= firstDay.getString("temperature");
                tempView.setText(tempStr);
                if((i%2)==0){
                    ((TableRow)findViewById(rowIDs[i])).setBackgroundColor(0xffcccccc);
                }
                if(i>24){
                    TableRow row= (TableRow)findViewById(rowIDs[i]);
                    row.setVisibility(View.GONE);
                }
            }
            final ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    imageView.setVisibility(View.GONE);
                    for(int i=24;i<49;i++){
                        TableRow row= (TableRow)findViewById(rowIDs[i]);
                        row.setVisibility(View.VISIBLE);
                    }
                }
            });

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void setWeeklyData(String json, String degree){
        try{
            int[] maxIDs = new int[] {R.id.max1, R.id.max2, R.id.max3,R.id.max4,R.id.max5,R.id.max6,R.id.max7};
            int[] iconIDs = new int[] {R.id.icon1, R.id.icon2, R.id.icon3,R.id.icon4,R.id.icon5,R.id.icon6,R.id.icon7};
            int[] dateIDs = new int[] {R.id.date1, R.id.date2, R.id.date3,R.id.date4,R.id.date5,R.id.date6,R.id.date7};
            JSONObject jsonObj= new JSONObject(json);
            JSONObject weeklyObj= jsonObj.getJSONObject("daily");
            JSONArray dataObj= weeklyObj.getJSONArray("data");
            for(int i=1;i<=7;i++){
                JSONObject weekDay=dataObj.getJSONObject(i);
                TextView weekDate= (TextView) findViewById(dateIDs[i-1]);
                String dateStr= getFormattedDate(weekDay.getString("time"),jsonObj.getString("timezone"));
                weekDate.setText(dateStr);
                weekDate.setTypeface(null, Typeface.BOLD);
                TextView weekTemp= (TextView) findViewById(maxIDs[i-1]);
                String tempMinStr= weekDay.getString("temperatureMin");
                String tempMaxStr= weekDay.getString("temperatureMax");
                String temp="Min: "+getFormatedTemp(degree, tempMinStr)+"|Max: "+getFormatedTemp(degree,tempMaxStr);
                weekTemp.setText(temp);
                ImageView summaryIcon= (ImageView)findViewById(iconIDs[i-1]);
                setImageView(summaryIcon, weekDay.getString("icon"));
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    String getFormattedTime(String time,String timezone){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        long timelong=Integer.parseInt(time);
        Date sunriseDate=new java.util.Date((long)timelong*1000);
        String timeStr=dateFormat.format(sunriseDate);
        return timeStr;
    }
    String getFormattedDate(String time,String timezone){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        dateFormat.applyPattern("EEEE, MMM d");
        long timeLong=Integer.parseInt(time);
        Date timeDate=new java.util.Date((long)timeLong*1000);
        String timeStr=dateFormat.format(timeDate);
        return timeStr;
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

    public  String getFormatedTemp(String degree, String temp){
        int value;
        String result;
        if(degree.equalsIgnoreCase("us")){
            value=Math.round(Float.parseFloat(temp));
            result= value+"° "+"F";
        }else{
            value=Math.round(Float.parseFloat(temp));
            result= value+"° "+"C";
        }
        return result;
    }
    String getStateCode(String state){
        String stateCode="";

        return stateCode;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
}
