package domain

import androidx.compose.runtime.*
import data.SourceRepository
import data.TargetRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow

class AppViewModel {
  companion object {
    const val TAG = "AppViewModel"
  }

  private val viewModelScope = MainScope()
//    var sourceImageUrl: String by remember { mutableStateOf("") }
  var mutableSrcImgUrl: MutableState<String> = mutableStateOf("")
  var mFlow = MutableStateFlow("")


  private val srcRepo by lazy {   SourceRepository()  }
  private val targetRepo by lazy {    TargetRepository()  }

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