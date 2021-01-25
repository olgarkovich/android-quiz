package com.example.quiz.view

import android.icu.text.CaseMap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.quiz.R
import com.example.quiz.viewmodel.QuizListViewModel

class DetailFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var quizListViewModel: QuizListViewModel
    private var position = 0

    private lateinit var detailImage: ImageView
    private lateinit var detailTitle: TextView
    private lateinit var detailDesc: TextView
    private lateinit var detailDiff: TextView
    private lateinit var detailQuestion: TextView

    private lateinit var button: Button

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

        detailImage = view.findViewById(R.id.details_image)
        detailTitle = view.findViewById(R.id.details_title)
        detailDesc = view.findViewById(R.id.details_desc)
        detailDiff = view.findViewById(R.id.details_difficulty_text)
        detailQuestion = view.findViewById(R.id.details_questions_text)
        button = view.findViewById(R.id.details_start_btn)

        button.setOnClickListener(this)

        val args: DetailFragmentArgs by navArgs()
        position = args.position
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        quizListViewModel = ViewModelProvider(requireActivity()).get(QuizListViewModel::class.java)
        quizListViewModel.getQuizListData().observe(viewLifecycleOwner, { quizList ->
            val quiz = quizList[position]

            Glide.with(requireContext())
                .load(quiz.image)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(detailImage)

            detailTitle.text = quiz.name
            detailDesc.text = quiz.desc
            detailDiff.text = quiz.level
            detailQuestion.text = quiz.questions.toString()
        })
    }

    override fun onClick(v: View) {
        val action = DetailFragmentDirections.actionDetailFragmentToQuizFragment(position)
        navController.navigate(action)
    }
}
