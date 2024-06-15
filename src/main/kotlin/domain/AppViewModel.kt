package domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.CategoryRepository
import data.SourceRepository
import data.TargetRepository
import extensions.ImageLoaderExtensions.isDirectory
import extensions.ImageLoaderExtensions.isFile
import extensions.ImageLoaderExtensions.isValidUrl
import kotlinx.coroutines.MainScope
import model.Category
import model.ImageModel
import java.awt.Image
import java.io.File
import java.net.URL
import java.util.concurrent.Executors
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.imageio.ImageIO


class AppViewModel {
  companion object {
    const val TAG = "AppViewModel"
  }

  private val mExecutor = Executors.newCachedThreadPool()

  // private val viewModelScope = MainScope()
//    var sourceImageUrl: String by remember { mutableStateOf("") }
  var mutableSrcImgUrl: MutableState<String> = mutableStateOf("")

  val srcImageDirState: MutableState<String> = mutableStateOf("")
  val targetImageDirState: MutableState<String> = mutableStateOf("")
  val categoryState: MutableState<String> = mutableStateOf("")

  val categoryRepo by lazy { CategoryRepository() }

  val mutableStateOfSrcImagesList: MutableState<ArrayList<ImageModel>> = mutableStateOf(ArrayList())
  val mutableStateOfTargetImagesList: MutableState<ArrayList<ImageModel>> = mutableStateOf(ArrayList())
  val mutableStateOfCategoriesList: MutableState<ArrayList<Category>> = mutableStateOf(categoryRepo.rxListOfCategories.getValue())

  val srcRepo by lazy { SourceRepository()  }
  val targetRepo by lazy { TargetRepository()  }


  init {
    initObservers()
  }

  private fun initObservers() {
    srcRepo.rxObservableImageModelsList.subscribe { latestList ->
      mutableStateOfSrcImagesList.value = latestList
    }

    targetRepo.rxObservableImageModelsList.subscribe { latestList ->
      mutableStateOfTargetImagesList.value = latestList
    }

    categoryRepo.rxObservableListOfCategories.subscribe { latestList ->
      mutableStateOfCategoriesList.value = ArrayList() // triggers to clear the ui
      println("AppVm->initObservers->observableCategoriesList-> latestList: $latestList")
      mutableStateOfCategoriesList.value = latestList  // triggers to redraw with actual data
    }
  }

  fun onRightArrowsClickMoveImagesFromSourceToTarget() {
    srcRepo.printCurrentList(tag = "before: srcRepo")
    targetRepo.printCurrentList(tag = "before: targetRepo")
    val selectedImagesFromSource = srcRepo.getSelectedImages()
    println("selectedImagedFromSrc: $selectedImagesFromSource")
    targetRepo.saveSelectedImages(selectedImagesFromSource)

        srcRepo.printCurrentList(tag = "after: srcRepo")
    targetRepo.printCurrentList(tag = "after: targetRepo")

  }

  fun onLeftArrowsClickMoveImagesFromTargetToSource() {
    srcRepo.printCurrentList(tag = "before: srcRepo")
    targetRepo.printCurrentList(tag = "before: targetRepo")
    val selectedImagesFromTarget = targetRepo.getSelectedImages()
        println("selectedImagedFromTarget: $selectedImagesFromTarget")
    srcRepo.saveSelectedImages(selectedImagesFromTarget)
        srcRepo.printCurrentList(tag = "after: srcRepo")
    targetRepo.printCurrentList(tag = "after: targetRepo")
  }

  fun addSrcImgUrl() {
    val srcImgUrl = mutableSrcImgUrl.value.trim()
    srcRepo.addImage(srcImgUrl)
  }

  fun addAllImagesFromSrcDir() {
    val mSrcDir = srcImageDirState.value
    val mListOfImages = listFilesForFolder(File(mSrcDir))
//    for(i in mListOfImages) {
//      println(i.absolutePath)
//    }

    srcRepo.addImagesFromSrcDir(mListOfImages)

  }

