package br.com.vinibelo.trabalhoapicompose.ui.theme.car.form

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.CameraViewModel
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.CarImage
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.common.SharedCarsViewModel
import java.util.Objects

@Composable
fun FormCarScreen(
    modifier: Modifier = Modifier,
    onReturnPressed: () -> Unit,
    sharedCarsViewModel: SharedCarsViewModel,
    cameraViewModel: CameraViewModel = viewModel(),
    viewModel: FormCarViewModel = viewModel()
) {
    val context = LocalContext.current
    val file = cameraViewModel.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        file
    )
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        capturedImageUri = uri
        viewModel.sendImage(uri, context)

    }
    LaunchedEffect(viewModel.state.persistedOrDeletedCar) {
        if (viewModel.state.persistedOrDeletedCar) {
            onReturnPressed()
            sharedCarsViewModel.setCarChanged(true)
        }
    }
    if (viewModel.state.showConfirmationDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.attention),
            text = stringResource(R.string.deleting_car_attention_message),
            onConfirm = viewModel::deleteCar,
            onDismiss = viewModel::hideConfirmationDialog,
        )
    }

    Scaffold(
        modifier = Modifier
    ) { paddingValues ->
        val columnModifier = Modifier
            .padding(paddingValues)
        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormContent(
                onDeletePressed = viewModel::showConfirmationDialog,
                onSavePressed = viewModel::saveCar,
                name = viewModel.state.name,
                year = viewModel.state.year,
                license = viewModel.state.licence,
                imageUrl = viewModel.state.imageUrl,
                onNameChanged = viewModel::onNameChanged,
                onYearChanged = viewModel::onYearChanged,
                onLicenseChanged = viewModel::onLicenseChanged,
                onImageUrlChanged = viewModel::onImageUrlChanged,
                onCameraPressed = { cameraLauncher.launch(uri) },
                isEdit = viewModel.isEdit()
            )
        }
    }
}

@Composable
fun FormContent(
    modifier: Modifier = Modifier,
    onDeletePressed: () -> Unit,
    onSavePressed: () -> Unit,
    name: FormField<String>,
    year: FormField<String>,
    license: FormField<String>,
    imageUrl: FormField<String>,
    onNameChanged: (String) -> Unit,
    onYearChanged: (String) -> Unit,
    onLicenseChanged: (String) -> Unit,
    onImageUrlChanged: (String) -> Unit,
    onCameraPressed: () -> Unit,
    isEdit: Boolean
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .padding(bottom = 16.dp)
        ) {
            CarImage(
                modifier = Modifier
                    .size(140.dp),
                imageUrl = imageUrl.value
            )
        }
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        Row {
            FormTextField(
                modifier = textFieldModifier,
                title = "Name",
                value = name.value,
                errorMessageCode = name.errorMessageCode,
                onValueChange = onNameChanged
            )
        }
        Row {
            FormTextField(
                modifier = textFieldModifier,
                title = "Year",
                value = year.value,
                errorMessageCode = year.errorMessageCode,
                onValueChange = onYearChanged
            )
        }
        Row {
            FormTextField(
                modifier = textFieldModifier,
                title = "License",
                value = license.value,
                errorMessageCode = license.errorMessageCode,
                onValueChange = onLicenseChanged
            )
        }
        Row {
            FormTextField(
                modifier = textFieldModifier,
                title = "Image Url",
                value = imageUrl.value,
                errorMessageCode = imageUrl.errorMessageCode,
                onValueChange = onImageUrlChanged,
                trailingIcon = {
                    IconButton(
                        onClick = onCameraPressed
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
                onClick = onSavePressed,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = ""
                )
                Text(
                    text = "Save"
                )
            }
            if (isEdit) {
                Button(
                    colors = ButtonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Red,
                        disabledContentColor = Color.White
                    ),
                    onClick = onDeletePressed
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = ""
                    )
                    Text(
                        text = "Delete"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormContentPreview() {
    TrabalhoApiComposeTheme {
        FormContent(
            onDeletePressed = {},
            onSavePressed = {},
            name = FormField("Supra"),
            year = FormField("2001/2002"),
            license = FormField("ABC-1234"),
            imageUrl = FormField("https://www.ronbrooks.co.uk/wp-content/uploads/2023/06/toyota-supra-mk4.png"),
            onNameChanged = {},
            onYearChanged = {},
            onLicenseChanged = {},
            onImageUrlChanged = {},
            onCameraPressed = {},
            isEdit = true
        )
    }
}

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    errorMessageCode: Int = 0,
    readonly: Boolean = false,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardImeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val hasError = errorMessageCode > 0
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = title) },
        maxLines = 1,
        enabled = enabled,
        isError = hasError,
        readOnly = readonly,
        keyboardOptions = KeyboardOptions(
            capitalization = keyboardCapitalization,
            imeAction = keyboardImeAction,
            keyboardType = keyboardType
        ),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
    if (hasError) {
        Text(
            text = stringResource(errorMessageCode),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dismissButtonText: String? = null,
    confirmButtonText: String? = null
) {
    AlertDialog(
        modifier = modifier,
        title = title?.let {
            { Text(it) }
        },
        text = { Text(text) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(confirmButtonText ?: stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(confirmButtonText ?: stringResource(R.string.cancel))
            }
        }
    )
}
