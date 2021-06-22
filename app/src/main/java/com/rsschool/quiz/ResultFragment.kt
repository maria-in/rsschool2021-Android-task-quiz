package com.rsschool.quiz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding

class ResultFragment: Fragment() {

    private  var _binding:FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var listener: QuizCommunication? = null
    private var user_answers: Array<String?>? = null

    private val questions = arrayOf("To be, or not to be...", "Tallest skyscraper:",
        "The biggest animal:", "What`s superfluous?", "The Answer to the Ultimate Question of" +
                " Life, the Universe and Everything is:")
    private val rightAnswers = arrayOf("...that is the question", "Burj Khalifa, Dubai",
            "Antarctic blue whale", "Kotlin", "42")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener = context as QuizCommunication
        user_answers = arguments?.getStringArray(USER_ANSWER_KEY)
        val right_ans_counter = user_answers?.filter { rightAnswers.contains(it) }?.count() ?: 0

        binding.result.text = "Your result: ${right_ans_counter}/5 "

        binding.share.setOnClickListener {
            shareIntent(right_ans_counter)
        }
        binding.restart.setOnClickListener {
            listener?.onRestartClicked()
        }
        binding.close.setOnClickListener {
            listener?.onCloseClicked()
         }
    }

    private fun shareIntent(right_ans_counter: Int) {
        var share_result = "Your result: ${right_ans_counter}/5 \n\n"
        for (index in 0..(rightAnswers.size-1)) {
            share_result += "${index+1}) ${questions[index]}\n Your answer: ${user_answers?.get(index)}\n\n"
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, share_result)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    companion object {
        fun newInstance(user_answers: Array<String?>): ResultFragment {
            return ResultFragment().apply{
                arguments = bundleOf(
                        USER_ANSWER_KEY to user_answers
                )
            }
        }
        private const val USER_ANSWER_KEY = "USER_ANSWERS"
    }
}