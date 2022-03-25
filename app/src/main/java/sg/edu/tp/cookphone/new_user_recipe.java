package sg.edu.tp.cookphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class new_user_recipe extends AppCompatActivity {

    Button add;
    //vars
    private INew_recipe mINew_recipe;
    private ArrayAdapter adapter;
    EditText recipe_name,duration,ingredients,steps,nutritionalvalues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_recipe);

        recipe_name = (EditText) findViewById(R.id.name_for_recipe);
        duration = (EditText) findViewById(R.id.duration_for_recipe);
        ingredients = (EditText) findViewById(R.id.ingredients_for_recipe);
        steps = (EditText) findViewById(R.id.steps_for_recipe);
        nutritionalvalues = (EditText) findViewById(R.id.nv_for_recipe);

        add = (Button) findViewById(R.id.add_recipe);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = recipe_name.getText().toString();
                String name2 = duration.getText().toString();
                String name3 = ingredients.getText().toString();
                String name4 = steps.getText().toString();
                String name5 = nutritionalvalues.getText().toString();

                addNewRecipe(name1, name2 , name3 , name4 , name5);
            }
        });
    }

    //Add a new recipe function
    public void addNewRecipe(String recipe_name, String duration, String ingredients, String steps, String nutritionalvalues) {
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
                    Toast.makeText(getBaseContext(), "Added reipce to list", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "Failure to add recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startActivity(new Intent(getBaseContext(), BottomNavActivity.class));
    }








}
