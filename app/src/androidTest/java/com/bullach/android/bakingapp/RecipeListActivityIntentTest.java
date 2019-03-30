package com.bullach.android.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;

import com.bullach.android.bakingapp.ui.activities.RecipeDetailsActivity;
import com.bullach.android.bakingapp.ui.activities.RecipeListActivity;
import com.bullach.android.bakingapp.utilities.Constants;
import com.bullach.android.bakingapp.utilities.OkHttpProvider;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

/**
 * Android Espresso tests for RecipeListActivity
 */
public class RecipeListActivityIntentTest {

    private static final String NAME_OKHTTP= "OkHttp";

    @Rule
    public IntentsTestRule<RecipeListActivity> mainActivityRule = new IntentsTestRule<>
            (RecipeListActivity.class);

    // Register the IdlingResource for OkHttp before the test
    @Before
    public void registerIdlingResource() {
        IdlingResource idlingResource = OkHttp3IdlingResource.create(NAME_OKHTTP, OkHttpProvider.getOkHttpInstance());
        IdlingRegistry.getInstance().register(idlingResource);

        // Stub all external intents
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void test_01_checkIntentToDetailsActivity(){
        onView(withId(R.id.rvRecipeList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(hasComponent(RecipeDetailsActivity.class.getName()));
        intended(hasExtraWithKey(Constants.RECIPE_EXTRA));
    }

    // Unregister the IdlingResource for OkHttp after the test.
    @After
    public void unregisterIdlingResource() {
        IdlingResource idlingResource = OkHttp3IdlingResource.create(NAME_OKHTTP,
                OkHttpProvider.getOkHttpInstance());
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
