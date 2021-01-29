package com.example.quiz.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.adapter.QuizAdapter
import com.example.quiz.R
import com.example.quiz.viewmodel.QuizListViewModel

class ListFragment : Fragment() {

    private lateinit var quizList: RecyclerView
    private lateinit var adapter: QuizAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)
        progressBar = view.findViewById(R.id.progress_list)
        quizList = view.findViewById(R.id.list)
        quizList.layoutManager = LinearLayoutManager(requireContext())
        adapter = QuizAdapter()

        quizList.setHasFixedSize(true)
        quizList.adapter = adapter

        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        adapter.setOnItemClickListener(object :
            QuizAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val action = ListFragmentDirections.actionListFragmentToDetailFragment(position)
                navController.navigate(action)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val quizListViewModel = ViewModelProvider(requireActivity()).get(QuizListViewModel::class.java)
        quizListViewModel.getQuizListData().observe(viewLifecycleOwner, { quiz ->
                quizList.startAnimation(fadeIn)
                progressBar.startAnimation(fadeOut)

                adapter.setQuizList(quiz)
                adapter.notifyDataSetChanged()
        })
    }
}
