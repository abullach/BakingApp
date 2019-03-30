package com.bullach.android.bakingapp.data.dao;

import com.bullach.android.bakingapp.data.models.Ingredient;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface IngredientsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllIngredients(List<Ingredient> ingredients);

    @Query("SELECT * FROM ingredient")
    List<Ingredient> getAllIngredients();

    @Query("DELETE FROM ingredient")
    void deleteIngredient();
}
