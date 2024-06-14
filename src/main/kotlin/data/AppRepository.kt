package data

import model.ImageModel

open class AppRepository {
  companion object {
    const val TAG = "AppRepository"
  }

  val imageModelsList: ArrayList<ImageModel> = ArrayList()

  open fun getSelectedImages(): ArrayList<ImageModel> {
    val mList = ArrayList<ImageModel>()

    for(i in (imageModelsList.size - 1) downTo  0) {
      val someImage = imageModelsList[i]
      if(someImage.isSelected) {
        imageModelsList.removeAt(i)
        someImage.isSelected = false
        mList.add(index = 0, element = someImage)
      }
    }

    return mList
  }

  fun saveSelectedImages(selectedImages: ArrayList<ImageModel>) {
    imageModelsList.addAll(selectedImages)
  }

  fun addImages(srcImgUrl: String) {
    imageModelsList.add(ImageModel(imageUrl = srcImgUrl))
  }

  fun addImagesFromSrcDir(srcDir: String) {

  }

}