package data

import extensions.RxPubSubList
import io.reactivex.rxjava3.core.Observable
import model.ImageModel
import java.io.File

open class AppRepository(
//  private val mainScope: CoroutineScope = MainScope()
) {
  companion object {
    const val TAG = "AppRepository"
  }

  private val rxImageModelsList: RxPubSubList<ImageModel> = RxPubSubList<ImageModel>(ArrayList())

  val rxObservableImageModelsList: Observable<ArrayList<ImageModel>> get() = rxImageModelsList.rxObservableData


  open fun getSelectedImages(): ArrayList<ImageModel> {
    val mList = ArrayList<ImageModel>()

    for(i in (rxImageModelsList.size - 1) downTo  0) {
      val someImage = rxImageModelsList.get(i)
      if(someImage.isSelectedMutableState.value) {
//        rxImageModelsList.removeAt(i) // inefficient cz too many update ==> too many new ArrayList object allocation!
        someImage.isSelectedMutableState.value = false
        mList.add(index = 0, element = someImage)
      }
    }
    rxImageModelsList.removeAll(mList)
    return mList
  }

  fun saveSelectedImages(selectedImages: ArrayList<ImageModel>) {
    rxImageModelsList.addAll(selectedImages)
//    rxImageModelsList.updateData()
  }

  fun addImage(srcImgUrl: String) {
    // val tmp = ArrayList(imageModelsList)
    rxImageModelsList.add(ImageModel(imageUrl = srcImgUrl))
//    rxImageModelsList.updateData() // onNext(ArrayList(imageModelsList))
  }

  fun addImagesFromSrcDir(srcImages: ArrayList<File>) {
    val mList = srcImages.stream()
      .map { srcImgFile -> ImageModel(imageUrl = srcImgFile.absolutePath) }
      .toList()
    rxImageModelsList.addAll(mList)
  }

  fun printCurrentList(tag: String = "AppRepo") {
    println("$tag : ${rxImageModelsList.getValue()}")
  }

}