package com.bullach.android.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.RecipeConverter;
import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.ui.activities.RecipeDetailsActivity;
import com.bullach.android.bakingapp.ui.activities.RecipeListActivity;
import com.bullach.android.bakingapp.utilities.Constants;

import java.util.List;

import androidx.core.app.TaskStackBuilder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    public static final String EXTRA_LABEL = "TASK_TEXT";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews remoteViews = getIngredientListRemoteView(context, appWidgetId);

        // Click event handler for the title, launches the app when the user clicks on the widget title
        Intent titleIntent = new Intent(context, RecipeListActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_recipe_name, titlePendingIntent);

        // Intent template to handle the click listener for each list view item
        Intent clickIntentTemplate = new Intent(context, RecipeDetailsActivity.class);

        // Retrieve recipe data from private Shared Preferences file
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_file_key), MODE_PRIVATE);

        // Extract recipe data from sharedPreferences for creating the recipe object
        int recipeId = prefs.getInt(context.getString(R.string.pref_recipe_id_key), -1);
        // Get the recipe name
        String recipeName = prefs.getString(context.getString(R.string.pref_recipe_name_key), context.getString(R.string.widget_title_text));
        // Get the string of ingredients
        String ingredientString = prefs.getString(context.getString(R.string.pref_ingredient_string_key),  Constants.DEFAULT_STRING);
        // Use TypeConverter to convert ingredient string to the list of ingredients
        List<Ingredient> ingredientList = RecipeConverter.toIngredientsList(ingredientString);
        // Get the string of steps
        String stepString = prefs.getString(context.getString(R.string.pref_step_string_key),  Constants.DEFAULT_STRING);
        // Use TypeConverter to convert step string to the list of steps
        List<Step> stepList = RecipeConverter.toStepsList(stepString);
        // Get the servings
        int servings =  prefs.getInt(context.getString(R.string.pref_servings_key), Constants.DEFAULT_INTEGER);
        // Get the image url
        String image = prefs.getString(context.getString(R.string.pref_image_key),  Constants.DEFAULT_STRING);

        // Create the recipe object which can be passed to the activity
        Recipe recipe = new Recipe(recipeId, recipeName, ingredientList, stepList, servings, image);

        // Pass the bundle through the intent
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_EXTRA, recipe);

        clickIntentTemplate.putExtra(Constants.RECIPE_EXTRA, bundle);

        // Update recipe name
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);

        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_recipe_list, clickPendingIntentTemplate);

        // Toast.makeText(context.getApplicationContext(), "Widget", Toast.LENGTH_LONG).show(); // Toast is for debugging purposes

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_list);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
            // Trigger data update to handle the ListView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_list);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        // Toast is for debugging purposes
        // Toast.makeText(context.getApplicationContext(), "onUpdate", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        // Toast is for debugging purposes
        // Toast.makeText(context.getApplicationContext(), "onEnabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Creates and returns the RemoteViews to be displayed in the ListView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the ListView mode widget
     */
    private static RemoteViews getIngredientListRemoteView(Context context, int appWidgetId) {

        // Here we setup the intent which points to the RecipeWidgetService which will
        // provide the views for this collection.
        Intent intent = new Intent(context, RecipeWidgetService.class);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_list);

        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connect to a RemoteViewsService through the specified intent.
        remoteViews.setRemoteAdapter(R.id.widget_recipe_list, intent);
        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        remoteViews.setEmptyView(R.id.widget_recipe_list, R.id.widget_empty_view);
        return remoteViews;
    }
}

