package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
import java.lang.module.ModuleFinder

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
        onValueChange = { srcImgUrl = it.trim() },
        label = { Text("Enter Image Url") },
//        maxLines = 1
      )

      Button(onClick = {
        if(srcImgUrl.isBlank()) return@Button
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
        onValueChange = { mSrcDir = it.trim() },
        label = { Text("Enter source directory") },
//        maxLines = 1
      )

       Button(onClick = {
         if(mSrcDir.isBlank()) return@Button
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
        onValueChange = { mTargetDir = it.trim() },
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
        onValueChange = { mCategory = it.trim() },
        label = { Text("Enter New Category") },
        maxLines = 1
      )

      Button(onClick = {
        println("Entered category: $mCategory")
        if(mCategory.isBlank()) return@Button
        val copyMCategory = StringBuilder(mCategory).toString()
        appViewModel.createNewCategory(copyMCategory)
        mCategory = ""
      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add")
      }

    }
  }

  @Composable
  fun showCategoriesInLazyColumn() {
    val categoriesList: ArrayList<Category> by remember { appViewModel.mutableStateOfCategoriesList }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
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
    Divider(color = MyColors.blue500, modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
      Text(text = category.name, modifier = Modifier.fillMaxWidth(0.8f).height(32.dp).background(color = MyColors.orange500))
      Button(onClick = {
        appViewModel.deleteCategory(category)
      }, modifier = Modifier.height(32.dp)) {
        Text(text = "X")
      }
    }

    // edittext for subcategory
    Divider(color = Color.White, modifier = Modifier.height(8.dp))
    addSubCategory(category)
    Divider(color = MyColors.blue500, modifier = Modifier.height(8.dp))

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
        onValueChange = { mSubCategory = it.trim() },
        label = { Text("New Sub Category") },
        maxLines = 1
      )

      Button(onClick = {
        println("Entered sub category: $mSubCategory")
        if(mSubCategory.isBlank()) {
          return@Button
        }
        val copySubCategory = StringBuilder(mSubCategory).toString()
        appViewModel.createNewSubCategory(parentCategory, copySubCategory)
        mSubCategory = ""
      }, modifier = Modifier.background(color = MyColors.blue500)) {
        Text("Add")
      }

    }
  }

  @Composable
  fun showSubCategoriesInNestedLazyColumn(category: Category) {
    for(subCategory in category.subCategories) {
      Row(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = subCategory,
          modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(32.dp)
            .background(color = MyColors.yellow500).
            clickable {
          appViewModel.onSelect(category, subCategory)
        })
        Button(onClick = {
          appViewModel.deleteSubCategory(category, subCategory)
        }
        ) {
          Text("X")
        }
      }
      Divider(color = Color.Black)
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
    val imgBitmap: ImageBitmap? by remember { mImage.stateImageBitmap }
    // ui
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).background(color = MyColors.yellow500).clickable {
        isSelected = !isSelected
      }) {

      // Image()

      Image(
        bitmap = imgBitmap?: defaultPlaceHolderImageBitmap, // mImage.imageBitmap?:defaultPlaceHolderImageBitmap,
        contentDescription = mImage.imageUrl,
        modifier = Modifier.width(100.dp)
      )

      Text(text = mImage.imageUrl, Modifier.padding(4.dp).fillMaxWidth().fillMaxHeight()
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
//      column3Arrows()
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