package sg.edu.tp.cookphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class forgot_password_page extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    //Declaring the UI component
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        //COnnecting the java variables to the UI id
        email = (EditText) findViewById(R.id.forgot_email);
    }

    //Reset password function
    public void sendEmail(View v) {
        String userEmail = email.getText().toString();

        if (userEmail.length() != 0) {
            mAuth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Email send success
                                Toast.makeText(getBaseContext(), "Email sent successfully.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Email send fails
                                Toast.makeText(getBaseContext(), "Error sending email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "ERROR: Email cannot be empty.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
