package br.com.vinibelo.trabalhoapicompose.ui.theme.car.common

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import java.io.File

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    fun createImageFile(): File {
        val storageDir = getApplication<Application>()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            storageDir
        )
    }
}
