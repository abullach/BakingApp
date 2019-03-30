package com.bullach.android.bakingapp.utilities;

public class Constants {
    public Constants() {
    }

    /** Baking API base URL */
    static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    /** Database name */
    public static final String DATABASE_NAME = "recipe_database.db";

    /** OkHttp client timeout values */
    public static final int CONNECT_TIMEOUT_SHORT = 10;
    public static final int READ_TIMEOUT_LONG = 20;

    /** Number of threads in the pool used in AppExecutors */
    public static final int THREAD_COUNT = 3;

    /** Bundle extra for the recipe to be received in the intent */
    public static final String RECIPE_EXTRA = "recipe";

    /** Bundle extra for the step to be received in the intent */
    public static final String STEP_INDEX_EXTRA = "stepIndex";

    /** Bundle extra for the step to be received in the bundle to the fragment */
    public static final String STEP_DATA = "stepData";

    /** Bundle extra for the total number of steps to be received in the bundle to the fragment */
    public static final String STEP_TOTAL = "stepTotal";

    /** Extra for the recipe title to be received in the intent */
    public static final String RECIPE_TITLE = "recipeTitle";

    public static final String PLAYER_STATE = "playerState";

    /** Default values for the Shared Preferences */
    public static final String DEFAULT_STRING = "";
    public static final int DEFAULT_INTEGER = 1;
}
