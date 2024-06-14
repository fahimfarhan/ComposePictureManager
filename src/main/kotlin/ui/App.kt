package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import domain.AppViewModel
import extensions.ImageLoaderExtensions.defaultPlaceHolderImageBitmap
import extensions.ImageLoaderExtensions.isFileOrIsValidUrl
import model.ImageModel

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
//        maxLines = 1
      )

      Button(onClick = {
        if(!isFileOrIsValidUrl(srcImgUrl)) {
          println("not a valid file or url $srcImgUrl, returning!")
          return@Button
        }

        appViewModel.addSrcImgUrl()
        srcImgUrl = ""
      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add")
      }
    }
  }


  @Composable
  private fun showImagesInLazyColumn(someMutableState: MutableState<ArrayList<ImageModel>>) {
    val mImagesList: ArrayList<ImageModel> by remember { someMutableState }
    LazyColumn {
      items(
        mImagesList,
        key = { mImg -> mImg.key }
      ) { mSrcImage ->
        showImageModelRow(mSrcImage)
      }
    }
  }


  @Composable
  private fun showImageModelRow(mImage: ImageModel) {
    // data
    var isSelected by remember { mImage.isSelectedMutableState }

    // ui
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).background(color = MyColors.red500).clickable {
        isSelected = !isSelected
      }) {

      // Image()

      Image(
        bitmap = mImage.imageBitmap?:defaultPlaceHolderImageBitmap,
        contentDescription = mImage.imageUrl,
        modifier = Modifier.width(100.dp)
      )

      Text(text = mImage.imageUrl, Modifier.padding(4.dp).fillMaxWidth()
        .background( if(isSelected) MyColors.orange500 else MyColors.neutral200 ) )
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
      showImagesInLazyColumn(appViewModel.mutableStateOfSrcImagesList)
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
      showImagesInLazyColumn(appViewModel.mutableStateOfTargetImagesList)
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