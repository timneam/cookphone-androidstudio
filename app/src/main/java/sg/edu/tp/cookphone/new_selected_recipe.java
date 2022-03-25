package sg.edu.tp.cookphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class new_selected_recipe extends AppCompatActivity {

    TextView new_name_of_recipe, new_duration_of_recipe, new_ingredients_of_recipe, new_steps_of_recipe, new_nv_of_recipe;
    String newrecipeid;
    Button bookmark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_selected_recipe);


        //Declaring the Java variables to the UI id
        new_name_of_recipe = (TextView) findViewById(R.id.creative_recipe_title);
        new_duration_of_recipe = (TextView) findViewById(R.id.creative_recipe_duration);
        new_ingredients_of_recipe = (TextView) findViewById(R.id.creative_recipe_ingredients);
        new_steps_of_recipe = (TextView) findViewById(R.id.creative_recipe_steps);
        new_nv_of_recipe = (TextView) findViewById(R.id.creative_recipe_nv);

        new_ingredients_of_recipe.setMovementMethod(new ScrollingMovementMethod());
        new_steps_of_recipe.setMovementMethod(new ScrollingMovementMethod());
        new_nv_of_recipe.setMovementMethod(new ScrollingMovementMethod());

        bookmark = (Button) findViewById(R.id.bookmark);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        final String[] temp = b.getString("Name").split("#");

        //To split the sentences in steps text
        String steps = temp[4];
        String steps2 = steps.replaceAll("\\\\n", "\n\n");

        //To split the sentences in ingredients text
        String ingredients = temp[3];
        String ingredients2 = ingredients.replaceAll("\\\\n", "\n\n");

        //To split the sentences in nutritional values text
        String nv = temp[5];
        String nv2 = nv.replaceAll("\\\\n", "\n\n");

        //the variables are in the array where ingredientid is in the position 0 of the array
        //name_of_ingredient is in the array position 1
        // quantity is in the array position 2
        newrecipeid = (String) temp[0];
        new_name_of_recipe.setText(temp[1]);
        new_duration_of_recipe.setText(temp[2]);
        new_ingredients_of_recipe.setText(ingredients2);
        new_steps_of_recipe.setText(steps2);
        new_nv_of_recipe.setText(nv2);

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkRecipe((temp[1]), (temp[2]), (temp[3]), (temp[4]), (temp[5]));
            }
        });


    }




    public void bookmarkRecipe(String recipe_name, String duration, String ingredients, String steps, String nutritionalvalues) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference recipeRef = db.collection("User Recipes").document(userid).collection("user recipe list").document();

        Recipe_database new_recipe = new Recipe_database();
        new_recipe.setRecipe_name(recipe_name);
        new_recipe.setDuration(duration);
        new_recipe.setIngredients(ingredients);
        new_recipe.setSteps(steps);
        new_recipe.setNutritionalvalues(nutritionalvalues);

        recipeRef.set(new_recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Added recipe to list", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "Failure to add recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startActivity(new Intent(getBaseContext(), BottomNavActivity.class));
    }




}
