package devmbira.mobile.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import devmbira.mobile.a7minutesworkout.databinding.ActivityFinishBinding
import nl.dionsegijn.konfetti.xml.KonfettiView

class FinishActivity : AppCompatActivity() {
    private lateinit var viewKonfetti : KonfettiView

    private var binding : ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinish)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarFinish?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.konfettiView?.start(Presets.rain())
    }
}