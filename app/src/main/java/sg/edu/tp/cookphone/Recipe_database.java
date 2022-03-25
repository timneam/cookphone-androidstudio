package sg.edu.tp.cookphone;

public class Recipe_database {
    //Declaring the variables
    private String recipe_name;
    private String duration;
    private String ingredients;
    private String steps;
    private String nutritionalvalues;

    //the variables for the ingredients database
    public Recipe_database(String ingredient, int quantity ) {
        this.recipe_name = recipe_name;
        this.duration = duration;
        this.ingredients = ingredients;
        this.steps = steps;
        this.nutritionalvalues = nutritionalvalues;
    }

    public Recipe_database () {

    }

    public String getRecipe_name() {
        return recipe_name;
    }
    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }
    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getNutritionalvalues() {
        return nutritionalvalues;
    }
    public void setNutritionalvalues(String nutritionalvalues) { this.nutritionalvalues = nutritionalvalues; }
}
