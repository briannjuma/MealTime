package com.kanyideveloper.mealtime

import androidx.navigation.NavController
import com.kanyideveloper.addmeal.presentation.addmeal.AddMealNavigator
import com.kanyideveloper.addmeal.presentation.addmeal.destinations.AddMealScreenDestination
import com.kanyideveloper.favorites.presentation.favorites.FavoritesNavigator
import com.kanyideveloper.home.presentation.home.HomeNavigator
import com.kanyideveloper.search.presentation.search.SearchNavigator
import com.kanyideveloper.settings.presentation.SettingsNavigator
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.spec.NavGraphSpec
import timber.log.Timber

class CoreFeatureNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController,
): HomeNavigator, SearchNavigator, FavoritesNavigator, SettingsNavigator, AddMealNavigator {
    override fun openFavorites(showId: Long) {
        Timber.d("Favorites")
    }

    override fun openAddMeal(showId: Long) {
        navController.navigateTo(AddMealScreenDestination within navGraph)
    }

    override fun openSearch(showId: Long) {
        Timber.d("Search")
    }

    override fun openSettings(showId: Long) {
        Timber.d("Settings")
    }
}