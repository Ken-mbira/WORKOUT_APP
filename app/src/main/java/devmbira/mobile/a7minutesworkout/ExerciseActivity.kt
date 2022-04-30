package devmbira.mobile.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Toast
import devmbira.mobile.a7minutesworkout.databinding.ActivityExcerciseBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExcerciseBinding ? = null

    private var countDownTimer : CountDownTimer? = null
    private var exerciseCountDownTimer : CountDownTimer? = null
    private var timerDuration:Long = 10000
    private var pauseOffset:Long = 0
    private var pausedCountDown : Boolean? = null
    private var restProgress : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.tvTimer?.text = (timerDuration/1000).toString()
        startTimer(pauseOffset)

        binding?.root?.setOnClickListener {
            if(pausedCountDown == false){
                pauseTimer()
            }else if(pausedCountDown == true){
                startTimer(pauseOffset)
                binding?.tvTitle?.text = getString(R.string.getReady)
            }
        }
    }

    private fun startTimer(pauseOffSetL:Long){
        binding?.progressBar?.progress = restProgress

        countDownTimer = object : CountDownTimer(timerDuration - pauseOffSetL,1000){
            override fun onTick(p0: Long) {
                restProgress ++
                binding?.progressBar?.progress = 10 - restProgress
                pauseOffset = timerDuration - p0
                binding?.tvTimer?.text = (p0/1000).toString()
                pausedCountDown = false
            }

            override fun onFinish() {
                pauseOffset = 0
                restProgress = 0
                binding?.flProgressBar?.visibility = View.INVISIBLE
                binding?.exerciseFl?.visibility = View.VISIBLE
                startExerciseTimer(pauseOffset)
            }
        }.start()
    }
    private fun startExerciseTimer(pauseOffSetL:Long){
        binding?.exerciseProgressBar?.progress = restProgress
        binding?.tvTitle?.text = "JUMPING JACKS"

        exerciseCountDownTimer = object : CountDownTimer(timerDuration - pauseOffSetL,1000){
            override fun onTick(p0: Long) {
                restProgress ++
                binding?.exerciseProgressBar?.progress = 10 - restProgress
                pauseOffset = timerDuration - p0
                binding?.tvExerciseTimer?.text = (p0/1000).toString()
            }

            override fun onFinish() {
                binding?.tvTitle?.text = getString(R.string.timerDone)
            }
        }.start()
    }

    private fun pauseTimer(){
        if(countDownTimer!=null){
            countDownTimer?.cancel()
            pausedCountDown = true
            binding?.tvTitle?.text = getString(R.string.timerPaused)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(countDownTimer!=null){
            countDownTimer?.cancel()
            pauseOffset = 0
            restProgress = 0
        }
        binding = null
    }
}