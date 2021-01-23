package com.example.quiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quiz.data.FirebaseRepository
import com.example.quiz.model.Quiz
import java.lang.Exception

class QuizListViewModel : ViewModel(), FirebaseRepository.OnFirestoreTaskComplete {

    private val listData: MutableLiveData<List<Quiz>> = MutableLiveData()
    private val firebaseRepository = FirebaseRepository(this)

    init {
        firebaseRepository.getQuizData()
    }

    fun getQuizListData(): LiveData<List<Quiz>> {
        return listData
    }
    override fun quizListDataAdd(quiz: List<Quiz>) {
        listData.value = quiz
    }

    override fun onError(exception: Exception?) {

    }
}
