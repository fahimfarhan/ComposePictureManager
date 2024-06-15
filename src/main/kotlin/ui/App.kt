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
import extensions.ImageLoaderExtensions.isDirectory
import extensions.ImageLoaderExtensions.isFileOrIsValidUrl
import model.Category
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
  fun enterSourceFolder() {
    Row(modifier = Modifier.fillMaxWidth(1f)) {
      var mSrcDir by remember { appViewModel.srcImageDirState }

      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        value = mSrcDir,
        onValueChange = { mSrcDir = it },
        label = { Text("Enter source directory") },
//        maxLines = 1
      )

       Button(onClick = {
        if(!isDirectory(mSrcDir)) {
          println("not a valid directory $mSrcDir, returning!")
          return@Button
        }

        appViewModel.addAllImagesFromSrcDir()

      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add")
      }

    }
  }



  @Composable
  fun enterTargetFolder() {
    Row(modifier = Modifier.fillMaxWidth(1f)) {
      var mTargetDir by remember { appViewModel.targetImageDirState }

      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        value = mTargetDir,
        onValueChange = { mTargetDir = it },
        label = { Text("Enter target directory") },
//        maxLines = 1
      )

       /*Button(onClick = {
        if(!isDirectory(mTargetDir)) {
          println("not a valid directory $mTargetDir, returning!")
          return@Button
        }

        appViewModel.addAllImagesFromSrcDir()

      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add")
      }*/

    }
  }

    @Composable
  fun addCategory() {
    Row(modifier = Modifier.fillMaxWidth(1f)) {
      var mCategory by remember { appViewModel.categoryState }

      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        value = mCategory,
        onValueChange = { mCategory = it },
        label = { Text("Enter New Category") },
        maxLines = 1
      )

      Button(onClick = {
        println("Entered category: $mCategory")
        val copyMCategory = StringBuilder(mCategory).toString()
        appViewModel.createNewCategory(copyMCategory)
        mCategory = ""
      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add Category")
      }

    }
  }

  @Composable
  fun showCategoriesInLazyColumn() {
    val categoriesList: ArrayList<Category> by remember { appViewModel.mutableStateOfCategoriesList }
    LazyColumn {
      items(
        categoriesList,
        key = { category -> category.uniqueKey }
      ) { mCategory ->
        showCategoryRow(mCategory)
      }
    }
  }

  @Composable
  fun showCategoryRow(category: Category) {
    Text(text = category.name)
    // edittext for subcategory
    addSubCategory(category)
    // rv of subCategories
    showSubCategoriesInNestedLazyColumn(category)
  }

      @Composable
  fun addSubCategory(parentCategory: Category) {
    Row(modifier = Modifier.fillMaxWidth(1f)) {
      var mSubCategory by remember { mutableStateOf("") }

      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        value = mSubCategory,
        onValueChange = { mSubCategory = it },
        label = { Text("New Sub Category") },
        maxLines = 1
      )

      Button(onClick = {
        println("Entered sub category: $mSubCategory")
        val copySubCategory = StringBuilder(mSubCategory).toString()
        appViewModel.createNewSubCategory(parentCategory, copySubCategory)
        mSubCategory = ""
      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add Sub Category")
      }

    }
  }

  @Composable
  fun showSubCategoriesInNestedLazyColumn(category: Category) {
    for(subCategory in category.subCategories) {
      Text(text = subCategory)
    }
  }

  /*
  @Composable
  fun showSubCategoriesInNestedLazyColumnFailed(parentCategory: Category) {
    val mSubCategories by remember { parentCategory.mutableStateOfSubCategories }
    if(mSubCategories.isNullOrEmpty()) {
      return
    }
    LazyColumn(modifier = Modifier.height(100.dp)) {
      items(
        mSubCategories?:ArrayList(),
        key = { subCategory -> subCategory }
      ) { mSubCategory ->
        showSubCategoryRow(parentCategory, mSubCategory)
      }
    }
  }
  */

  @Composable
  fun showSubCategoryRow(parentCategory: Category, subCategory: String) {
    Text(text = subCategory)
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
    var imgBitmap: ImageBitmap? by remember { mImage.stateImageBitmap }
    // ui
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).background(color = MyColors.red500).clickable {
        isSelected = !isSelected
      }) {

      // Image()

      Image(
        bitmap = imgBitmap?: defaultPlaceHolderImageBitmap, // mImage.imageBitmap?:defaultPlaceHolderImageBitmap,
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
      Text("Source", modifier = Modifier.padding(16.dp).background(color = MyColors.red500))
      enterSourceImageUrl()
      enterSourceFolder()
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
      Text("Target", modifier = Modifier.padding(16.dp).background(color = MyColors.red500))
      enterTargetFolder()
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
     Button(onClick = {
        println("Confirm move to target destination")
      }, modifier = Modifier.padding(top = 100.dp)

      ) {
        Text("Move Confirm")
      }
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
      addCategory()
      showCategoriesInLazyColumn()
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