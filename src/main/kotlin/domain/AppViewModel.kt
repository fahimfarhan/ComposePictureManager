package domain

import data.SourceRepository
import data.TargetRepository
import kotlinx.coroutines.MainScope

class AppViewModel {
  companion object {
    const val TAG = "AppViewModel"
  }

  private val viewModelScope = MainScope()

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

}