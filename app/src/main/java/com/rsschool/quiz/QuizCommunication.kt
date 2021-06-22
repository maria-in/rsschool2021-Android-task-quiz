package com.rsschool.quiz

interface QuizCommunication {
    fun onNext()
    fun onSubmit()
    fun onPrevious()
    fun onRestartClicked()
    fun onCloseClicked()
}