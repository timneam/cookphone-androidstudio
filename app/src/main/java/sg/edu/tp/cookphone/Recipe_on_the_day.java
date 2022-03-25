package sg.edu.tp.cookphone;

public class Recipe_on_the_day {

    private String recipe_name;

    public Recipe_on_the_day(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public Recipe_on_the_day () {

    }
    public String getRecipe_name() {
        return recipe_name;
    }
    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

}
