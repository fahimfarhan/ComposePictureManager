package data

import androidx.compose.runtime.MutableState
import extensions.RxPubSub
import extensions.RxPubSubList
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

  private val rxImageModelsList: RxPubSubList<ImageModel> = RxPubSubList<ImageModel>(ArrayList())

  val rxObservableImageModelsList: Observable<ArrayList<ImageModel>> get() = rxImageModelsList.rxObservableData


  open fun getSelectedImages(): ArrayList<ImageModel> {
    val mList = ArrayList<ImageModel>()

    for(i in (rxImageModelsList.size - 1) downTo  0) {
      val someImage = rxImageModelsList.get(i)
      if(someImage.isSelected) {
        rxImageModelsList.removeAt(i) // inefficient cz too many update ==> too many new ArrayList object allocation!
        someImage.isSelected = false
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

  fun addImages(srcImgUrl: String) {
    // val tmp = ArrayList(imageModelsList)
    rxImageModelsList.add(ImageModel(imageUrl = srcImgUrl))
//    rxImageModelsList.updateData() // onNext(ArrayList(imageModelsList))
  }

  fun addImagesFromSrcDir(srcDir: String) {

  }

}