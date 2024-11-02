package br.com.vinibelo.trabalhoapicompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrabalhoApiComposeTheme {
                Scaffold {
                    Text(
                        modifier = Modifier,
                        text =  "Hello World!"
                    )
                }
            }
        }
    }
}