package sg.edu.tp.cookphone;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_explore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_explore extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView explore_recipe_list;
    private SearchView searchNewRecipe;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_explore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_explore.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_explore newInstance(String param1, String param2) {
        fragment_explore fragment = new fragment_explore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        explore_recipe_list = (ListView)view.findViewById(R.id.new_creative_recipes);

        // display new creative recipes
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<String> creative_recipe_blind_list = new ArrayList<String>();
        mDatabase.collection("Recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> list_of_new_recipes = new ArrayList<String>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //data that is going to be transfer when the recipe is chosen

                                creative_recipe_blind_list.add(document.getId()
                                        + "#" + document.getData().get("Name").toString()
                                        + "#" + document.getData().get("Duration").toString()
                                        + "#" + document.getData().get("Ingredients").toString()
                                        + "#"  + document.getData().get("Steps").toString()
                                        + "#" + document.getData().get("nv").toString());

                                list_of_new_recipes.add("        " + document.getData().get("Name").toString() + "        " + document.getData().get("Duration").toString());
                            }
                            ArrayAdapter adapter = new ArrayAdapter(explore_recipe_list.getContext(), android.R.layout.simple_list_item_1, list_of_new_recipes);
                            explore_recipe_list.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Error getting recipes", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        //Making the ingredients clickable where it brings users to another page to update their quantity or delete the ingredient
        explore_recipe_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recipe_content = String.valueOf(parent.getItemAtPosition(position));
                String list2 = String.valueOf(creative_recipe_blind_list.get(position));
                Intent detailsIntent = new Intent(view.getContext(), new_selected_recipe.class);
                detailsIntent.putExtra("Name", list2);
                startActivity(detailsIntent);
            }
        });



        //Search function
        searchNewRecipe = (SearchView)view.findViewById(R.id.search_new_recipe);
        searchNewRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (searchNewRecipe.getQuery().length() != 0){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final ArrayList<String> searchResults = new ArrayList<String>();
                    CollectionReference searchCollection =  db.collection("Recipes");

                    //Search for recipe name
                    Query searchQuery1 = searchCollection.whereEqualTo("Name", searchNewRecipe.getQuery().toString());

                    searchQuery1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    searchResults.add(document.getData().get("Name").toString() + "                       " + document.getData().get("Duration").toString());
                                    creative_recipe_blind_list.removeAll(creative_recipe_blind_list);
                                    creative_recipe_blind_list.add(document.getId()
                                            + "#" + document.getData().get("Name").toString()
                                            + "#" + document.getData().get("Duration").toString()
                                            + "#" + document.getData().get("Ingredients").toString()
                                            + "#"  + document.getData().get("Steps").toString()
                                            + "#" + document.getData().get("nv").toString());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(explore_recipe_list.getContext(), android.R.layout.simple_list_item_1, searchResults);
                                explore_recipe_list.setAdapter(adapter);
                            } else
                            {
                                Toast.makeText(getActivity().getBaseContext(), "Error getting documents",
                                        Toast.LENGTH_LONG).show();
                            }


                        }
                    });


                    //Search for duration
                    Query searchQuery2 = searchCollection.whereEqualTo("Duration", searchNewRecipe.getQuery().toString());

                    searchQuery2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    searchResults.add(document.getData().get("Name").toString() + "                       " + document.getData().get("Duration").toString());
                                    creative_recipe_blind_list.removeAll(creative_recipe_blind_list);
                                    creative_recipe_blind_list.add(document.getId()
                                            + "#" + document.getData().get("Name").toString()
                                            + "#" + document.getData().get("Duration").toString()
                                            + "#" + document.getData().get("Ingredients").toString()
                                            + "#"  + document.getData().get("Steps").toString()
                                            + "#" + document.getData().get("nv").toString());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(explore_recipe_list.getContext(), android.R.layout.simple_list_item_1, searchResults);
                                explore_recipe_list.setAdapter(adapter);
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
