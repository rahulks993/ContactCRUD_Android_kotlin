package com.example.contactcrud

data class Model(val name: String, val number: String, val email: String, var imgPth: String)

object Supplier {

    val contactDtls = mutableListOf(
        Model("Rahul", "9986182012", "rahulks993@gmail.com", ""),
        Model("Santhu", "8895624562", "santhucm@gmail.com", ""),
        Model("Aman", "9778175834", "amamde@gmail.com", ""),
        Model("Sandeep", "7377169824", "sandy@gmail.com", ""),
        Model("Manju", "73772156441", "manju@gmail.com", ""),
        Model("Shreyas", "7377565643", "shreyas@gmail.com", ""),
        Model("Jitin", "84564145484", "jitin@gmail.com", ""),
        Model("Satya", "64656600539", "pandua@gmail.com", ""),
        Model("Akash", "82165131313", "daanav@gmail.com", ""),
        Model("Sourav", "42654465115", "sourav@gmail.com", "")
    )
}