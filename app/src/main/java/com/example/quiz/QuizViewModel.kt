package com.example.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

class QuizViewModel : ViewModel(), FirebaseRepository.OnFirestoreTaskComplete {

    private val listData: MutableLiveData<List<Quiz>> = MutableLiveData()
    private val firebaseRepository = FirebaseRepository(this)

    init {
        firebaseRepository.getQuizData()
    }

    fun getQuizModelData(): LiveData<List<Quiz>> {
        return listData
    }
    override fun quizListDataAdd(quiz: List<Quiz>) {
        listData.value = quiz
    }

    override fun onError(exception: Exception?) {

    }


}
