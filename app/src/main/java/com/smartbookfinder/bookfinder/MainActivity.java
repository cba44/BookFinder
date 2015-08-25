package com.smartbookfinder.bookfinder;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends ActionBarActivity implements LocationListener {

    public static Location dest;
    TextView longLatView;
    LocationManager lm;
    String provider;
    Location l;
    Double lng,lat;
    float minDist = Float.MAX_VALUE;
    String minBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "cDjCcIaSJQxY7pQY2tdeP6odtYqHhZLmonAzxTGn", "95wtQir31qYjawYRSCS9PlHrXVLcisLmtxk5yL7H");

        Button btn = (Button)findViewById(R.id.button);

        Button mapShow = (Button)findViewById(R.id.button2);

        final EditText edt = (EditText)findViewById(R.id.editText);

        final TextView txtView=(TextView)findViewById(R.id.textView2);

        longLatView=(TextView)findViewById(R.id.textView3);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");

        lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c=new Criteria();
        provider=lm.getBestProvider(c, false);

        l=lm.getLastKnownLocation(provider);
        if(l!=null)
        {
            //get latitude and longitude of the location
            lng=l.getLongitude();
            lat=l.getLatitude();
            //display on text view
            //longLatView.setText(lat + " " + lng);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String bookName = edt.getText().toString();
                query.whereEqualTo("BookName", bookName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> bookList, ParseException e) {

                        if (e == null) {
                            Log.d("score", "Retrieved " + bookList.size() + " items");
                            String str = "Retrieved " + bookList.size() + " items\n";
                            for (int i = 0 ; i < bookList.size() ; i++){
                                str = str + bookList.get(i).get("BookShop")+ "\n";

                                float longi = Float.parseFloat((String)bookList.get(i).get("Longitude"));
                                float lati = Float.parseFloat((String)bookList.get(i).get("Latitude"));

                                Location targetLocation = new Location("");
                                targetLocation.setLatitude(lati);
                                targetLocation.setLongitude(longi);

                                float d = l.distanceTo(targetLocation);

                                if(d < minDist){
                                    minDist = d;
                                    minBook = (String)bookList.get(i).get("BookShop");
                                    dest = targetLocation;
                                }

                            }
                            if(bookList.size() > 0) str = str + "\nNearest book shop with " + bookName + " is " + minBook;
                            minDist = Float.MAX_VALUE;
                            txtView.setText(str);
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                            txtView.setText("Error: " + e.getMessage());
                        }
                    }
                });
            }
        });

        mapShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onLocationChanged(Location loc) {
        lat = loc.getLatitude();
        lng = loc.getLongitude();

        longLatView.setText(lat + " " + lng);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}