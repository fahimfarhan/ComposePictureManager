package domain

import androidx.compose.runtime.*
import data.SourceRepository
import data.TargetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import model.ImageModel

class AppViewModel {
  companion object {
    const val TAG = "AppViewModel"
  }

  private val viewModelScope = MainScope()
//    var sourceImageUrl: String by remember { mutableStateOf("") }
  var mutableSrcImgUrl: MutableState<String> = mutableStateOf("")
  var mFlow = MutableStateFlow("")

  val mutableStateOfSrcImagesList: MutableState<ArrayList<ImageModel>> = mutableStateOf(ArrayList())
  val mutableStateOfTargetImagesList: MutableState<ArrayList<ImageModel>> = mutableStateOf(ArrayList())


  val srcRepo by lazy {   SourceRepository()  }
  val targetRepo by lazy {    TargetRepository()  }

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
    val srcImgUrl = mutableSrcImgUrl.value
    srcRepo.addImages(srcImgUrl)
  }

}