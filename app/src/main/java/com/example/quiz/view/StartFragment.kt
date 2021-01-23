package com.example.quiz.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quiz.R
import com.google.firebase.auth.FirebaseAuth

class StartFragment : Fragment() {

    private lateinit var startProgress: ProgressBar
    private lateinit var startText: TextView

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)

        startProgress = view.findViewById(R.id.start_progress)
        startText = view.findViewById(R.id.start_feedback)

        startText.text = getString(R.string.checking_user_account)
    }

    override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            startText.text = getString(R.string.creating_account)
            firebaseAuth.signInAnonymously().addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    startText.text = getString(R.string.account_created)
                    navController.navigate(R.id.action_startFragment_to_listFragment)
                }
            }

        } else {
            startText.text = getString(R.string.logged_account)
            navController.navigate(R.id.action_startFragment_to_listFragment)
        }
    }
}
