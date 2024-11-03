package br.com.vinibelo.trabalhoapicompose

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.AppCars

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS,
                0
            )
        }
        enableEdgeToEdge()
        setContent {
            TrabalhoApiComposeTheme {
                AppCars()
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA
        )
    }
}