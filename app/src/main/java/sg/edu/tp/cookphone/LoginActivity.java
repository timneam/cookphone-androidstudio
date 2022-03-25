package sg.edu.tp.cookphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private GoogleSignInClient mGoogleSignInClient;

    //Declaring the UI components and the java variables
    EditText email;
    EditText password;

    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            Intent intent = new Intent(getApplicationContext(), BottomNavActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Connecting the java variables to the UI components using the find view by id code
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
    }

    //User login function
    public void clickLogin(View v) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (userEmail.length() != 0 && userPassword.length()!= 0) {
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success
                                Toast.makeText(getBaseContext(), "Login Successful.",
                                        Toast.LENGTH_SHORT).show();
                                // Bring user to success activvity
                                Intent successActivity = new Intent(getBaseContext(), BottomNavActivity.class);
                                startActivity(successActivity);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getBaseContext(), "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "ERROR: Email and Password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }

    //When clicked, it brings you to the forgot password page
    public void forgotPasswordPage(View v) {
        // Directs the user to the ForgotPassword activity
        Intent forgotActivity = new Intent(getApplicationContext(), forgot_password_page.class);
        startActivity(forgotActivity);
    }

    //When clicked, it brings you to the sign up page
    public void goToSignUpPage (View v) {
        //Brings the user to the sign up page
        Intent goToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(goToSignUp);
    }
}
