package sg.edu.tp.cookphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import sg.edu.tp.cookphone.ui.home.HomeFragment;

public class Selected_Ingredient extends AppCompatActivity {

    //Declaring the UI and the java varaibles
    TextView name_of_ingredient, quantity;
    String ingredientid;
    EditText new_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected__ingredient);

        //Declaring the Java variables to the UI id
        name_of_ingredient = (TextView) findViewById(R.id.ingredient);
        quantity = (TextView) findViewById(R.id.quantity);
        new_quantity = (EditText) findViewById(R.id.new_quantity);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        String[] temp = b.getString("Ingredient").split(",");

        //the variables are in the array where ingredientid is in the position 0 of the array
        //name_of_ingredient is in the array position 1
        // quantity is in the array position 2
        ingredientid = (String) temp[0];
        name_of_ingredient.setText(temp[1]);
        quantity.setText(temp[2]);
    }

    //Updating the quantity of the selected ingredient chosen
    public void updateQuantity (View v) {
        // check that there is value in the new updated quantity
        //if the new quantity is not 0
        if (new_quantity.getText().toString().length() !=0 && (!new_quantity.getText().toString().equals("0")))
        {
            // To read values from the Firebase
            Map<String, Object> update_ingredient = new HashMap<>();
            update_ingredient.put("ingredient", name_of_ingredient.getText().toString());
            update_ingredient.put("quantity", new_quantity.getText().toString());
            // To update data to the Firebase
            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String ingredient_id = ingredientid;
            mDatabase.collection("Ingredients").document(userid).collection("ingredient list").document(ingredient_id).update(update_ingredient)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Details updated: ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Error updating to DB", Toast.LENGTH_LONG).show();
                        }
                    });
            startActivity(new Intent(getBaseContext(), BottomNavActivity.class)); // bring user to previous Activity
        }
        else if (new_quantity.getText().toString().equals("0")) //if the new quantity is 0, delete the ingredient
            {
            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String ingredient_id = ingredientid;
                mDatabase.collection("Ingredients").document(userid).collection("ingredient list").document(ingredient_id).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Ingredient deleted: ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Error deleting from DB", Toast.LENGTH_LONG).show();
                        }
                    });
            // bring user to home page
            startActivity(new Intent(getBaseContext(), BottomNavActivity.class));
        }
        }
    }