  fun isImageFile(str: String?): Boolean {
    // Regex to check valid image file extension.
    val regex = "(\\S+(\\.(?i)(jpe?g|png|gif|bmp))$)"


    // Compile the ReGex
    val p: Pattern = Pattern.compile(regex)


    // If the string is empty
    // return false
    if (str == null) {
      return false
    }


    // Pattern class contains matcher() method
    // to find matching between given string
    // and regular expression.
    val m: Matcher = p.matcher(str)


    // Return if the string
    // matched the ReGex
    return m.matches()
  }

  private fun listFilesForFolder(folder: File): ArrayList<File> {
    val output = ArrayList<File>()
    val mFiles =  folder.listFiles()
    var mSize = mFiles?.size?:0
    for (i in 0 until mSize ) {
      val fileEntry = mFiles!![i]
      if (fileEntry.isDirectory) {
        val tmp = listFilesForFolder(fileEntry)
        output.addAll(tmp)
      } else {
        // println(fileEntry.name)
        if(isImageFile(fileEntry.absolutePath)) {
          output.add(fileEntry)
        }
      }
    }
    return output
  }

  fun createNewCategory(categoryName: String) {
    categoryRepo.addCategory(categoryName)
  }

  fun createNewSubCategory(parentCategory: Category, subCategory: String) {
    categoryRepo.addSubCategory(parentCategory, subCategory)
  }

  fun deleteCategory(category: Category) {
    categoryRepo.removeCategory(category)
  }

  fun deleteSubCategory(category: Category, subCategory: String) {
    categoryRepo.removeSubCategory(category, subCategory)
  }

  fun onSelect(category: Category, subCategory: String) {
    println("inside onSelect-> category=${category.name}, subCategory: $subCategory, targetDir: ${targetImageDirState.value}")
    val targetDirParent = targetImageDirState.value
    if(!isDirectory(targetDirParent)) {
      println("TargetDirParent $targetDirParent is invalid.")
      return
    }

    val targetCatDir = File(targetDirParent, category.name)
    val targetCatSubCatDir = File(targetCatDir, subCategory)

    moveFilesFromSrcToTargetAsync(targetCatSubCatDir)

  }

  private fun moveFilesFromSrcToTargetAsync(targetDir: File) {
    mExecutor.execute {
      moveFilesFromSrcToTargetBlocking(targetDir)
    }
  }

  private fun moveFilesFromSrcToTargetBlocking(targetCatSubCatDir: File) {
    var dirExists = false
    if(!targetCatSubCatDir.exists()) {
      dirExists = targetCatSubCatDir.mkdirs()
    } else {
      dirExists = true
    }

    if(!dirExists) {
      println("Failed to create dirs / dir does not exist!")
      return
    }

    val mSelectedImages = targetRepo.getSelectedImages()

    mSelectedImages.forEach { imgModel ->
      moveASingleImage(imgModel, targetCatSubCatDir)
      saveRemoteFile(imgModel, targetCatSubCatDir)
    }
    targetRepo.removeImages(mSelectedImages)

  }

  private fun saveRemoteFile(imgModel: ImageModel, targetDir: File) {
    val imgUrl = imgModel.imageUrl
    if( !isValidUrl(imgUrl)) {
      return
    }
    val targetFile = File(targetDir, "${System.currentTimeMillis()}.jpg")
    targetFile.createNewFile()
    val bufferedImg = ImageIO.read(URL(imgUrl))
    ImageIO.write(bufferedImg, "jpg", targetFile)
  }

  private fun moveASingleImage(imgModel: ImageModel, targetDir: File) {
    val imgUrl = imgModel.imageUrl
    if( !isFile(imgUrl) ) {
      println("Invalid file! skipping $imgUrl")
      return
    }

    val srcFile = File(imgUrl)
    val imageName = srcFile.name

    var targetFile = File(targetDir, imageName)
    if(targetFile.exists()) {
      targetFile = File(targetDir, "${System.currentTimeMillis()}-$imageName")
    }
    srcFile.renameTo(targetFile)
  }

}