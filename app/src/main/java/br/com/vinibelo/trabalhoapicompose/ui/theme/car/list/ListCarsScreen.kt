package br.com.vinibelo.trabalhoapicompose.ui.theme.car.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.model.Car
import br.com.vinibelo.trabalhoapicompose.model.CarDetails
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.CarImage
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.SharedCarsViewModel

@Composable
fun ListCarsScreen(
    modifier: Modifier = Modifier,
    onCarPressed: (CarDetails) -> Unit,
    onAddPressed: () -> Unit,
    onLogoutPressed: () -> Unit,
    sharedCarsViewModel: SharedCarsViewModel,
    viewModel: ListCarsViewModel = viewModel(),
) {
    if (sharedCarsViewModel.changesOnCar) {
        viewModel.fetchCars()
        sharedCarsViewModel.setCarChanged(false)
    }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPressed) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = ""
                )
            }
        },
        topBar = {
            TopBar(
                onRefreshPressed = viewModel::fetchCars,
                onLogoutPressed = onLogoutPressed
            )
        }
    ) { paddingValues ->
        CarList(
            modifier.padding(paddingValues),
            onCarPressed = onCarPressed,
            cars = viewModel.state.cars
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onRefreshPressed: () -> Unit,
    onLogoutPressed: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.cars)) },
        modifier = modifier.fillMaxWidth(),
        actions = {
            IconButton(onClick = onRefreshPressed) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = ""
                )
            }
            IconButton(onClick = onLogoutPressed) {
                Icon(
                    painter = painterResource(R.drawable.ic_logout),
                    contentDescription = ""
                )
            }
        }
    )
}

@Composable
fun CarList(
    modifier: Modifier = Modifier,
    onCarPressed: (CarDetails) -> Unit,
    cars: List<CarDetails>
) {
    LazyColumn(modifier = modifier) {
        items(cars) { car ->
            ListItem(
                modifier = Modifier.clickable { onCarPressed(car) },
                headlineContent = { Text(text = car.name) },
                supportingContent = { Text(car.year) },
                trailingContent = { Text(car.licence) },
                leadingContent = {
                    CarImage(
                        modifier = Modifier.size(70.dp),
                        imageUrl = car.imageUrl
                    )
                }
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CarListPreview() {
    TrabalhoApiComposeTheme {
        CarList(
            cars = listOf<CarDetails>(
                CarDetails(
                    name = "Skyline",
                    year = "2001/2002",
                    licence = "ABC-1234",
                    imageUrl = ""
                )
            ),
            onCarPressed = {}
        )
    }
}