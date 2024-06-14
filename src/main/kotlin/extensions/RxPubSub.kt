package extensions

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import model.ImageModel

open class RxPubSub<T>(protected var data: T) {
  val rxPubSubData: PublishSubject<T> = PublishSubject.create()
  val rxObservableData get() = rxPubSubData.hide()

  fun getValue(): T = data

  open fun updateData(someData: T) {
    this.data = someData
    this.rxPubSubData.onNext(data)
  }
}

open class RxPubSubList<T>(data: ArrayList<T>): RxPubSub<ArrayList<T>>(data) {
  override fun updateData(someData: ArrayList<T>) {
    val newData = ArrayList(someData)
    super.updateData(ArrayList(someData))
  }

  fun removeAt(i: Int) {
    val tmp = ArrayList(super.data)
    tmp.removeAt(i)
    updateData(tmp)
  }

  fun removeAll(someSubListToRemove: ArrayList<T>) {
    val tmp = ArrayList(super.data)
    tmp.removeAll(someSubListToRemove.toSet())
    updateData(tmp)
  }

  fun add(someObject: T) {
    val tmp = ArrayList(super.data)
    tmp.add(someObject)
    updateData(tmp)
  }

  fun addAll(someList: ArrayList<T>) {
    val tmp = ArrayList(super.data)
    tmp.addAll(someList)
    updateData(tmp)
  }

  val size get() = super.data.size

  fun get(i: Int) = super.data[i]
}