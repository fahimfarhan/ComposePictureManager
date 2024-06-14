package data

import androidx.compose.runtime.MutableState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.kotlin.withLatestFrom
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import model.ImageModel

open class AppRepository(
//  private val mainScope: CoroutineScope = MainScope()
) {
  companion object {
    const val TAG = "AppRepository"
  }



//  val stateFLowOfImageModelsList: MutableStateFlow<ArrayList<ImageModel>> = MutableStateFlow(ArrayList())
  val imageModelsList: ArrayList<ImageModel> = ArrayList()
  private val rxImageModelsList: PublishSubject<ArrayList<ImageModel>> = PublishSubject.create() // Subject.fromIterable(imageModelsList)
  val rxObservableImageModelsList get() = rxImageModelsList.hide()

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
    // val tmp = ArrayList(imageModelsList)
    imageModelsList.add(ImageModel(imageUrl = srcImgUrl))
    rxImageModelsList.onNext(ArrayList(imageModelsList))
  }

  fun addImagesFromSrcDir(srcDir: String) {

  }

}