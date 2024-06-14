package extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.io.File
import java.net.URL
import javax.imageio.ImageIO


object ImageLoaderExtensions {

  fun isFile(imgUrl: String): Boolean {
    val file = File(imgUrl)
    if(!file.exists()) {
      return false
    }
    if(!file.isFile) {
      return false
    }
    return true
  }

  /* Returns true if url is valid */
  fun isValidUrl(url: String): Boolean {
    /* Try creating a valid URL */
    try {
      URL(url).toURI()
      return true
    } // If there was an Exception
    // while creating URL object

    catch (e: java.lang.Exception) {
      return false
    }
  }

  fun isFileOrIsValidUrl(imgUrl: String): Boolean = (isFile(imgUrl) || isValidUrl(imgUrl))

  private fun createImageBitmapFromLocalFile(imgUrl: String): ImageBitmap? {
    try {
      val file = File(imgUrl)
      val mPicture = ImageIO.read(file)
      return mPicture.toComposeImageBitmap()
    } catch (x: Exception) {
      x.printStackTrace()
      return null
    }
  }

  private fun createImageBitmapFromRemoteUrl(imgUrl: String): ImageBitmap? {
     return try {
       ImageIO.read(URL(imgUrl)).toComposeImageBitmap()
     } catch (x: Exception) {
       x.printStackTrace()
       null
     }
  }

   fun createImageBitmap(imgUrl: String): ImageBitmap? {
     return try {
       if(isFile(imgUrl)) {
         createImageBitmapFromLocalFile(imgUrl)
       } else if(isValidUrl(imgUrl)) {
         createImageBitmapFromRemoteUrl(imgUrl)
       } else {
         null
       }
     } catch (x: Exception) {
       x.printStackTrace()
       null
     }
  }

  val defaultPlaceHolderImageBitmap = ImageBitmap(100, 60)
}