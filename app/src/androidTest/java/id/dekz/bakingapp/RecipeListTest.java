package id.dekz.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import id.dekz.bakingapp.features.recipelist.RecipeListActivity;

/**
 * Created by DEKZ on 7/16/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListTest {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Rule
    public ActivityTestRule<RecipeListActivity> testRule =
            new ActivityTestRule<>(RecipeListActivity.class);

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
