package sg.edu.tp.cookphone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ListViewAutoScrollHelper;
import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_ingredients#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_ingredients extends Fragment implements IFragment_ingredient {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText name_ingredient;
    private EditText quantity_ingredient;
    private ImageButton add;
    private ListView ingredient_list;
    private SearchView search;

    //vars
    private IFragment_ingredient mIFragment_ingredient;
    private ArrayAdapter adapter;

    public fragment_ingredients() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_ingredients.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_ingredients newInstance(String param1, String param2) {
        fragment_ingredients fragment = new fragment_ingredients();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        //Connecting java variables to UI id
        //Displaying list view and to add ingredients
        name_ingredient = (EditText) view.findViewById(R.id.ingredient_name);
        quantity_ingredient = (EditText) view.findViewById(R.id.ingredient_quantity);
        add = (ImageButton) view.findViewById(R.id.addIngredientButton);

        final ListView ingredient_list = (ListView)view.findViewById(R.id.ingredient_list);


        //Function to display all the ingredients for a user based on their user id
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<String> list = new ArrayList<String>();
        mDatabase.collection("Ingredients").document(userid).collection("ingredient list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> list_of_ingredients = new ArrayList<String>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                list.add(document.getId() + "," + document.getData().get("ingredient").toString()
                                        + "," + document.getData().get("quantity").toString());

                                list_of_ingredients.add("        " + document.getData().get("ingredient").toString() + "                                        "+ document.getData().get("quantity").toString());
                            }
                            ArrayAdapter adapter = new ArrayAdapter(ingredient_list.getContext(), android.R.layout.simple_list_item_1, list_of_ingredients);
                            ingredient_list.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Error getting ingredients", Toast.LENGTH_LONG).show();
                        }
                    }
                });






        //Making the ingredients clickable where it brings users to another page to update their quantity or delete the ingredient
        ingredient_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedIngredient= String.valueOf(parent.getItemAtPosition(position));
                String list2 = String.valueOf(list.get(position));
                Intent detailsIntent = new Intent(view.getContext(), Selected_Ingredient.class);
                detailsIntent.putExtra("Ingredient", list2);
                startActivity(detailsIntent);
            }
        });


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name1 = name_ingredient.getText().toString();
                        int quantity1 = Integer.parseInt(quantity_ingredient.getText().toString());

                        addNewIngredient(name1, quantity1);
                    }
                });
















        //Search function
        search = (SearchView)view.findViewById(R.id.search_ingredients);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (search.getQuery().length() != 0){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final ArrayList<String> searchResults = new ArrayList<String>();
                    CollectionReference searchCollection =  db.collection("Ingredients").document(user_id).collection("ingredient list");

                    Query searchQuery = searchCollection.whereEqualTo("ingredient", search.getQuery().toString());

                    searchQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    searchResults.add(document.getData().get("ingredient").toString() + "                       " + document.getData().get("quantity").toString());
                                    list.removeAll(list);
                                    list.add(document.getId() + "," + document.getData().get("ingredient").toString() + "," + document.getData().get("quantity").toString());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(ingredient_list.getContext(), android.R.layout.simple_list_item_1, searchResults);
                                ingredient_list.setAdapter(adapter);
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize your view here for use view.findViewById("your view id")
    }


    //Add a new ingredient function
    @Override
    public void addNewIngredient(String ingredient, int quantity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference ingredientRef = db.collection("Ingredients").document(userid).collection("ingredient list").document();

        Ingredients_database new_ingredient = new Ingredients_database();
        new_ingredient.setIngredient(ingredient);
        new_ingredient.setQuantity(quantity);

        ingredientRef.set(new_ingredient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity().getBaseContext(), "Added ingredient to list", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity().getBaseContext(), "Failure to add ingredient", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
