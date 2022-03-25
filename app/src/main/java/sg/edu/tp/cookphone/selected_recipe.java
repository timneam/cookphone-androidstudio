package sg.edu.tp.cookphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class selected_recipe extends AppCompatActivity {

    TextView name_of_recipe, duration_of_recipe, ingredients_of_recipe, steps_of_recipe, nv_of_recipe;
    String recipeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_recipe);

        //Declaring the Java variables to the UI id
        name_of_recipe = (TextView) findViewById(R.id.recipe_content_title);
        duration_of_recipe = (TextView) findViewById(R.id.recipe_content_duration);
        ingredients_of_recipe = (TextView) findViewById(R.id.recipe_content_ingredients);
        steps_of_recipe = (TextView) findViewById(R.id.recipe_content_steps);
        nv_of_recipe = (TextView) findViewById(R.id.recipe_content_nv);

        ingredients_of_recipe.setMovementMethod(new ScrollingMovementMethod());
        steps_of_recipe.setMovementMethod(new ScrollingMovementMethod());
        nv_of_recipe.setMovementMethod(new ScrollingMovementMethod());

        //For every #, split the data to assign them to their own textView
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String[] temp = b.getString("recipe_name").split("#");

        //To split the sentences in the steps text
        String steps = temp[4];
        String steps2 = steps.replaceAll("\\\\n", "\n\n");

        //To split the sentences in the ingredients text
        String ingredients = temp[3];
        String ingredients2 = ingredients.replaceAll("\\\\n", "\n\n");

        //To split the sentences in the nutritional values text
        String nv = temp[5];
        String nv2 = nv.replaceAll("\\\\n", "\n\n");

        //the variables are in the array where ingredientid is in the position 0 of the array
        //name_of_ingredient is in the array position 1
        // quantity is in the array position 2
        recipeid = (String) temp[0];
        name_of_recipe.setText(temp[1]);
        duration_of_recipe.setText(temp[2]);
        ingredients_of_recipe.setText(ingredients2);
        steps_of_recipe.setText(steps2);
        nv_of_recipe.setText(nv2);
    }

    //Delete recipe
    public void deleteRecipe (View v) {
        // To delete data from the Firebase
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String id = recipeid;
        mDatabase.collection("User Recipes").document(userid).collection("user recipe list").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getBaseContext(), "Recipe deleted ", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Error deleting from DB", Toast.LENGTH_LONG).show();
                    }
                });
        // bring user to previous Activity
        startActivity(new Intent(getBaseContext(), BottomNavActivity.class));
    }
}
