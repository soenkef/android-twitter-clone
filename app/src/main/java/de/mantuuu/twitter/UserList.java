package de.mantuuu.twitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ParseUser.getCurrentUser().get("isFollowing") == null ) {

            List<String> emptyList = new ArrayList<String>();
            ParseUser.getCurrentUser().put("isFollowing", emptyList);

        }

        final ArrayList users = new ArrayList();
        users.add("Soenke");
        users.add("Hugo");

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);

        final ListView listView = (ListView) findViewById(R.id.tweetsListView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()) {
                    Log.i("AppInfo", "Zeile markiert!");

                    ParseUser.getCurrentUser().getList("isFollowing").add(users.get(position));
                    List newList = (List) ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", newList);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                Log.i("AppInfo", "Liste geändert, User hinzugefügt");
                            }

                        }
                    });

                } else {
                    Log.i("AppInfo", "Zeile nicht markiert");
                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(position));
                    List newList = (List) ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", newList);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                Log.i("AppInfo", "Liste geändert, User entfernt");
                            }

                        }
                    });
                }

            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null) {
                    users.clear();

                    for (ParseUser user : objects) {
                        users.add(user.getUsername());
                    }

                    arrayAdapter.notifyDataSetChanged();

                    for (Object username : users) {

                        if (ParseUser.getCurrentUser().getList("isFollowing").contains(username)); {
                            listView.setItemChecked(users.indexOf(username), true);
                        }

                    }

                } else {
                    // hat nicht funktioniert
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.exit) {
            logOut(null);
        }

        if (id == R.id.feed) {

            Intent i = new Intent(getApplicationContext(), ViewFeed.class);
            startActivity(i);

        }

        if (id == R.id.tweet) {

            // Dialogfeld mit Texteingabe
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sende einen Tweet");
            final EditText tweetContent = new EditText(this);
            builder.setView(tweetContent);

            // Senden
            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("AppInfo", String.valueOf(tweetContent.getText()));

                    ParseObject tweet = new ParseObject("Tweet");
                    tweet.put("username", ParseUser.getCurrentUser().getUsername());
                    tweet.put("tweet", String.valueOf(tweetContent.getText()));
                    tweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "Dein Tweet wurde gespeichert", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Dein Tweet konnte nicht gespeichert werden, versuche es später noch einmal", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
            });

            // Abbrechen
            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Fenster anzeigen
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Logout-Funktion
    public void logOut(View view) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Logout", "Logout erfolgreich.");
                    Toast.makeText(getApplicationContext(), "Logout erfolgreich!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    Log.i("Logout", "Logout nicht erfolgreich.");
                    Toast.makeText(getApplicationContext(), "Logout nicht erfolgreich!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}