package sg.edu.tp.cookphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    //Declaring the firebase firestore variables
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    //Declaring the UI in the sign up xml
    EditText email;
    EditText password;
    EditText confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Connecting the java variables to the UI in the xml
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmPassword);

    }

    //Codes for the register function
    public void clickSignup(View v) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userConfirmPassword = confirmpassword.getText().toString();

        //if both the password and confirm password does not match
        if (!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(this, "The passwords do not match, please re-type again", Toast.LENGTH_SHORT).show();
        }

        //If the length of the email and password is not 0
        else if (userEmail.length() != 0 && userPassword.length() != 0) {
            // Signup an account using email and password from EditTexts
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success
                                Toast.makeText(getBaseContext(), "Sign Up Successful.", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(getBaseContext(), "Sign Up failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: Email and Password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}