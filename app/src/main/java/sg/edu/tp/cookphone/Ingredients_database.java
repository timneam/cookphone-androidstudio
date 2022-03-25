package sg.edu.tp.cookphone;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

@IgnoreExtraProperties
public class Ingredients_database {

    //Declaring the variables
    private String ingredient;
    private int quantity;

    //the variables for the ingredients database
    public Ingredients_database(String ingredient, int quantity ) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredients_database () {

    }
    public String getIngredient() {
        return ingredient;
    }
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
