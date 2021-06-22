package com.rsschool.quiz

data class QuizItem(
    val title: String,
    val question: String,
    val first_ans: String,
    val second_ans: String,
    val third_ans: String,
    val fourth_ans: String,
    val fifth_ans: String,
    val style_id: Int,
    val status_bar_color_id: Int
)