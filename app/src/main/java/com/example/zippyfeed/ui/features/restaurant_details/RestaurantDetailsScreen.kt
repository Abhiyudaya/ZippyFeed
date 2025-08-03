package com.example.zippyfeed.ui.features.restaurant_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.zippyfeed.R
import com.example.zippyfeed.data.models.FoodItem
import com.example.zippyfeed.ui.gridItems
import com.example.zippyfeed.ui.navigation.RestaurantDetails

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetailsScreen(
    navController: NavController,
    name: String,
    imageUrl: String,
    restaurantId: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: RestaurantViewModel = hiltViewModel()
) {
    LaunchedEffect(restaurantId) {
        viewModel.getFoodItem(restaurantId)
    }
    val uiState = viewModel.uiState.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item{
            RestaurantDetailsHeader(
                imageUrl = imageUrl,
                restaurantId = restaurantId,
                animatedVisibilityScope = animatedVisibilityScope,
                onBackClick = { navController.popBackStack() },
                onFavouriteClick = { /*TODO*/ }
            )
        }
        item{
            RestaurantDetails(
                title = name,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ",
                animatedVisibilityScope = animatedVisibilityScope,
                restaurantId = restaurantId
            )
        }
        when(uiState.value){
            is RestaurantViewModel.RestaurantEvent.Loading -> {
                item{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Loading")
                    }
                }
            }
            is RestaurantViewModel.RestaurantEvent.Nothing -> {}

            is RestaurantViewModel.RestaurantEvent.Success -> {
                val foodItems = (uiState.value as RestaurantViewModel.RestaurantEvent.Success).foodItems
                if (foodItems.isNotEmpty()){
                    gridItems(foodItems.size,2){ index ->
                        FoodItemView(footItem = foodItems[index])
                    }
                }
                else{
                    item{
                        Text(text = "No items found")
                    }
                }
            }
            RestaurantViewModel.RestaurantEvent.Error -> {
                item{
                    Text(text = "Error")
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetails(
    title : String,
    description : String,
    restaurantId: String,
    animatedVisibilityScope: AnimatedVisibilityScope
){
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.sharedElement(
                state = rememberSharedContentState(key = "title/${restaurantId}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        )
        Spacer(modifier = Modifier.size(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically){
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "4.5",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "(30+)",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {}) {
                Text(
                    text = "View All Reviews",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetailsHeader(
    imageUrl: String,
    restaurantId: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick : () -> Unit,
    onFavouriteClick : () -> Unit
){
    Box(modifier = Modifier.fillMaxWidth()){
        AsyncImage(model = imageUrl, contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .sharedElement(state = rememberSharedContentState(key = "image/${restaurantId}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
            contentScale = ContentScale.Crop)

        IconButton(onClick = onBackClick, modifier = Modifier.padding(16.dp).size(68.dp).align(Alignment.TopStart)) {
            Image(painter = painterResource(R.drawable.ic_back), contentDescription = null)
        }
        IconButton(onClick = onFavouriteClick, modifier = Modifier.padding(16.dp).size(58.dp).align(Alignment.TopEnd)) {
            Image(painter = painterResource(R.drawable.ic_favourite), contentDescription = null)
        }
    }
}

@Composable
fun FoodItemView(footItem : FoodItem){
    Column(modifier = Modifier
        .padding(8.dp)
        .width(162.dp)
        .height(216.dp)
        .shadow(elevation = 16.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Color.Gray.copy(alpha = 0.8f),
            spotColor = Color.Gray.copy(alpha = 0.8f)
        )
        .background(Color.White)
        .clip(RoundedCornerShape(16.dp))
    ){
        Box(Modifier
            .fillMaxWidth()
            .height(167.dp)
        ){
            AsyncImage(model = footItem.imageUrl, contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Text(text = "$${footItem.price}", color = Color.Black,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal =   10.dp)
                    .align(Alignment.TopStart)
            )
            Image(
                painter = painterResource(R.drawable.ic_favourite),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .align(Alignment.TopEnd)
            )

            Row(modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "4.5",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "(21)",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    modifier = Modifier.alpha(0.5f)
                )
            }
        }
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            Text(
                text = footItem.name ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = footItem.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}