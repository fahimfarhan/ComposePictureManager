package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import extensions.ImageLoaderExtensions
import java.awt.print.Book

data class ImageModel (
  val imageUrl: String,
  val isSelectedMutableState: MutableState<Boolean> = mutableStateOf(false),
  val key: Long = System.currentTimeMillis(),
//  val imageBitmapMutableState: MutableState<ImageBitmap?> = mutableStateOf(null)
  var imageBitmap: ImageBitmap? = null
) {

  init {
    imageBitmap = ImageLoaderExtensions.createImageBitmap(imageUrl)
  }

  override fun toString(): String {
    return "{ imgUrl: $imageUrl, isSelected: ${isSelectedMutableState.value} }"
  }
}