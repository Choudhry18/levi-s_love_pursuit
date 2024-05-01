package models

import collection.mutable

object AuthenModel {
    private val users = mutable.Map[String, String]("Kevin" -> "pass", "Levi" -> "pass")
    private val tasks = mutable.Map[String, List[String]]("Kevin" -> List("hello", "hi", "There"))

    def validateUser(username: String, password: String): Boolean = {
        users.get(username).map(_==password).getOrElse(false)
    }

    def createUser(username: String, password: String): Boolean = {
        if(users.contains(username)) false else{
            users(username) = password
            true
        }
    }
}