package devmbira.mobile.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import devmbira.mobile.a7minutesworkout.databinding.ActivityBmiBinding

class BMI : AppCompatActivity() {

    private var binding : ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmi)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmi?.setNavigationOnClickListener {
            onBackPressed()
        }


    }
}