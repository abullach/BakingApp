package com.bullach.android.bakingapp;

import com.bullach.android.bakingapp.ui.activities.RecipeListActivity;
import com.bullach.android.bakingapp.utilities.OkHttpProvider;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;

/**
 * Android Espresso tests for RecipeListActivity
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private static final String NAME_OKHTTP= "OkHttp";
    private static final String FIRST_RECIPE_NAME = "Nutella Pie";
    private static final String SECOND_RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    // Register the IdlingResource for OkHttp before the test
    @Before
    public void registerIdlingResource() {
        IdlingResource idlingResource = OkHttp3IdlingResource.create(NAME_OKHTTP, OkHttpProvider.getOkHttpInstance());
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void test_01_onLaunch_recipeListIsDisplayed() {
        onView(withId(R.id.rvRecipeList)).check(matches(isDisplayed()));
    }

    @Test
    public void test_02_scrollToPosition_CheckRecipeName() {
        onView(withId(R.id.rvRecipeList)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(FIRST_RECIPE_NAME)).check(matches(isDisplayed()));

        onView(withId(R.id.rvRecipeList)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(SECOND_RECIPE_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void test_03_clickRecyclerViewItem_OpensDetailActivity() {
        CharSequence title = InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.app_name);
        matchToolbarTitle(title);

        onView(withId(R.id.rvRecipeList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        matchToolbarTitle(SECOND_RECIPE_NAME);
    }

    @Test
    public void test_04_clickRecyclerViewItem_checkStepsListExistsOnDetailActivity() {
        onView(withId(R.id.rvRecipeList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.steps_list_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void test_05_clickRecyclerViewItem_checkIngredientListExistsOnDetailActivity() {
        onView(withId(R.id.rvRecipeList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.ingredients_list_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void test_06_checkPlayerViewIsVisibleOnStepDetail() {
        onView(ViewMatchers.withId(R.id.rvRecipeList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.steps_list_fragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.flPlayer)).check(matches(isDisplayed()));
    }

    /**
     * Helper method to check toolbar has the expected title.
     */
    private static void matchToolbarTitle(CharSequence title) {
        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));
    }

    /**
     * Returns a custom BoundedMatcher, which gives us type safety.
     */
    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {

            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                // Checks the value fo the title by calling Toolbar.getTitle()
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    // Unregister the IdlingResource for OkHttp after the test.
    @After
    public void unregisterIdlingResource() {
        IdlingResource idlingResource = OkHttp3IdlingResource.create(NAME_OKHTTP, OkHttpProvider.getOkHttpInstance());
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
