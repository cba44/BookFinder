package com.smartbookfinder.bookfinder;

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


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "cDjCcIaSJQxY7pQY2tdeP6odtYqHhZLmonAzxTGn", "95wtQir31qYjawYRSCS9PlHrXVLcisLmtxk5yL7H");

        Button btn = (Button)findViewById(R.id.button);
        final EditText edt = (EditText)findViewById(R.id.editText);

        final TextView txtView=(TextView)findViewById(R.id.textView2);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");


        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String bookName = edt.getText().toString();
                query.whereEqualTo("BookName", bookName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> bookList, ParseException e) {

                        if (e == null) {
                            Log.d("score", "Retrieved " + bookList.size() + " items");
                            String str = "Retrieved " + bookList.size() + " items\n";
                            for (int i = 0 ; i < bookList.size() ; i++){
                                str = str + bookList.get(i).get("BookName") + " - " + bookList.get(i).get("BookShop") + "\n";
                            }
                            txtView.setText(str);
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                            txtView.setText("Error: " + e.getMessage());
                        }
                    }
                });
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
}