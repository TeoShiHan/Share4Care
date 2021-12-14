package com.example.share4care.loginAndRegisterForm
import com.google.gson.Gson
import java.lang.reflect.Type

data class User(
    val primary_key: String,
    val userName : String,
    val password : MutableList<String>
)


class GsonCustom {
    val gson = Gson()

    fun <T : Any> convertObjectToJSON(target_object: T): String {
        return gson.toJson(target_object)
    }

    inline fun <reified T : Any>convertJsonToObject(json: String): T{
        return gson.fromJson(json, T::class.java)
    }
}
fun main() {

    val gson = GsonCustom()
    val gson_g = Gson()

    val list = mutableListOf<String>("first", "second", "third", "fourth", "fifth")

    val shihan = User("A001","Teo Shi Han", list)
    println(shihan.primary_key)
    println(shihan.userName)

    val json = gson.convertObjectToJSON(shihan)

    val objct = gson.convertJsonToObject<User>(json)

    println(objct.password)

    println(objct)
}


