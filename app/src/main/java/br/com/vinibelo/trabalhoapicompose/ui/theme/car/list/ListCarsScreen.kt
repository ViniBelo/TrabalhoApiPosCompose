package br.com.vinibelo.trabalhoapicompose.ui.theme.car.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.vinibelo.trabalhoapicompose.model.Car
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.CarImage
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.SharedCarsViewModel

@Composable
fun ListCarsScreen(
    modifier: Modifier = Modifier,
    onCarPressed: (Car) -> Unit,
    sharedCarsViewModel: SharedCarsViewModel,
    viewModel: ListCarsViewModel = viewModel(),
) {
    if (sharedCarsViewModel.changesOnCar) {
        viewModel.fetchCars()
        sharedCarsViewModel.setCarChanged(false)
    }
    Scaffold(modifier = modifier) { paddingValues ->
        CarList(
            modifier.padding(paddingValues),
            onCarPressed = onCarPressed,
            cars = viewModel.state.cars
        )
    }
}

@Composable
fun CarList(
    modifier: Modifier = Modifier,
    onCarPressed: (Car) -> Unit,
    cars: List<Car>
) {
    LazyColumn(modifier = modifier) {
        items(cars) { car ->
            ListItem(
                modifier = Modifier.clickable { onCarPressed(car) },
                headlineContent = { Text(text = car.name) },
                supportingContent = { Text(car.year) },
                trailingContent = { Text(car.license) },
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
            cars = listOf<Car>(
                Car(
                    id = "001",
                    name = "Skyline",
                    year = "2001/2002",
                    license = "ABC-1234",
                    imageUrl = ""
                )
            ),
            onCarPressed = {}
        )
    }
}