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
package com.kanyideveloper.mealplanner.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.mealplanner.MealPlannerNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MealTypesScreen(
    navigator: MealPlannerNavigator
) {
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
            title = {},
            showBackArrow = true,
            navActions = {}
        )

        LazyVerticalGrid(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            columns = GridCells.Fixed(2)
        ) {
            item(span = { GridItemSpan(currentLineSpan = 3) }) {
                Text(
                    text = "Select Dish Types",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item(span = { GridItemSpan(currentLineSpan = 3) }) {
                Text(
                    text = "Choose the different meal types to prepare in a day",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            item(span = { GridItemSpan(currentLineSpan = 3) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(mealTypes) { type ->
                MealTypeItemCard(
                    mealType = type,
                    onClick = { /*TODO*/ }
                )
            }

            item(span = { GridItemSpan(currentLineSpan = 3) }) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item(span = { GridItemSpan(currentLineSpan = 3) }) {
                Button(onClick = {
                    navigator.openMealPlanner()
                }) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp),
                        text = "Complete",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

private val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Dessert")

@Composable
fun MealTypeItemCard(
    mealType: String,
    onClick: () -> Unit,
    selected: Boolean = false
) {
    Card(
        Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(4.dp)
            .clickable {
                onClick()
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mealType,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}