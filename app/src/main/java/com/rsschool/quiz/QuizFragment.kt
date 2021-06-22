package com.rsschool.quiz

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding


class QuizFragment: Fragment() {

    var quiz_fragment: QuizItem? = null
    private var isFirst: Boolean? = null
    private var isLast: Boolean? = null
    private var answers: Array<String?> = arrayOf()
    private var listener: QuizCommunication? = null

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpTheme()
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = arguments?.getBoolean(IS_FIRST_KEY)
        isLast = arguments?.getBoolean(IS_LAST_KEY)
        quizPosition = arguments?.getInt(QUIZ_POSITION_KEY)
        answers = arguments?.getStringArray(QUIZ_ANSWERS_KEY)!!

        listener = context as QuizCommunication

        setUpActionBar()
        setUpNavigateButtons()

        binding.question.text = quiz_fragment?.question
        binding.optionOne.text = quiz_fragment?.first_ans
        binding.optionTwo.text = quiz_fragment?.second_ans
        binding.optionThree.text = quiz_fragment?.third_ans
        binding.optionFour.text = quiz_fragment?.fourth_ans
        binding.optionFive.text = quiz_fragment?.fifth_ans

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            answers[quizPosition ?: 0] = view.findViewById<RadioButton>(checkedId).text.toString()
            binding.nextButton.isEnabled = true
        }

    }

    private fun setUpNavigateButtons() {
        if (isFirst == true) {
            binding.previousButton.isEnabled = false
        } else {
            binding.previousButton.setOnClickListener {
                listener?.onPrevious()
            }
        }
        if ( isLast == true ) {
            binding.nextButton.text = "Submit"
            binding.nextButton.setOnClickListener {
                listener?.onSubmit()
            }
        } else {
            binding.nextButton.setOnClickListener {
                listener?.onNext()
            }
        }
        binding.nextButton.isEnabled = false
    }

    private fun setUpActionBar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setHomeButtonEnabled(true)
            }
            binding.toolbar.setNavigationOnClickListener {
                listener?.onPrevious()
            }
            supportActionBar?.title = quiz_fragment?.title
        }
    }

    private fun setUpTheme() {
        activity?.theme?.applyStyle(quiz_fragment?.style_id ?: 0, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.statusBarColor = activity?.getColor(quiz_fragment?.status_bar_color_id ?: 0) ?: 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        fun newInstance(content: QuizItem, isFirst: Boolean, isLast: Boolean,
                        answers: Array<String?>, quizPosition: Int): QuizFragment {
            return QuizFragment().apply{
                arguments = bundleOf(
                        IS_FIRST_KEY to isFirst,
                        IS_LAST_KEY to isLast,
                        QUIZ_POSITION_KEY to quizPosition,
                        QUIZ_ANSWERS_KEY to answers
                )
                this.quiz_fragment = content
            }
        }
        private var quizPosition: Int? = null
        private const val IS_FIRST_KEY = "IS_FIRST"
        private const val IS_LAST_KEY = "IS_LAST"
        private const val QUIZ_POSITION_KEY = "QUIZ_POSITION"
        private const val QUIZ_ANSWERS_KEY = "QUIZ_ANSWERS"
    }
}