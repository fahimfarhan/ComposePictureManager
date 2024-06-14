package model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.awt.print.Book

data class ImageModel (
  val imageUrl: String,
  var isSelectedMutableState: MutableState<Boolean> = mutableStateOf(false),
  val key: Long = System.currentTimeMillis()
) {
//  var isSelected get() = isSelectedMutableState.value
//    set(value) {
//      isSelectedMutableState.value = value
//    }

  override fun toString(): String {
    return "{ imgUrl: $imageUrl, isSelected: ${isSelectedMutableState.value} }"
  }
}