package com.mmb.ui_home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.mmb.domain.model.CityWeather
import com.mmb.ui_home.HomeSideEffect.ShowError
import com.mmb.ui_theme.WeatherTrackerTheme
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    var errorMessageRes by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ShowError -> {
                    errorMessageRes = sideEffect.messageRes
                }
            }
        }
    }

    if (errorMessageRes != 0) {
        AlertDialog(
            onDismissRequest = {
                errorMessageRes = 0
            },
            title = { Text(text = stringResource(R.string.error)) },
            text = { Text(text = stringResource(errorMessageRes)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        errorMessageRes = 0
                        viewModel.handleViewEvent(HomeViewEvent.OnRetryRequested)
                    }
                ) {
                    Text(stringResource(R.string.retry))
                }
            }
        )
    }

    HomeScreen(
        modifier = modifier,
        state = state,
        onSearchQueryChanged = {
            viewModel.handleViewEvent(HomeViewEvent.OnSearchQueryChanged(it))
        },
        onCitySelected = {
            viewModel.handleViewEvent(HomeViewEvent.OnCitySelected(it))
        }
    )
}

@Composable
private fun HomeScreen(
    state: HomeViewState,
    onSearchQueryChanged: (String) -> Unit,
    onCitySelected: (CityWeather) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        SearchBar(
            state.searchQuery,
            onQueryChanged = onSearchQueryChanged
        )

        Spacer(Modifier.height(45.dp))

        when {
            state.isLoading -> LoadingState()

            state.isSearchMode -> SearchItems(
                cities = state.searchCities,
                onCitySelected = onCitySelected
            )

            state.currentCity != null -> CityWeatherState(state.currentCity)

            else -> EmptyState()
        }
    }
}

@Composable
private fun SearchBar(
    query: String?,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = query ?: "",
        onValueChange = onQueryChanged,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
            )
        },
        placeholder = { Text(text = stringResource(R.string.search_location)) },
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
        ),
    )
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8F),
        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.no_city_selected),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.please_search_for_a_city),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun CityWeatherState(
    city: CityWeather,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp)
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            modifier = Modifier.size(123.dp),
            model = city.icon,
            contentDescription = null,
        )

        Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleLarge
            )

            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null,
            )
        }

        Spacer(Modifier.height(20.dp))

        Row {
            Text(
                text = city.temperature.roundToInt().toString(),
                style = MaterialTheme.typography.displayLarge
            )

            Text(
                text = stringResource(R.string.celsius_sign),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(35.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
                .padding(vertical = 16.dp, horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InformationColumn(
                title = stringResource(R.string.humidity),
                value = stringResource(R.string.humidity_percent, city.humidity)
            )

            InformationColumn(
                title = stringResource(R.string.uv),
                value = city.uvIndex.toString()
            )

            InformationColumn(
                title = stringResource(R.string.feels_like),
                value = stringResource(R.string.temperature, city.feelsLike)
            )
        }
    }
}

@Composable
fun InformationColumn(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8F),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
private fun SearchItems(
    cities: List<CityWeather>,
    onCitySelected: (CityWeather) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cities) { city ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(20.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onCitySelected(city)
                    }
                    .padding(vertical = 16.dp, horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.weight(1F)
                ) {
                    Text(
                        text = city.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row {
                        Text(
                            text = city.temperature.roundToInt().toString(),
                            style = MaterialTheme.typography.displayLarge
                        )

                        Text(
                            text = stringResource(R.string.celsius_sign),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                AsyncImage(
                    modifier = Modifier
                        .size(100.dp),
                    model = city.icon,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
private fun HomeScreenPreview() {
    WeatherTrackerTheme {
        HomeScreen(
            state = HomeViewState(
                searchQuery = "",
                isLoading = true,
                currentCity = CityWeather(
                    name = "Paris",
                    temperature = 31.0,
                    humidity = 87,
                    uvIndex = 4.0,
                    feelsLike = 9.8,
                    icon = "https://cdn.weatherapi.com/weather/128x128/night/122.png",
                ),
                searchCities = listOf(
                    CityWeather(
                        name = "Paris",
                        temperature = 31.0,
                        humidity = 87,
                        uvIndex = 4.0,
                        feelsLike = 9.8,
                        icon = "https://cdn.weatherapi.com/weather/128x128/night/122.png",
                    ),
                )
            ),
            onSearchQueryChanged = {},
            onCitySelected = {}
        )
    }
}