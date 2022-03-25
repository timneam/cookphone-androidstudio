package sg.edu.tp.cookphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartingPage extends AppCompatActivity {

    //Displays the starting splash screen for 1 second
    private static int starting_page = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page); //finding the layout using id
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goLogin = new Intent(StartingPage.this, LoginActivity.class);
                //after 1 second, the screen will move from the starting page to the next screen which is the login activity
                startActivity(goLogin);
                finish();
            }
        },starting_page);
    }
}

