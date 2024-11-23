package com.example.datetimecarddata

import java.io.Serializable

class Person (
    val name: String?,
    val surName: String?,
    val phone: String?,
    val birthday: String?,
    val image: String?
) : Serializable