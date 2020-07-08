package de.mantuuu.twitter;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;

    public void showUserList() {

        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);

    }

    public void loginOrSignup(View view) {

        ParseUser.logInInBackground(String.valueOf(usernameEditText.getText()), String.valueOf(passwordEditText.getText()), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null) {
                    // Super, Der Benutzer ist eingeloggt
                    Log.i("AppInfo", "Eingeloggt");
                    showUserList();

                } else {
                    //einloggen hat nicht funktioniert
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(String.valueOf(usernameEditText.getText()));
                    newUser.setPassword(String.valueOf(passwordEditText.getText()));

                    newUser.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {

                            if (e == null) {
                                Log.i("AppInfo", "Registriert");
                                showUserList();
                            } else {

                                Toast.makeText(getApplicationContext(), "Einloggen oder Registrierung hat nicht funktioniert - versuche es noch einmal!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        // Checkt, ob User existiert
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            showUserList();
        }

        // Testobjekt
        /*ParseObject testObjekt = new ParseObject("testObjekt");
        testObjekt.put("test", 2000);
        testObjekt.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Parse", "Save Succeeded");
                } else {
                    Log.i("Parse", "Save Failed");
                }
            }
        });*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

  /*  @Override
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
    }*/
}