/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanyideveloper.mealplanner.di

import android.content.Context
import com.kanyideveloper.core.data.MealTimePreferences
import com.kanyideveloper.core_database.dao.FavoritesDao
import com.kanyideveloper.core_database.dao.MealDao
import com.kanyideveloper.core_database.dao.MealPlanDao
import com.kanyideveloper.core_network.MealDbApi
import com.kanyideveloper.mealplanner.data.repository.MealPlannerRepositoryImpl
import com.kanyideveloper.mealplanner.domain.repository.MealPlannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MealPlannerModule {

    @Provides
    @Singleton
    fun providesMealPlannerRepository(
        mealTimePreferences: MealTimePreferences,
        mealsPlanDao: MealPlanDao,
        mealDbApi: MealDbApi,
        mealDao: MealDao,
        favoritesDao: FavoritesDao,
        context: Context
    ): MealPlannerRepository {
        return MealPlannerRepositoryImpl(
            mealTimePreferences = mealTimePreferences,
            mealPlanDao = mealsPlanDao,
            mealDao = mealDao,
            mealDbApi = mealDbApi,
            favoritesDao = favoritesDao,
            context = context
        )
    }
}
