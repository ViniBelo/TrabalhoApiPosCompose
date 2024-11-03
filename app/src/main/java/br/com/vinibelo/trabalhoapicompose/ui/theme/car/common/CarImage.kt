package br.com.vinibelo.trabalhoapicompose.ui.theme.car.common


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.model.Car
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.transform.CircleCropTransformation

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