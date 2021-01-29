package com.example.quiz.view

import android.os.Bundle
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
import com.example.quiz.*
import com.example.quiz.viewmodel.QuizListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DetailFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var position = 0
    private var quizId = ""
    private var quizName = ""
    private var totalQuestion = 0

    private lateinit var detailImage: ImageView
    private lateinit var detailTitle: TextView
    private lateinit var detailDesc: TextView
    private lateinit var detailDiff: TextView
    private lateinit var detailQuestion: TextView
    private lateinit var detailScore: TextView
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
        detailScore = view.findViewById(R.id.details_score_text)
        button = view.findViewById(R.id.details_start_btn)

        button.setOnClickListener(this)

        val args: DetailFragmentArgs by navArgs()
        position = args.position
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val quizListViewModel =
            ViewModelProvider(requireActivity()).get(QuizListViewModel::class.java)
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

            quizId = quizList[position].id
            totalQuestion = quizList[position].questions
            quizName = quizList[position].name

            firebaseAuth = FirebaseAuth.getInstance()
            firebaseFirestore = FirebaseFirestore.getInstance()

            loadResultData()
        })
    }

    private fun loadResultData() {
        firebaseAuth.currentUser?.let {
            firebaseFirestore.collection(QUIZ_LIST)
                .document(quizId)
                .collection(RESULTS)
                .document(it.uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val results = task.result

                        getResults(results)
                    }
                }
        }
    }

    private fun getResults(results: DocumentSnapshot?) {
        if (results != null && results.exists()) {

            val right = results.getLong(CORRECT)!!
            val wrong = results.getLong(WRONG)!!
            val no = results.getLong(MISSED)!!

            val percent = right.toFloat() / (right + wrong + no) * 100
            detailScore.text = getString(R.string.percent, percent.toInt().toString())
        }
    }

    override fun onClick(v: View) {
        val action = DetailFragmentDirections.actionDetailFragmentToQuizFragment(
            quizId,
            totalQuestion,
            quizName
        )
        navController.navigate(action)
    }
}
