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
package com.kanyideveloper.mealplanner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.compose_ui.theme.PrimaryColor
import com.kanyideveloper.compose_ui.theme.Shapes
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination

interface MealPlannerNavigator {
    fun popBackStack()
    fun openAllergiesScreen()
    fun openNoOfPeopleScreen()
    fun openMealTypesScreen()
    fun openMealPlanner()
}

@Destination
@Composable
fun MealPlannerScreen(
    navigator: MealPlannerNavigator,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val hasMealPlan = true
    var shouldShowMealsDialog by remember {
        mutableStateOf(false)
    }

    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
            title = {
                Text(text = "Meal Planner", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {
            }
        )

        if (shouldShowMealsDialog) {
            SelectMealDialog(
                mealType = viewModel.mealType.value,
                onDismiss = {
                    shouldShowMealsDialog = !shouldShowMealsDialog
                }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (!hasMealPlan) {
                EmptyStateComponent(
                    anim = R.raw.women_thinking,
                    message = "Looks like you haven't created a meal plan yet",
                    content = {
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = {
                            navigator.openAllergiesScreen()
                        }) {
                            Text(text = "Get Started")
                        }
                    }
                )
            }

            if (hasMealPlan) {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(days) { day ->
                                DayItemCard(day = day.name, date = day.date)
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(mealTypes) { type ->
                        MealPlanItem(
                            mealType = type,
                            onClickAdd = { mealType ->
                                viewModel.setMealTypeState(mealType)
                                shouldShowMealsDialog = !shouldShowMealsDialog
                            }
                        )
                    }
                }
            }
        }
    }
}

data class Day(
    val name: String,
    val date: String
)

private val days = listOf(
    Day(name = "Mon", date = "02"),
    Day(name = "Tue", date = "03"),
    Day(name = "Wed", date = "04"),
    Day(name = "Thur", date = "05"),
    Day(name = "Fri", date = "06"),
    Day(name = "Sat", date = "07"),
    Day(name = "Sun", date = "08")
)

private val meal = Meal(
    name = "Test Meal that will fit here will fit well",
    imageUrl = "https://www.themealdb.com/images/media/meals/020z181619788503.jpg",
    cookingTime = 0,
    category = "Test",
    cookingDifficulty = "",
    ingredients = listOf(),
    cookingDirections = listOf(),
    isFavorite = false,
    servingPeople = 0
)

private val mealTypes = listOf(
    MealType(
        name = "Breakfast",
        meals = listOf(meal, meal)
    ),
    MealType(
        name = "Lunch",
        meals = listOf(meal, meal)
    ),
    MealType(
        name = "Dinner",
        meals = listOf(meal, meal)
    )
)

data class MealType(
    val name: String,
    val meals: List<Meal> = emptyList()
)

@Composable
fun MealPlanItem(
    mealType: MealType,
    onClickAdd: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    text = mealType.name,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = {
                    onClickAdd(mealType.name)
                }) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.add_circle),
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
        ) {
            items(mealType.meals) { meal ->
                PlanMealItem(meal = meal)
            }
        }

        if (mealType.meals.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DayItemCard(
    day: String,
    date: String,
    onClick: () -> Unit = {},
    selected: Boolean = false
) {
    Card(
        Modifier
            .width(70.dp)
            .height(70.dp)
            .padding(2.dp)
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
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Text(
                    text = date,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
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
}

@Composable
fun PlanMealItem(
    meal: Meal,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 160.dp,
    imageHeight: Dp = 120.dp,
    isAddingToPlan: Boolean = false,
    isAdded: Boolean = false
) {
    Box(modifier = Modifier.width(cardWidth).padding(horizontal = 4.dp, vertical = 4.dp)) {
        Card(
            modifier = modifier
                .width(cardWidth)
                .wrapContentHeight(),
            shape = Shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .width(cardWidth)
                        .height(imageHeight),
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = meal.imageUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                placeholder(R.drawable.food_loading)
                            }).build()
                    ),
                    contentScale = ContentScale.Crop
                )

                Text(
                    modifier = Modifier.padding(4.dp),
                    text = meal.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (isAddingToPlan) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isAdded) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.secondary
                            },
                            contentColor = if (isAdded) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSecondary
                            }
                        ),
                        onClick = { /*TODO*/ }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                            Text(
                                text = "Add",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
                .background(
                    color = PrimaryColor,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 0.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {
            }) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun SelectMealDialog(
    onDismiss: () -> Unit,
    mealType: String
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.9f),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = mealType,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Search By",
                            style = MaterialTheme.typography.labelMedium
                        )

                        SearchableExpandedDropDownMenu(
                            listOfItems = listOf(""),
                            modifier = Modifier.fillMaxWidth(),
                            onDropDownItemSelected = { item ->
                                // viewModel.setCategory(item)
                            },
                            dropdownItem = { category ->
                                Text(text = category, color = Color.Black)
                            },
                            parentTextFieldCornerRadius = 4.dp
                        )
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Source",
                            style = MaterialTheme.typography.labelMedium
                        )

                        SearchableExpandedDropDownMenu(
                            listOfItems = listOf(""),
                            modifier = Modifier.fillMaxWidth(),
                            onDropDownItemSelected = { item ->
                                // viewModel.setCategory(item)
                            },
                            dropdownItem = { category ->
                                Text(text = category, color = Color.Black)
                            },
                            parentTextFieldCornerRadius = 4.dp
                        )
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    SearchBarComponent()
                }

                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(10) {
                    PlanMealItem(
                        meal = meal,
                        cardWidth = 160.dp,
                        imageHeight = 80.dp,
                        isAddingToPlan = true,
                        isAdded = false
                    )
                }
            }
        },
        confirmButton = {
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent() {
    Spacer(modifier = Modifier.height(4.dp))

    TextField(
        value = "",
        onValueChange = {
            // viewModel.setSearchTerm(it)
        },
        placeholder = {
            Text(
                text = "Search"
                // color = primaryGray
            )
        },

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            },
        shape = RoundedCornerShape(size = 8.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = true,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        maxLines = 1,
        singleLine = true,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null
            )
        }
    )
}