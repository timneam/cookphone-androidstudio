package sg.edu.tp.cookphone;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_calendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_calendar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    CalendarView calendarView;
    private EditText recipename;
    private TextView date;
    private Button add;
    private ListView recipeanddate;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_calendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_calendar.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_calendar newInstance(String param1, String param2) {
        fragment_calendar fragment = new fragment_calendar();
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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        //Connecting java variables to the UI id
        CalendarView calendar = (CalendarView)view.findViewById(R.id.calendarView);
        final ListView recipeanddate = (ListView)view.findViewById(R.id.recipe_and_dates);
        final TextView thedate = (TextView)view.findViewById(R.id.date);
        final EditText recipename = (EditText)view.findViewById(R.id.recipe_on_date);
        Button add = (Button)view.findViewById(R.id.add_date);

        //on day selected, display date on textview
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                thedate.setText(date);

                //Function to display all the ingredients for a user based on their user id
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final ArrayList<String> list_of_recipe_on_day = new ArrayList<String>();
                db.collection("Recipe on day").document(userid).collection(date)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        list_of_recipe_on_day.add(document.getData().get("recipe_name").toString()
                                                + "         " + date);

                                        System.out.println("hi"+ list_of_recipe_on_day);

                                    }
                                    ArrayAdapter adapter = new ArrayAdapter(recipeanddate.getContext(), android.R.layout.simple_list_item_1, list_of_recipe_on_day);
                                    recipeanddate.setAdapter(adapter);
                                } else {
                                    Toast.makeText(getActivity().getBaseContext(), "Error getting ingredients", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        //add the recipe on date function
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = recipename.getText().toString();
                String date = thedate.getText().toString();

                addRecipeOnDay(name,date);
            }
        });










        return view;
    }













    //add recipe on day
    public void addRecipeOnDay(String recipe_name, final String date) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference recipe_on_dayRef = db.collection("Recipe on day").document(userid).collection(date).document();

        Recipe_on_the_day new_recipe_on_the_day = new Recipe_on_the_day();
        new_recipe_on_the_day.setRecipe_name(recipe_name);
        final ListView recipeanddate = (ListView)getActivity().findViewById(R.id.recipe_and_dates);

        recipe_on_dayRef.set(new_recipe_on_the_day).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity().getBaseContext(), "Added recipe on date", Toast.LENGTH_SHORT).show();

                    final ArrayList<String> list_of_recipe_on_day = new ArrayList<String>();
                    db.collection("Recipe on day").document(userid).collection(date)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            list_of_recipe_on_day.add(document.getData().get("recipe_name").toString()
                                                    + "," + date);

                                        }
                                        ArrayAdapter adapter = new ArrayAdapter(recipeanddate.getContext(), android.R.layout.simple_list_item_1, list_of_recipe_on_day);
                                        recipeanddate.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(getActivity().getBaseContext(), "Error getting ingredients", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getActivity().getBaseContext(), "Failure to add recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
