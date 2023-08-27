package com.example.habitconnect

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.habitconnect.databinding.ActivityTutorialBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class TutorialActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTutorialBinding
    private val switchImageView: ImageSwitcher? = null
    private val nextButton: MaterialButton? = null

    private val _listImage = intArrayOf(
        R.drawable.tasks, R.drawable.new_task, R.drawable.reminders, R.drawable.new_reminder,
        R.drawable.new_reminder_date, R.drawable.new_reminder_time, R.drawable.new_reminder_save,
        R.drawable.focus, R.drawable.timer_running, R.drawable.timer_pause,
        R.drawable.sign_up, R.drawable.sign_in, R.drawable.community
    )
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding.imageSwitcher.setFactory {
                val imgView = ImageView(applicationContext)
                imgView.scaleType = ImageView.ScaleType.FIT_CENTER
                imgView.setPadding(0, 0, 0, 0)
                imgView
            }
        binding.imageSwitcher?.setImageResource(_listImage[index])

        val imgIn = AnimationUtils.loadAnimation(
            this, android.R.anim.fade_in)
        binding.imageSwitcher.inAnimation = imgIn

        val imgOut = AnimationUtils.loadAnimation(
            this, android.R.anim.fade_out)
        binding.imageSwitcher.outAnimation = imgOut

        binding.prevButton.visibility = View.INVISIBLE
        // previous button
        binding.prevButton.setOnClickListener {
            //se l'indice è a 0 e si preme prev, non va all'ultima slide, ma rimane li
            index = if (index - 1 >= 0) index - 1 else 0
            // quando si preme su prev e si torna alla prima slide, si disabilita il pulsante prev
            if(index == 0) binding.prevButton.visibility = View.INVISIBLE
            // quando si preme su prev e si torna alla penultima slide, si abilita il pulsante next
            if(index == _listImage.size - 2) binding.nextButton.visibility = View.VISIBLE
            binding.imageSwitcher.setImageResource(_listImage[index])
        }

        // next button
        binding.nextButton.setOnClickListener {
            // controlla se il counter è arrivato a fine lista
            index = if (index + 1 < _listImage.size) index +1 else 0
            // quando si preme su next e si passa alla seconda slide, si abilita il pulsante prev
            if(index == 1) binding.prevButton.visibility = View.VISIBLE
            // quando si preme su next e si arriva all'ultima' slide, si disabilita il pulsante next
            if(index == _listImage.size - 1) binding.nextButton.visibility = View.INVISIBLE
            binding.imageSwitcher.setImageResource(_listImage[index])
        }


    }
}