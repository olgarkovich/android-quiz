package com.example.quiz.view

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quiz.*
import com.example.quiz.model.Question
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var timer: CountDownTimer

    private var questions = mutableListOf<Question>()
    private val questionsToAnswer = arrayListOf<Question>()

    private var currentUser = ""
    private var quizId = ""
    private var quizName = ""

    private var answerEnable = false
    private var totalQuestions = 0
    private var currentQuestion = 0
    private var correctCounter = 0
    private var wrongCounter = 0
    private var missedCounter = 0

    private lateinit var closeButton: ImageView
    private lateinit var nextButton: Button
    private lateinit var quizTitle: TextView
    private lateinit var questionsNumber: TextView
    private lateinit var questionTime: TextView
    private lateinit var optionOne: Button
    private lateinit var optionTwo: Button
    private lateinit var optionThree: Button
    private lateinit var feedBack: TextView
    private lateinit var questionText: TextView
    private lateinit var questionProgress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            currentUser = firebaseAuth.currentUser!!.uid
        }

        closeButton = view.findViewById(R.id.quiz_close_btn)
        nextButton = view.findViewById(R.id.quiz_next_btn)
        quizTitle = view.findViewById(R.id.quiz_title)
        questionsNumber = view.findViewById(R.id.quiz_question_number)
        questionTime = view.findViewById(R.id.quiz_question_time)
        optionOne = view.findViewById(R.id.quiz_option_one)
        optionTwo = view.findViewById(R.id.quiz_option_two)
        optionThree = view.findViewById(R.id.quiz_option_three)
        feedBack = view.findViewById(R.id.quiz_question_feedback)
        questionText = view.findViewById(R.id.quiz_question)
        questionProgress = view.findViewById(R.id.quiz_question_progress)

        val args: QuizFragmentArgs by navArgs()
        quizId = args.quizId
        totalQuestions = args.totalQuestions
        quizName = args.quizName

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(QUIZ_LIST)
            .document(quizId)
            .collection(QUESTIONS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    questions = task.result?.toObjects(Question::class.java)!!

                    verifyQuestions()
                } else {
                    quizTitle.text = getString(R.string.error_loading)
                }
            }

        optionOne.setOnClickListener(this)
        optionTwo.setOnClickListener(this)
        optionThree.setOnClickListener(this)

        nextButton.setOnClickListener(this)
    }

    private fun verifyQuestions() {
        if (questions.size > 0) {
            pickQuestion()
            loadUI()
        } else {
            quizTitle.text = getString(R.string.coming_soon)
        }
    }

    private fun loadUI() {
        quizTitle.text = quizName
        questionText.text = getString(R.string.loading_the_first)

        enableOptions()
        loadQuestion(1)
    }

    private fun loadQuestion(questionNumber: Int) {
        questionsNumber.text = questionNumber.toString()
        questionText.text = questionsToAnswer[questionNumber - 1].question
        optionOne.text = questionsToAnswer[questionNumber - 1].option1
        optionTwo.text = questionsToAnswer[questionNumber - 1].option2
        optionThree.text = questionsToAnswer[questionNumber - 1].option3

        answerEnable = true
        currentQuestion = questionNumber

        startTimer(questionNumber)
    }

    private fun startTimer(questionNumber: Int) {
        val answerTime = questionsToAnswer[questionNumber - 1].time.toLong()
        questionTime.text = answerTime.toString()

        questionProgress.visibility = View.VISIBLE

        timer = object : CountDownTimer(answerTime * 1000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                questionTime.text = (millisUntilFinished / 1000).toString()

                val percent = millisUntilFinished / answerTime / 10
                questionProgress.progress = percent.toInt()
            }

            override fun onFinish() {
                answerEnable = false
                feedBack.text = resources.getString(R.string.no_answer)
                missedCounter++
                showNextBtn()
            }
        }
        timer.start()
    }

    private fun enableOptions() {
        optionOne.visibility = View.VISIBLE
        optionTwo.visibility = View.VISIBLE
        optionThree.visibility = View.VISIBLE

        optionOne.isEnabled = true
        optionTwo.isEnabled = true
        optionThree.isEnabled = true

        feedBack.visibility = View.INVISIBLE
        nextButton.visibility = View.INVISIBLE
        nextButton.isEnabled = false
    }

    private fun pickQuestion() {
        for (i in 0 until totalQuestions) {
            val randomIndex = getRandomIndex(0, questions.size)
            questionsToAnswer.add(questions[randomIndex])
            questions.removeAt(randomIndex)
        }
    }

    companion object {
        fun getRandomIndex(min: Int, max: Int): Int {
            return Math.random().toInt() * (max - min) + min
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.quiz_option_one -> answerSelected(optionOne)
                R.id.quiz_option_two -> answerSelected(optionTwo)
                R.id.quiz_option_three -> answerSelected(optionThree)
                R.id.quiz_next_btn -> {
                    if (currentQuestion == totalQuestions) {
                        setResults()
                    } else {
                        currentQuestion++
                        loadQuestion(currentQuestion)
                        resetOptions()
                    }
                }
            }
        }
    }

    private fun setResults() {
        val results: HashMap<String, Any> = hashMapOf()
        results[CORRECT] = correctCounter
        results[WRONG] = wrongCounter
        results[MISSED] = missedCounter

        firebaseFirestore.collection(QUIZ_LIST)
            .document(quizId)
            .collection(RESULTS)
            .document(currentUser)
            .set(results)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val action = QuizFragmentDirections.actionQuizFragmentToResultFragment(
                        quizId
                    )
                    navController.navigate(action)
                } else {
                    quizTitle.text = task.exception?.message
                }
            }
    }

    private fun resetOptions() {
        optionOne.setBackgroundColor(resources.getColor(R.color.colorDark, null))
        optionTwo.setBackgroundColor(resources.getColor(R.color.colorDark, null))
        optionThree.setBackgroundColor(resources.getColor(R.color.colorDark, null))

        optionOne.setTextColor(resources.getColor(R.color.colorLightText, null))
        optionTwo.setTextColor(resources.getColor(R.color.colorLightText, null))
        optionThree.setTextColor(resources.getColor(R.color.colorLightText, null))

        feedBack.visibility = View.INVISIBLE
        nextButton.visibility = View.INVISIBLE
        nextButton.isEnabled = false
    }

    private fun answerSelected(btn: Button) {
        if (answerEnable) {
            verifyAnswer(btn)
        }
    }

    private fun verifyAnswer(btn: Button) {
        if (questionsToAnswer[currentQuestion - 1].answer == btn.text) {
            correctCounter++
            feedBack.text = getString(R.string.correct)

            btn.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
            btn.setTextColor(resources.getColor(R.color.colorDark, null))
        } else {
            wrongCounter++
            feedBack.text =
                getString(R.string.wrong_right, questionsToAnswer[currentQuestion - 1].answer)

            btn.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
            btn.setTextColor(resources.getColor(R.color.colorDark, null))
        }
        answerEnable = false
        timer.cancel()
        showNextBtn()
    }


    private fun showNextBtn() {
        if (currentQuestion - 1 == totalQuestions) {
            nextButton.text = getString(R.string.show_results)
        }

        feedBack.visibility = View.VISIBLE
        nextButton.visibility = View.VISIBLE
        nextButton.isEnabled = true
    }
}
