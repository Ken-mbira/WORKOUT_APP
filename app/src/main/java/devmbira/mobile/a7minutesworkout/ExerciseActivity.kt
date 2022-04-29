package devmbira.mobile.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import devmbira.mobile.a7minutesworkout.databinding.ActivityExcerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExcerciseBinding ? = null

    private var countDownTimer : CountDownTimer? = null
    private var timerDuration:Long = 10000
    private var pauseOffset:Long = 0
    private var pausedCountDown : Boolean? = null

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
            }
        }
    }

    private fun startTimer(pauseOffSetL:Long){
        countDownTimer = object : CountDownTimer(timerDuration - pauseOffSetL,1000){
            override fun onTick(p0: Long) {
                pauseOffset = timerDuration - p0
                binding?.tvTimer?.text = (p0/1000).toString()
                pausedCountDown = false
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,"The timer finished",Toast.LENGTH_LONG).show()
                pausedCountDown = null
            }
        }.start()
    }

    private fun pauseTimer(){
        if(countDownTimer!=null){
            countDownTimer?.cancel()
            pausedCountDown = true
        }
    }

//    private fun resetTimer(){
//        if(countDownTimer!=null){
//            countDownTimer?.cancel()
//            binding?.tvTimer?.text = (timerDuration / 1000).toString()
//            countDownTimer = null
//            pauseOffset = 0
//        }
//    }
}