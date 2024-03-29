/*
 * Copyright 2022 Joel Kanyi.
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
package com.kanyideveloper.favorites.presentation.favorites.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.compose_ui.theme.Shapes
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.model.Favorite
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination

interface FavoritesNavigator {
    fun openOnlineMealDetails(mealId: String)
    fun openMealDetails(meal: Meal)
}

@Destination
@Composable
fun FavoritesScreen(
    navigator: FavoritesNavigator,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.observeAsState()
    val meal = viewModel.singleMeal.observeAsState().value?.observeAsState()?.value

    FavoritesScreenContent(
        favorites = favorites,
        onClick = { _, onlineMealId, localMealId, isOnline ->
            if (isOnline) {
                onlineMealId?.let { navigator.openOnlineMealDetails(mealId = it) }
            } else {
                if (localMealId != null) {
                    viewModel.getASingleMeal(id = localMealId)

                    if (meal != null) {
                        navigator.openMealDetails(meal = meal)
                    }
                }
            }
        },
        onFavoriteClick = { favorite ->
            viewModel.deleteAFavorite(favorite = favorite)
        },
        onClickDeleteAllFavorites = {
            viewModel.deleteAllFavorites()
        }
    )
}

@Composable
private fun FavoritesScreenContent(
    favorites: List<Favorite>?,
    onClick: (Int?, String?, Int?, Boolean) -> Unit,
    onFavoriteClick: (Favorite) -> Unit,
    onClickDeleteAllFavorites: () -> Unit
) {
    var mDisplayMenu by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {},
            title = {
                Text(text = "Favorite meals", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {
                IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                    Icon(Icons.Default.MoreVert, "")
                }

                DropdownMenu(
                    expanded = mDisplayMenu,
                    onDismissRequest = { mDisplayMenu = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onClickDeleteAllFavorites()
                            mDisplayMenu = false
                        },
                        text = {
                            Text(text = "Delete All Favorites")
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.chevron_right),
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(favorites ?: emptyList()) { favorite ->
                    FoodItem(
                        favorite = favorite,
                        onClick = onClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }

            if (favorites.isNullOrEmpty()) {
                EmptyStateComponent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItem(
    favorite: Favorite,
    modifier: Modifier = Modifier,
    onClick: (Int?, String?, Int?, Boolean) -> Unit,
    onFavoriteClick: (Favorite) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        shape = Shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = {
            onClick(
                favorite.id,
                favorite.onlineMealId,
                favorite.localMealId,
                favorite.onlineMealId != null
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(),
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = favorite.mealImageUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                placeholder(R.drawable.placeholder)
                            }).build()
                    ),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = favorite.mealName,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .padding(0.dp)
                    .clickable {
                        onFavoriteClick(favorite)
                    },
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color(0xFFfa4a0c)
            )
        }
    }
}
