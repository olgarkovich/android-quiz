package com.example.quiz.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.adapter.QuizAdapter
import com.example.quiz.R
import com.example.quiz.viewmodel.QuizListViewModel

class ListFragment : Fragment() {

    private lateinit var list: RecyclerView
    private lateinit var quizListViewModel: QuizListViewModel
    private lateinit var adapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.list)
        adapter = QuizAdapter()

        list.layoutManager = LinearLayoutManager(requireContext())
        list.setHasFixedSize(true)
        list.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        quizListViewModel = ViewModelProvider(requireActivity()).get(QuizListViewModel::class.java)
        quizListViewModel.getQuizListData().observe(viewLifecycleOwner,
            { quiz -> adapter.setQuizList(quiz)
            adapter.notifyDataSetChanged()})
    }

}
