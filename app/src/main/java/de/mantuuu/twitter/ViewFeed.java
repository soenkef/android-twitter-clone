package de.mantuuu.twitter;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewFeed extends AppCompatActivity {

    ListView listView;
    List<Map<String,String>> tweetData;
    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Zurück-Button einbauen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.tweetsListView);

        // Tweets bzw. ListView soll zwei Strings enthalten: Den Tweet und den dazugehörigen Tweeter
        tweetData = new ArrayList<Map<String,String>>();

        // Anstatt arrayadapter - simpleAdapter nimmt zwei Einträge entgegen (Für Tweet und Tweeter)
        simpleAdapter = new SimpleAdapter(this, tweetData, android.R.layout.simple_list_item_2, new String[] {"content", "username"}, new int[] {android.R.id.text1, android.R.id.text2});

        // Holt Einträge aus Parse-Datenbank
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> tweetObjects, ParseException e) {

                if (e == null) {

                    if (tweetObjects.size() > 0) {
                        for (ParseObject tweetObject : tweetObjects) {

                            // Fügt Einträge in die Liste hinzu
                            Map<String,String> tweet = new HashMap<String,String>(2);
                            tweet.put("content", tweetObject.getString("tweet"));
                            tweet.put("username", tweetObject.getString("username"));
                            tweetData.add(tweet);

                        }

                        // setzt den Adapter zur ListView
                        listView.setAdapter(simpleAdapter);
                    }

                }

            }
        });




    }
}