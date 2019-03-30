package com.bullach.android.bakingapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bullach.android.bakingapp.R;
import com.bullach.android.bakingapp.data.RecipeConverter;
import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredientList = new ArrayList<>();

    public RecipeRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        // Toast is for debugging purposes
        // Toast.makeText(mContext.getApplicationContext(), "onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataSetChanged() {
        // Get the updated ingredient string from shared preferences
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.pref_file_key), MODE_PRIVATE);
        String ingredientString = prefs.getString(mContext.getString(R.string.pref_ingredient_string_key),  Constants.DEFAULT_STRING);

        // Convert ingredient string to the list of ingredients
        mIngredientList = RecipeConverter.toIngredientsList(ingredientString);
    }

    @Override
    public void onDestroy() {

    }

    /**
     * Returns the number of items to be displayed in the RecipeWidget
     */
    @Override
    public int getCount() {
        return mIngredientList == null ? 0 : mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_list_item);
        Ingredient ingredient = mIngredientList.get(position);
        remoteView.setTextViewText(R.id.tvWidgetIngredientName, ingredient.getIngredient());
        remoteView.setTextViewText(R.id.tvWidgetIngredientQuantity,
                ingredient.getQuantity() + " " + ingredient.getMeasure());
        // Handle click events on ListView items
        // Fill the pending intents template defined in RecipeWidget (AppWidgetProvider) class.
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeWidget.EXTRA_LABEL, mIngredientList.get(position));
        // Make the full row clickable by setting the click listener on the root element of widget_recipe_list_item.xml
        remoteView.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
