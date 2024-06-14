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


  val srcRepo by lazy {   SourceRepository()  }
  val targetRepo by lazy {    TargetRepository()  }

  init {
    initObservers()
  }

  private fun initObservers() {
    srcRepo.rxObservableImageModelsList.subscribe { latestList ->
      mutableStateOfSrcImagesList.value = latestList
    }
  }

  fun onRightArrowsClickMoveImagesFromSourceToTarget() {
    val selectedImagesFromSource = srcRepo.getSelectedImages()
    targetRepo.saveSelectedImages(selectedImagesFromSource)
  }

  fun onLeftArrowsClickMoveImagesFromTargetToSource() {
    val selectedImagesFromTarget = targetRepo.getSelectedImages()
    srcRepo.saveSelectedImages(selectedImagesFromTarget)
  }

  fun addSrcImgUrl() {
    val srcImgUrl = mutableSrcImgUrl.value
    srcRepo.addImages(srcImgUrl)
  }

}