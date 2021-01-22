package com.example.quiz

import com.google.firebase.firestore.DocumentId

class Quiz {

    @DocumentId
    val id: String = ""
    val name: String = ""
    val desc: String = ""
    val image: String = ""
    val level: String = ""
    val visibility: String = ""
    val questions: Int = 0
}