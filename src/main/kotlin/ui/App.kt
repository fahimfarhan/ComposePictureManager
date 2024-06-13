package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.AppViewModel

class App {
  companion object {
    const val TAG = "APP"
  }

  private val appViewModel by lazy { AppViewModel() }

  @Composable
  private fun column0Source() {
    Column(modifier = Modifier.padding(8.dp)) {
     Text("Column0")
     Text("Ricardo Milos")
    }
  }

  @Composable
  private fun column1Arrows() {
    Column(modifier = Modifier.padding(8.dp)) {
     Text("Column1")
     Text("Arrows")
    }
  }

  @Composable
  private fun column2Target() {
    Column(modifier = Modifier.padding(8.dp)) {
     Text("Column2")
     Text("target")
    }
  }

  @Composable
  private fun column3Arrows() {
    Column(modifier = Modifier.padding(8.dp)) {
     Text("Column3")
     Text("Arrows")
    }
  }

  @Composable
  private fun column4Categories() {
    Column(modifier = Modifier.padding(8.dp)) {
     Text("Column4")
     Text("categories")
    }
  }


  @Composable
  fun appHome() {
    Row (modifier = Modifier.padding(16.dp)) {
      column0Source()
      column1Arrows()
      column2Target()
      column3Arrows()
      column4Categories()
    }
  }

  // ---------------------------------------------------
  @Composable
  fun initialDemoCode() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
      Button(onClick = {
        text = "Hello, Desktop!"
      }) {
        Text(text)
      }
    }
  }


  @Preview
  @Composable
  fun PreviewInitialDemoCode() {
    initialDemoCode()
  }
}