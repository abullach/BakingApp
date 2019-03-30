package com.bullach.android.bakingapp.data.dao;

import com.bullach.android.bakingapp.data.models.Recipe;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRecipe(Recipe recipe);

    @Query("SELECT * FROM recipe")
    List<Recipe> getAllRecipes();

    @Query("DELETE FROM recipe")
    void deleteRecipe();
}
