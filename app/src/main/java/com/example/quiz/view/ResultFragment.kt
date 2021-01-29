package com.example.quiz.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quiz.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ResultFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var quizId = ""
    private var currentUser = ""

    private lateinit var correctCounter: TextView
    private lateinit var wrongCounter: TextView
    private lateinit var missedCounter: TextView

    private lateinit var resultPercent: TextView
    private lateinit var progress: ProgressBar
    private lateinit var homeBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            currentUser = firebaseAuth.currentUser!!.uid
        } else {

        }

        firebaseFirestore = FirebaseFirestore.getInstance()
        val args: ResultFragmentArgs by navArgs()
        quizId = args.quizId

        correctCounter = view.findViewById(R.id.results_correct_text)
        wrongCounter = view.findViewById(R.id.results_wrong_text)
        missedCounter = view.findViewById(R.id.results_missed_text)

        resultPercent = view.findViewById(R.id.results_percent)
        progress = view.findViewById(R.id.results_progress)
        homeBtn = view.findViewById(R.id.results_home_btn)

        homeBtn.setOnClickListener {
            navController.navigate(R.id.action_resultFragment_to_listFragment)
        }

        firebaseFirestore.collection(QUIZ_LIST)
            .document(quizId)
            .collection(RESULTS)
            .document(currentUser)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val results: DocumentSnapshot = task.result!!

                    val right = results.getLong(CORRECT)!!
                    val wrong = results.getLong(WRONG)!!
                    val no = results.getLong(MISSED)!!
                    correctCounter.text = right.toString()
                    wrongCounter.text = wrong.toString()
                    missedCounter.text = no.toString()

                    val percent = right.toFloat() / (right + wrong + no) * 100
                    resultPercent.text = getString(R.string.percent, percent.toInt().toString())
                    progress.progress = percent.toInt()
                }
            }
    }
}
