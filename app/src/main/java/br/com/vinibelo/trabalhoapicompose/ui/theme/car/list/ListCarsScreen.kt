package br.com.vinibelo.trabalhoapicompose.ui.theme.car.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.model.Car
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.transform.CircleCropTransformation

@Composable
fun ListCarsScreen(
    modifier: Modifier = Modifier,
    viewModel: ListCarsViewModel = viewModel()
) {
    Scaffold(modifier = modifier) { paddingValues ->
        CarList(
            modifier.padding(paddingValues),
            cars = viewModel.state.cars
        )
    }
}

@Composable
fun CarList(
    modifier: Modifier = Modifier,
    cars: List<Car>
) {
    LazyColumn(modifier = modifier) {
        items(cars) { car ->
            ListItem(
                headlineContent = { Text(text = car.name) },
                supportingContent = { Text(car.year) },
                trailingContent = { Text(car.license) },
                leadingContent = {
                    CarImage(
                        modifier = Modifier.size(70.dp),
                        car = car
                    )
                }
            )
        }
    }
}

@Composable
fun CarImage(
    modifier: Modifier = Modifier,
    car: Car
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(car.imageUrl)
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = stringResource(R.string.car_picture),
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_download),
            error = painterResource(R.drawable.ic_error)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarListPreview() {
    TrabalhoApiComposeTheme {
        CarList(
            cars =
            listOf<Car>(
                Car(
                    id = "001",
                    name = "Skyline",
                    year = "2001/2002",
                    license = "ABC-1234",
                    imageUrl = ""
                )
            )
        )
    }
}