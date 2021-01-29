package com.example.quiz.data

import com.example.quiz.model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class FirebaseRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {

    private val firebase = FirebaseFirestore.getInstance()
    private val quizRef = firebase.collection("QuizList").whereEqualTo("visibility", "public")

    fun getQuizData() {
        quizRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { onFirestoreTaskComplete.quizListDataAdd(it.toObjects(Quiz::class.java)) }
            } else {
                onFirestoreTaskComplete.onError(task.exception)
            }
        }
    }

    interface OnFirestoreTaskComplete {
        fun quizListDataAdd(quiz: List<Quiz>)
        fun onError(exception: Exception?)
    }
}