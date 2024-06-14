package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import extensions.ImageLoaderExtensions
import extensions.ImageLoaderExtensions.createImageBitmapAsync
import java.awt.print.Book
import java.util.UUID

data class ImageModel (
  val imageUrl: String,
  val isSelectedMutableState: MutableState<Boolean> = mutableStateOf(false),
  val key: UUID = UUID.randomUUID(),
//  val imageBitmapMutableState: MutableState<ImageBitmap?> = mutableStateOf(null)
//  var imageBitmap: ImageBitmap? = null
  val stateImageBitmap: MutableState<ImageBitmap?> = mutableStateOf(null)
) {

  init {
    createImageBitmapAsync(imageUrl, stateImageBitmap)
//    imageBitmap = ImageLoaderExtensions.createImageBitmap(imageUrl)
  }

  override fun toString(): String {
    return "{ imgUrl: $imageUrl, isSelected: ${isSelectedMutableState.value} }"
  }
}