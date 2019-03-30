package com.bullach.android.bakingapp.data;

import android.content.Context;
import android.util.Log;

import com.bullach.android.bakingapp.data.dao.IngredientsDao;
import com.bullach.android.bakingapp.data.dao.RecipesDao;
import com.bullach.android.bakingapp.data.models.Ingredient;
import com.bullach.android.bakingapp.data.models.Recipe;
import com.bullach.android.bakingapp.data.models.Step;
import com.bullach.android.bakingapp.utilities.Constants;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {Ingredient.class, Recipe.class, Step.class},
        version = 1,
        exportSchema = false)
@TypeConverters(RecipeConverter.class)
public abstract class RecipesDatabase extends RoomDatabase {

    private static final String LOG_TAG = RecipesDatabase.class.getSimpleName();

    // The associated DAOs for the database
    public abstract IngredientsDao ingredientsDao();
    public abstract RecipesDao recipesDao();

    // Make the RecipesDatabase a singleton to prevent having multiple instances of the database opened at the same time.
    private static volatile RecipesDatabase INSTANCE;

    // Get the database
    public static RecipesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipesDatabase.class, Constants.DATABASE_NAME)
                            .build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return INSTANCE;
    }
}
