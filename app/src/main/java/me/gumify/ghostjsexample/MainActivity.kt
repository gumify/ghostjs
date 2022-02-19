package me.gumify.ghostjsexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import me.gumify.ghostjs.Ghostjs
import me.gumify.ghostjsexample.ui.theme.GhostjsExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GhostjsExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val context = LocalContext.current
    val ghostjs = remember { Ghostjs(context) }
    val scope = rememberCoroutineScope()
    
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                ghostjs.executeFile("test.js")
                scope.launch {
                    val output: String? = ghostjs.eval("""return dio;""")
                    Log.d("Ghostjs", output.toString())
                }
            }
        ) {
            Text(text = "Go!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GhostjsExampleTheme {
        Greeting("Android")
    }
}