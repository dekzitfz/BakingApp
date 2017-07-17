package id.dekz.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import id.dekz.bakingapp.features.recipelist.RecipeListActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by DEKZ on 7/16/2017.
 * ----------------------------------------------------------
 *                          N O T E
 * CURRENTLY THIS TEST ONLY SUPPORT ON HANDSET (NOT TABLET)
 * WITH SCREEN-WIDTH SMALLER THAN 600DP
 * ----------------------------------------------------------
 */

@RunWith(AndroidJUnit4.class)
public class RecipeNavigationTest {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<RecipeListActivity> testRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    @Before
    public void registerIdlingResource(){
        idlingResource = testRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void unregisterIdlingResource(){
        if(idlingResource != null) Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void selectRecipe_CheckIfToolbarDisplayedCorrectRecipe(){
        //pass an int recipe position here
        testScenarioCheckToolbarTitle(3);
    }

    private void testScenarioCheckToolbarTitle(int position){
        //scroll to item position
        onView(withId(R.id.rv_recipe))
                .perform(scrollToPosition(position));

        //click item
        onView(withRecyclerView(R.id.rv_recipe).atPosition(position))
                .perform(click());

        //check if toolbar title contain the correct recipe name
        onView(withId(R.id.toolbar))
                .check(matches(hasDescendant(withText(getRecipeName(position)))));
    }

    private String getRecipeName(int position){
        switch (position){
            case 0:
                return "Nutella Pie";
            case 1:
                return "Brownies";
            case 2:
                return "Yellow Cake";
            case 3:
                return "Cheesecake";
            default:
                return "";
        }
    }

}
