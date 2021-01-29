package com.example.quiz.model

import com.google.firebase.firestore.DocumentId

class Question {

    @DocumentId
    val id: String = ""
    val answer: String = ""
    val option1: String = ""
    val option2: String = ""
    val option3: String = ""
    val question: String = ""
    val time: Int = 0
}