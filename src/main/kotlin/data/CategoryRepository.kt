package data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import extensions.RxPubSubList
import io.reactivex.rxjava3.core.Observable
import model.Category
import java.io.File
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CategoryRepository {
  companion object {
    const val TAG = "CategoryRepo"
    const val FILE_NAME = ".kmm-categories.txt"
  }

  // variables
  private val mFile by lazy { File(FILE_NAME) }

  private val gson  = Gson()
  private val mTypeToken = object: TypeToken<ArrayList<Category>>(){}.type
  val rxListOfCategories: RxPubSubList<Category> by lazy { RxPubSubList(readCategories()) }
  val rxObservableListOfCategories: Observable<ArrayList<Category>> get() = rxListOfCategories.rxObservableData

  private val mExecutor by lazy { Executors.newCachedThreadPool() }

  // constructor
  init {
    ensureFileExists()

    initObservers()
  }

  // private methods
  private fun initObservers() {
    rxObservableListOfCategories.subscribe { latestList ->
      writeCategoriesAsync()
    }
  }

  private fun fromCategoriesToJson(someList: ArrayList<Category>): String = gson.toJson(someList)

  private fun fromJsonToCategories(someJson: String): ArrayList<Category> = gson.fromJson(someJson, mTypeToken)

  private fun ensureFileExists() {
    if(!mFile.exists()) {
      mFile.createNewFile()
    }
    println("mFile.absolutePath: ${mFile.absolutePath}")
  }

  private fun getJsonFromFile(): String? {
    //    val jsonBuilder = StringBuilder()
    //    val sc = Scanner(mFile)
    //    while (sc.hasNextLine()) {
    //      jsonBuilder.append(sc.nextLine())
    //    }
    //    val json = jsonBuilder.toString()
    val json: String? = mFile.readText()
    return json
  }

  private fun saveJsonToFile(someJson: String) {
    mFile.writeText(someJson)
  }

  private fun writeCategoriesAsync() {
    // WARNING! DO NOT INVOKE ANOTHER rx.onNext() to prevent infinite loop!
    mExecutor.execute {
      val mJson = fromCategoriesToJson(rxListOfCategories.getValue())
      saveJsonToFile(mJson)
    }
  }

  private fun readCategories(): ArrayList<Category> {
//    mExecutor.execute {
      val output = try {
        val json = getJsonFromFile()?: ArrayList<Category>()
        println("json = $json")
        val tmp = fromJsonToCategories(json.toString())
        tmp
      } catch (x: Exception) {
        x.printStackTrace()
        ArrayList()
      }

    println("readCategories -> output: $output")
    return output
//    }
  }

  // public methods
  fun addCategory(categoryName: String) {
    val category = Category(name = categoryName)
    rxListOfCategories.add(category)
  }

  fun addSubCategory(categoryReference: Category, subCategory: String) {
    val mList = rxListOfCategories.getValue()
    if(!mList.contains(categoryReference)) {
      println("Category reference does not exist in currentList! catRef: $categoryReference, currentList: $mList")
    }
    if(categoryReference.subCategories.contains(subCategory)) {
      println("Same subCategory already exists. return...")
      return
    }
    categoryReference.subCategories.add(subCategory)
    rxListOfCategories.updateData(mList)
  }

  fun removeCategory(category: Category) {
    rxListOfCategories.remove(category)
  }

  fun removeSubCategory(categoryReference: Category, subCategory: String) {
    categoryReference.subCategories.remove(subCategory)
    rxListOfCategories.updateData(rxListOfCategories.getValue())
  }

}