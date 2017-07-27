package yonifre.com.crimeloggerandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.d("A", "running");

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_log_crime);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Log Crime", Snackbar.LENGTH_LONG)
                        .setAction("Log Crime", null)
                        .show();
            }
        });
        TextView textLoginName = (TextView)findViewById(R.id.textLoginName);
        textLoginName.setText("Not logged in");
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    firebaseUser = user;

                    Log.d("MainActivity", "USER AUTHENTICATED: " + user.getDisplayName());
                    TextView textLoginName = (TextView) findViewById(R.id.textLoginName);
                    textLoginName.setText("Logged in user: " + user.getDisplayName());
                    textLoginName.refreshDrawableState();
                    textLoginName.invalidate();
                    Task<GetTokenResult> task = user.getIdToken(false);
                    String idToken = "no value";

                    task.addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                // Task completed successfully
                                GetTokenResult result = task.getResult();

                                Log.d("MainActivity", "Token: " + result.getToken());

                            } else {
                                // Task failed with an exception
                                Exception exception = task.getException();
                            }
                        }
                    });

                } else {
                    Log.d("MainActivity", "USER NOT LOGGED IN");
                    TextView textLoginName = (TextView) findViewById(R.id.textLoginName);
                    textLoginName.setText("NOT LOGGED IN");
                    textLoginName.invalidate();
                    textLoginName.refreshDrawableState();
                    textLoginName.invalidate();
                }
            }});
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
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        //auth.signOut();

        //setContentView(R.layout.activity_main);
        // Check if user is signed in (non-null) and update UI accordingly.

/*
        Log.d("MainActivity", "USER AUTHENTICATED: "+auth.getCurrentUser().getDisplayName());
        Task<GetTokenResult> task = user.getIdToken(false);
        try {
            task.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Log.d("E", e.getStackTrace());
        }
        String idToken = task.getResult().getToken();

        Log.d("MainActivity", "ID TOKEN = "+idToken);

        //updateUI(currentUser);
        */
    }

    public void logOut(MenuItem item) {

        Log.d("MainActivity", "button clicked");
        //setContentView(R.layout.activity_main);
        auth.signOut();
    }

    public void login(MenuItem item) {

        Log.d("MainActivity", "button clicked");
        auth = FirebaseAuth.getInstance();


        Log.d("MainActivity", "LOGGING IN");
        //auth.signOut();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
        // already signed in



       // setContentView(R.layout.activity_maps);

    }

    public void showMap(MenuItem item) {
        //setContentView(R.layout.activity_maps);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {



                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                   // showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    //showSnackbar(R.string.unknown_error);
                    return;
                }
            }

           // showSnackbar(R.string.unknown_sign_in_response);
        }
    }
}
