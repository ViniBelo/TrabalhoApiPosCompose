package br.com.vinibelo.trabalhoapicompose.ui.theme.car.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.CarImage
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

@Composable
fun FormCarScreen(
    modifier: Modifier = Modifier,
    viewModel: FormCarViewModel = viewModel()
) {
    Scaffold(
        modifier = Modifier
    ) { paddingValues ->
        val columnModifier = Modifier
            .padding(paddingValues)
            .padding(top = 32.dp)
        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                CarImage(
                    modifier = Modifier.size(140.dp),
                    car = viewModel.state.car
                )
            }
            FormContent(
                name = viewModel.state.car.name,
                year = viewModel.state.car.year,
                license = viewModel.state.car.license,
                imageUrl = viewModel.state.car.imageUrl
            )
        }
    }
}

@Composable
fun FormContent(
    modifier: Modifier = Modifier,
    name: String,
    year: String,
    license: String,
    imageUrl: String
) {
    Column(
        modifier = modifier
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textFieldModifier = Modifier.fillMaxWidth()
            .padding(top = 12.dp)
        Row {
            OutlinedTextField(
                modifier = textFieldModifier,
                label = { Text("Name") },
                value = name,
                onValueChange = {}
            )
        }
        Row {
            OutlinedTextField(
                modifier = textFieldModifier,
                label = { Text("Year") },
                value = year,
                onValueChange = {}
            )
        }
        Row {
            OutlinedTextField(
                modifier = textFieldModifier,
                label = { Text("License") },
                value = license,
                onValueChange = {}
            )
        }
        Row {
            OutlinedTextField(
                modifier = textFieldModifier,
                label = { Text("Image Url") },
                value = imageUrl,
                onValueChange = {},
                maxLines = 1,
                trailingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_camera),
                            contentDescription = ""
                        )
                    }
                }
            )
        }
        Row(
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Button(
                onClick = {},
                modifier = modifier.padding(end = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(end = 2.dp),
                    text = "Save"
                )
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = ""
                )
            }
            Button(
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Red,
                    disabledContentColor = Color.White
                ),
                onClick = {}
            ) {
                Text(
                    modifier = Modifier.padding(end = 2.dp),
                    text = "Delete"
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormContentPreview() {
    TrabalhoApiComposeTheme {
        FormContent(
            name = "Supra",
            year = "2001/2002",
            license = "ABC-1234",
            imageUrl = "https://www.ronbrooks.co.uk/wp-content/uploads/2023/06/toyota-supra-mk4.png"
        )
    }
}
