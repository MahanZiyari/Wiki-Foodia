package ir.mahan.wikifoodia.utils.constants

object Constants {

    const val BASE_URL = "https://api.spoonacular.com/"
    const val BASE_URL_IMAGE_INGREDIENTS = "https://spoonacular.com/cdn/ingredients_100x100/"
    const val BASE_URL_IMAGE_RECIPE = "https://spoonacular.com/recipeImages/"
    const val CONNECTION_TIMEOUT = 60L
    const val MY_API_KEY = "c9d21a59cd2f48cb83f0ef9a97a5ff09"

    // DataStore keys
    const val REGISTER_DATASTORE_KEY = "register_datastore_key"
    const val DATASTORE_KEY_USERNAME = "datastore_key_username"
    const val DATASTORE_KEY_HASH = "datastore_key_hash"

    //Menu datastore
    const val MENU_DATASTORE = "menu_datastore"
    const val MENU_MEAL_TITLE_KEY = "menu_meal_title_key"
    const val MENU_MEAL_ID_KEY = "menu_meal_id_key"
    const val MENU_DIET_TITLE_KEY = "menu_diet_title_key"
    const val MENU_DIET_ID_KEY = "menu_diet_id_key"

    //Database
    const val RECIPE_TABLE_NAME = "recipe_table_name"
    const val DATABASE_NAME = "database_name"
    const val DETAIL_TABLE_NAME = "detail_table_name"
    const val FAVORITE_TABLE_NAME = "favorite_table_name"


    //Other
    const val OLD_IMAGE_SIZE = "312x231.jpg"
    const val NEW_IMAGE_SIZE = "636x393.jpg"
    const val REPEAT_TIME = 100
    const val DELAY_TIME = 3000L
    var STEPS_COUNT = 0
    const val DEBUG_TAG = "Debug"
}