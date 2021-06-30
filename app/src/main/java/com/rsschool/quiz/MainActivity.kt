package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import com.rsschool.quiz.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), QuizCommunication {
    private val mAnswers = arrayOfNulls<String>(5)
    private lateinit var binding: ActivityMainBinding

    val titles = arrayOf("Question 1", "Question 2", "Question 3", "Question 4", "Question 5")

    val questions = arrayOf("To be, or not to be...", "Tallest skyscraper:",
            "The biggest animal:", "What`s superfluous?", "The Answer to the Ultimate Question of" +
            " Life, the Universe and Everything is:")

    val first_ans = arrayOf("To be", "Shanghai Tower, Shanghai", "Cat", "Discord", "33")
    val second_ans = arrayOf("Not to be", "Burj Khalifa, Dubai", "Elephant", "Slack", "11")
    val third_ans = arrayOf("I don`t know", "Trump International Hotel, Chicago",
            "Kitt`s hog-nosed bat", "Kotlin", "7")
    val fourth_ans = arrayOf("It`s not my problem", "Central Park Tower, New York",
            "Antarctic blue whale", "MicrosoftTeams", "0")
    val fifth_ans = arrayOf("...that is the question", "Eiffel Tower, Paris", "Human",
            "Telegram", "42")

    val fragmentStyles = arrayOf(R.style.Theme_Quiz_First, R.style.Theme_Quiz_Second,
            R.style.Theme_Quiz_Third, R.style.Theme_Quiz_Fourth, R.style.Theme_Quiz_Fifth)

    val statusBarColors = arrayOf(R.color.status_bar_red, R.color.status_bar_purple,
            R.color.status_bar_cyan, R.color.status_bar_green, R.color.yellow_100_dark)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            quest_counter = savedInstanceState.getInt("quest_count")
            checkFirst = savedInstanceState.getBoolean("is_first")
            checkLast = savedInstanceState.getBoolean("is_last")
        }
        else{
            quest_counter = 0
        }
    }

    override fun onStart() {
        super.onStart()
        setUpQuizContent()
        if (quest_counter == -1)
            openResultFragment()
        else
            openQuiz(quest_counter)
    }

    private fun setUpQuizContent() {
        for(ind in 0..4){
            if(ind == 0){ checkFirst = true }
            else if(ind == 4){ checkLast = true }
            else checkFirst = false
            val quizItem = QuizItem(
                    titles[ind],
                    questions[ind],
                    first_ans[ind],
                    second_ans[ind],
                    third_ans[ind],
                    fourth_ans[ind],
                    fifth_ans[ind],
                    fragmentStyles[ind],
                    statusBarColors[ind]
            )
            val quizFragment = QuizFragment.newInstance(quizItem, checkFirst,
                    checkLast, mAnswers, ind)
            fragments.add(quizFragment)
        }
    }

    private fun openQuiz(fragment_ind: Int) {
        if (fragment_ind == 0) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragments[0])
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragments[fragment_ind])
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openResultFragment() {
        fragments.forEach { _ -> supportFragmentManager.popBackStack() }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ResultFragment.newInstance(mAnswers), "result")
            .commit()
    }

    companion object {
        private var quest_counter = 0

        private var checkFirst = false
        private var checkLast = false

        private val fragments = ArrayList<QuizFragment>()
    }

    override fun onBackPressed() {
        fragments.forEach { if ( it.isAdded ) quest_counter-- }
        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("quest_count", quest_counter)
        outState.putBoolean("is_first", checkFirst)
        outState.putBoolean("is_last", checkLast)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        quest_counter = savedInstanceState.getInt("quest_count")
        checkFirst = savedInstanceState.getBoolean("is_first")
        checkLast = savedInstanceState.getBoolean("is_last")
    }

    override fun onNext() {
        quest_counter++
        openQuiz(quest_counter)
    }

    override fun onPrevious() {
        onBackPressed()
    }

    override fun onSubmit() {
        openResultFragment()
        quest_counter = -1
    }

    override fun onRestartClicked() {
        fragments.clear()
        quest_counter = 0
        checkFirst = false
        checkLast = false
        onStart()
    }

    override fun onCloseClicked() {
        this.finishAffinity()
    }

}
