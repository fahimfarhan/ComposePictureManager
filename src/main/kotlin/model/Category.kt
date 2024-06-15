package model

import java.util.*
import kotlin.collections.ArrayList

data class Category(
  val uniqueKey: UUID = UUID.randomUUID(),
  val name: String,
  val subCategories: ArrayList<String> = ArrayList()
)
