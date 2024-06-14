package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.AppViewModel

class App {
  companion object {
    const val TAG = "App"
  }

  private val appViewModel by lazy { AppViewModel() }



  @Composable
  fun enterSourceImageUrl() {
    Row(modifier = Modifier.fillMaxWidth(1f)) {

      var srcImgUrl by remember { appViewModel.mutableSrcImgUrl }
      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        value = srcImgUrl,
        onValueChange = { srcImgUrl = it },
        label = { Text("Enter Image Url") },
        maxLines = 1
      )

      Button(onClick = {
        appViewModel.addSrcImgUrl()
        srcImgUrl = ""
      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add")
      }
    }
  }

  @Composable
  private fun column0Source() {
    Column(modifier =
      Modifier.padding(8.dp)
        .fillMaxHeight()
        .fillMaxWidth(0.3f)
        .background(color = MyColors.neutral200)
    ) {
     Text("Source")
     Text("Ricardo Milos")
      enterSourceImageUrl()
    }
  }

  @Composable
  private fun column1Arrows() {
    Column(modifier = Modifier
      .padding(8.dp)
      .fillMaxHeight()
      .fillMaxWidth( (0.05f/0.7f) )
      .background(color = MyColors.neutral200)
    ) {
     // Text("Arrows")
      Button(onClick = {
        appViewModel.onRightArrowsClickMoveImagesFromSourceToTarget()
      }, modifier = Modifier.padding(top = 100.dp)

      ) {
        Text(">>>")
      }

      Button(onClick = {
        appViewModel.onLeftArrowsClickMoveImagesFromTargetToSource()
      }) {
        Text("<<<")
      }
    }
  }

  @Composable
  private fun column2Target() {
    Column(modifier = Modifier
      .padding(8.dp)
      .fillMaxHeight()
      .fillMaxWidth((0.3f/0.65f) )
      .background(color = MyColors.neutral200)
    ) {
     Text("target")
    }
  }

  @Composable
  private fun column3Arrows() {
    Column(modifier = Modifier
      .padding(8.dp)
      .fillMaxHeight()
      .fillMaxWidth((0.05f/0.35f) )
      .background(color = MyColors.neutral200)
    ) {
     Text("Arrows")
    }
  }

  @Composable
  private fun column4Categories() {
    Column(modifier = Modifier
      .padding(8.dp)
      .fillMaxHeight()
      .fillMaxWidth((0.3f / 0.3f) )
      .background(color = MyColors.neutral200)
    ) {
     Text("categories")
    }
  }


  @Composable
  fun appHome() {
    Row (modifier = Modifier.fillMaxSize().background(color = Color.White)) {
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