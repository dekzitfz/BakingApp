package id.dekz.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import id.dekz.bakingapp.features.recipelist.RecipeListActivity;

/**
 * Created by DEKZ on 7/16/2017.
 * ----------------------------------------------------------
 *                          N O T E
 * CURRENTLY THIS TEST ONLY SUPPORT ON HANDSET (NOT TABLET)
 * WITH SCREEN-WIDTH SMALLER THAN 600DP
 * ----------------------------------------------------------
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListTest {

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
    public void checkRecipePositionContainCorrectText(){
        Espresso.onView(withRecyclerView(R.id.rv_recipe).atPosition(1))
                .check(ViewAssertions.matches(
                        ViewMatchers.hasDescendant(
                                ViewMatchers.withText("Brownies"))
                        )
                );
    }
}
