package com.example.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuizAdapter : RecyclerView.Adapter<QuizAdapter.ViewHolder> () {

    private var list: List<Quiz> = emptyList()

    fun setQuizList(quiz: List<Quiz>) {
        list = quiz
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    override fun getItemCount(): Int {
        return if (list.isNotEmpty()) {
            list.size
        } else {
            0
        }
    }

    private fun getItem(position: Int) : Quiz = list[position]

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.list_title)
        private val desc: TextView = itemView.findViewById(R.id.list_desc)
        private val difficulty: TextView = itemView.findViewById(R.id.list_difficulty)
        private val image: ImageView = itemView.findViewById(R.id.list_image)
        private val button: Button = itemView.findViewById(R.id.list_btn)

        fun bind(quiz: Quiz) {
            name.text = quiz.name
            desc.text = quiz.desc
            difficulty.text = quiz.level
        }
    }
}