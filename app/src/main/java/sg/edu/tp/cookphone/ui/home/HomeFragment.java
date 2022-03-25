package sg.edu.tp.cookphone.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import sg.edu.tp.cookphone.R;
import sg.edu.tp.cookphone.new_user_recipe;
import sg.edu.tp.cookphone.selected_recipe;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ImageButton addRecipeButton;
    private SearchView searchrecipe;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final ListView recipe_list = (ListView)view.findViewById(R.id.recipe_list);

        ImageButton addRecipeButton = (ImageButton)view.findViewById(R.id.addRecipeButton);




        //display recipes
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<String> blind_list = new ArrayList<String>();
        mDatabase.collection("User Recipes").document(userid).collection("user recipe list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> list_of_recipes = new ArrayList<String>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //data that is going to be transfer when the recipe is chosen
                                blind_list.add(document.getId()
                                        + "#" + document.getData().get("recipe_name").toString()
                                        + "#" + document.getData().get("duration").toString()
                                        + "#" + document.getData().get("ingredients").toString()
                                        + "#"  + document.getData().get("steps").toString()
                                        + "#" + document.getData().get("nutritionalvalues").toString());

                                list_of_recipes.add("        " + document.getData().get("recipe_name").toString() + "                                        "+ document.getData().get("duration").toString());
                            }
                            ArrayAdapter adapter = new ArrayAdapter(recipe_list.getContext(), android.R.layout.simple_list_item_1, list_of_recipes);
                            recipe_list.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Error getting recipes", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        //Making the ingredients clickable where it brings users to another page to update their quantity or delete the ingredient
        recipe_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recipe_content = String.valueOf(parent.getItemAtPosition(position));
                String list2 = String.valueOf(blind_list.get(position));
                Intent detailsIntent = new Intent(view.getContext(), selected_recipe.class);
                detailsIntent.putExtra("recipe_name", list2);
                startActivity(detailsIntent);
            }
        });

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getActivity(), new_user_recipe.class);
                startActivity(go);
            }
        });










        //Search function
        searchrecipe = (SearchView)view.findViewById(R.id.search_my_recipes);
        searchrecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchrecipe.getQuery().length() != 0){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final ArrayList<String> searchResults = new ArrayList<String>();
                    CollectionReference searchCollection =  db.collection("User Recipes").document(user_id).collection("user recipe list");

                    Query searchQuery = searchCollection.whereEqualTo("recipe_name", searchrecipe.getQuery().toString());

                    searchQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    searchResults.add(document.getData().get("recipe_name").toString() + "                       " + document.getData().get("duration").toString());
                                    blind_list.removeAll(blind_list);
                                    blind_list.add(document.getId()
                                            + "#" + document.getData().get("recipe_name").toString()
                                            + "#" + document.getData().get("duration").toString()
                                            + "#" + document.getData().get("ingredients").toString()
                                            + "#"  + document.getData().get("steps").toString()
                                            + "#" + document.getData().get("nutritionalvalues").toString());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(recipe_list.getContext(), android.R.layout.simple_list_item_1, searchResults);
                                recipe_list.setAdapter(adapter);
                            } else
                            {
                                Toast.makeText(getActivity().getBaseContext(), "Error getting documents",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    //Search for duration
                    Query searchQuery2 = searchCollection.whereEqualTo("duration", searchrecipe.getQuery().toString());

                    searchQuery2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    searchResults.add(document.getData().get("recipe_name").toString() + "                       " + document.getData().get("duration").toString());
                                    blind_list.removeAll(blind_list);
                                    blind_list.add(document.getId()
                                            + "#" + document.getData().get("recipe_name").toString()
                                            + "#" + document.getData().get("duration").toString()
                                            + "#" + document.getData().get("ingredients").toString()
                                            + "#"  + document.getData().get("steps").toString()
                                            + "#" + document.getData().get("nutritionalvalues").toString());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(recipe_list.getContext(), android.R.layout.simple_list_item_1, searchResults);
                                recipe_list.setAdapter(adapter);
                            } else
                            {
                                Toast.makeText(getActivity().getBaseContext(), "Error getting documents",
                                        Toast.LENGTH_LONG).show();
                            }


                        }
                    });


















                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });











        return view;
    }
}
