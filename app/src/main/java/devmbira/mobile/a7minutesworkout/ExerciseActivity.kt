package devmbira.mobile.a7minutesworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import devmbira.mobile.a7minutesworkout.databinding.ActivityExcerciseBinding
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExcerciseBinding ? = null

    private var restCountDownTimer : CountDownTimer? = null
    private var exerciseCountDownTimer : CountDownTimer? = null
    private var timerDuration:Long = 10000
    private var pauseOffset:Long = 0
    private var pausedCountDown : Boolean? = null
    private var restProgress : Int = 0
    private var exerciseProgress:Int = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this,this)

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.tvTimer?.text = (timerDuration/1000).toString()
        setUpRestTimer()

        binding?.root?.setOnClickListener {
            if(pausedCountDown == false){
                pauseRestTimer()
            }else if(pausedCountDown == true){
                startRestTimer(pauseOffset)
                binding?.tvTitle?.text = getString(R.string.getReady)
            }
        }
    }

    private fun setUpRestTimer(){

        try{
            val soundURI =  Uri.parse("android.resource://devmbira.mobile.a7minutesworkout/" + R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()
        }catch(e:Exception){
            e.printStackTrace()
        }

        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.exerciseFl?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivExerciseImage?.visibility = View.INVISIBLE
        binding?.upcomingExercise?.visibility = View.VISIBLE
        binding?.upcomingExerciseName?.visibility = View.VISIBLE

        val upcomingExercise : ExerciseModel = exerciseList!![currentExercisePosition + 1]
        binding?.upcomingExerciseName?.text = "${upcomingExercise.getName()} for ${upcomingExercise.getDuration()/1000} seconds"

        if(restCountDownTimer != null){
            restCountDownTimer?.cancel()
            restProgress = 0
            pauseOffset = 0
        }

        startRestTimer(pauseOffset)
    }

    private fun startRestTimer(pauseOffSetL:Long){
        binding?.progressBar?.progress = restProgress

        restCountDownTimer = object : CountDownTimer(timerDuration - pauseOffSetL,1000){
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
                currentExercisePosition ++
                pausedCountDown = null
                setUpExerciseTimer()
            }
        }.start()
    }

    private fun setUpExerciseTimer() {
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.exerciseFl?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.ivExerciseImage?.visibility = View.VISIBLE
        binding?.upcomingExercise?.visibility = View.INVISIBLE
        binding?.upcomingExerciseName?.visibility = View.INVISIBLE

        if(exerciseCountDownTimer !== null){
            exerciseCountDownTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivExerciseImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        startExerciseTimer(exerciseList!![currentExercisePosition].getDuration())
    }

    private fun startExerciseTimer(exerciseDuration:Long){
        binding?.exerciseProgressBar?.progress = exerciseProgress

        exerciseCountDownTimer = object : CountDownTimer(exerciseDuration,1000){
            override fun onTick(p0: Long) {
                exerciseProgress ++
                binding?.exerciseProgressBar?.max = (exerciseDuration/1000).toInt()
                binding?.exerciseProgressBar?.progress = (exerciseDuration/1000).toInt() - exerciseProgress
                binding?.tvExerciseTimer?.text = (p0/1000).toString()
            }

            override fun onFinish() {
                binding?.tvExerciseName?.text = getString(R.string.timerDone)
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setUpRestTimer()
                }else{
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congratulations! You have completed the 7 minutes workout.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }

    private fun pauseRestTimer(){
        if(restCountDownTimer!=null){
            restCountDownTimer?.cancel()
            pausedCountDown = true
            binding?.tvTitle?.text = getString(R.string.timerPaused)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restCountDownTimer!=null){
            restCountDownTimer?.cancel()
            pauseOffset = 0
            restProgress = 0
        }
        if(player != null){
            player?.stop()
        }
        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }
        binding = null
    }

    override fun onInit(status:Int){
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not supported")
            }
        }else{
            Log.e("TTS","Initialization failed")
        }
    }

    private fun speakOut(text:String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}