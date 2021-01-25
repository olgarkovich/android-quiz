package com.example.quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quiz.R
import com.example.quiz.model.Quiz

class QuizAdapter : RecyclerView.Adapter<QuizAdapter.QuizViewHolder> () {

    private var list: List<Quiz> = emptyList()
    private var listener: OnItemClickListener? = null

    fun setQuizList(quiz: List<Quiz>) {
        list = quiz
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return QuizViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val currentQuiz = list[position]
        holder.name.text = currentQuiz.name
        holder.difficulty.text = currentQuiz.level

        if (currentQuiz.desc.length < 100) {
            holder.desc.text = currentQuiz.desc
        } else {
            holder.desc.text = currentQuiz.desc.substring(100) + "..."
        }

        Glide.with(holder.itemView.context)
            .load(currentQuiz.image)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return if (list.isNotEmpty()) {
            list.size
        } else {
            0
        }
    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.list_title)
        val desc: TextView = itemView.findViewById(R.id.list_desc)
        val difficulty: TextView = itemView.findViewById(R.id.list_difficulty)
        val image: ImageView = itemView.findViewById(R.id.list_image)
        private val button: Button = itemView.findViewById(R.id.list_btn)

        init {
            button.setOnClickListener {
                listener?.let { listener ->
                    val position: Int = adapterPosition
                    if (position in 0..itemCount) {
                        listener.onItemClick(position)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}