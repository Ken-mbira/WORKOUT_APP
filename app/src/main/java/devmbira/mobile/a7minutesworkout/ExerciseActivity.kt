package devmbira.mobile.a7minutesworkout

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import devmbira.mobile.a7minutesworkout.databinding.ActivityExcerciseBinding
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExcerciseBinding ? = null

    private var restCountDownTimer : CountDownTimer? = null
    private var exerciseCountDownTimer : CountDownTimer? = null
    private var timerDuration:Long = 10
    private var pauseOffset:Long = 0
    private var pausedCountDown : Boolean? = null
    private var restProgress : Int = 0
    private var exerciseProgress:Int = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null

    private var exerciseAdapter : ExerciseAdapterStatus? =  null

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

        setUpExerciseStatusRecyclerView()
    }

    private fun setUpExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseAdapterStatus(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestTimer(){

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

        restCountDownTimer = object : CountDownTimer((timerDuration * 1000) - pauseOffSetL,1000){
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
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
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
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestTimer()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
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

    private fun backPressDialog(){
        val builder : AlertDialog.Builder? = let {
            AlertDialog.Builder(it)
        }

        builder?.setMessage("Are you sure you want to quit the current exercise?")
            ?.setTitle("Quit Session")
            ?.setPositiveButton(
                R.string.back_to_main,
                DialogInterface.OnClickListener{
                    dialog,_ ->
                    finish()
                    dialog.dismiss()
                }
            )
            ?.setNegativeButton(
                R.string.stay_in_session,
                DialogInterface.OnClickListener{
                        dialog,_ ->
                    dialog.dismiss()
                }
            )

        val alertDialog:AlertDialog? = builder?.create()
        alertDialog?.setCancelable(true)
        alertDialog?.show()
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

    override fun onBackPressed() {
        backPressDialog()
    }

    private fun speakOut(text:String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}