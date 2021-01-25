package com.example.quiz.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quiz.R
import com.example.quiz.viewmodel.QuizListViewModel

class DetailFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var quizListViewModel: QuizListViewModel
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val args: DetailFragmentArgs by navArgs()
        position = args.position

        Log.d("MyLog", "Position: " + position)
    }
}
