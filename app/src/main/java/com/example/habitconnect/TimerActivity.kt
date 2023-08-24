package com.example.habitconnect

import android.os.Bundle
import android.view.Menu
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.habitconnect.databinding.ActivityTimerBinding
import com.google.android.material.snackbar.Snackbar
import com.example.habitconnect.util.PrefUtil

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding

    // proprietà del timer
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var secondsRemaining: Long = 0

    // classe che definisce gli stati del timer
    enum class TimerState{
        Stopped, Paused, Running
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // inserisco l'icona e titolo del timer
        supportActionBar?.setIcon(R.drawable.ic_twotone_offline_bolt_24)
        supportActionBar?.title = "      Focus"

        binding.fabStart.setOnClickListener{v ->
            startTimer()
            timerState =  TimerState.Running
            updateButtons() // update degli altri bottoni es: timer Running, lo start button è disabilitato
        }

        binding.fabPause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        binding.fabStop.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        //TODO: remove background timer, hide notification
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()
            //TODO: start background timer and show notification
        }
        else if (timerState == TimerState.Paused){
            //TODO: show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    /* inizializza il timer con lo stato e il tempo salvati nelle preferences
    * se lo stato è stopped lo imposta con la durata impostata la volta prima, salvata nelle
    * preferences, altrimenti imposta la durata che mancava dalla scorsa volta, anch'essa salvata
    * nelle preferences */
    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this)

        setNewTimerLength()
        /* non cambiamo la durata del timer che sta attualmente in stato di running
        se la lunghezza è stata cambiata nelle settings nel mentre era in background
         */
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        //TODO: change secondsRemaining according to where the background timer stopped

        //resume da dove si era interrotto
        if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    // funzione per quando si clicca sul pulsante di stop (stato di stopped)
    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        /* Imposta la lunghezza del timer con quella impostata in SettingsActivity
        se la durata è cambiata nel mentre era in stato di running
         */
        setNewTimerLength()

        binding.contentTimer.progressCountdown.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons() // chiama update buttons quando lo stato del timer è stopped
        updateCountdownUI()
    }

    // agganciata al pulsante di start
    private fun startTimer(){
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
// ad ogni tick del timer, aggiorna la UI
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    // funzione per settare la durata del timer
    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L) // di tipo long
        binding.contentTimer.progressCountdown.max = timerLengthSeconds.toInt() // imposta la durata max
    }

    // imposta la durata "precedente" ovvero quella già salvata in precedenza nelle preferences
    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        binding.contentTimer.progressCountdown.max = timerLengthSeconds.toInt()
    }

    /* aggiorna UI timer quando è in running, la stringa dei secondi è gestita in modo che visualizzi
     uno 0 davanti se il numero è un'unità */
    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.contentTimer.textViewCountdown.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        binding.contentTimer.progressCountdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    // aggiorna i cottoni a seconda dello stato in cui il timer si trova al momento della chiamata
    // abilita/disabilita i fab con conseguente modifica automatica della UI
    private fun updateButtons(){
        when (timerState) {
            TimerState.Running -> {
                binding.fabStart.isEnabled = false
                binding.fabPause.isEnabled = true
                binding.fabStop.isEnabled = true
            }
            TimerState.Stopped -> {
                binding.fabStart.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = false
            }
            TimerState.Paused -> {
                binding.fabStart.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate del menu; aggiunge items all'action bar se presente.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }


    // selezione dell'item nel menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Gestisce il click nell'item dell'action bar
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* logs per debug */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TimerActivity", "destroyed")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TimerActivity", "stopped")
    }

    override fun onStart() {
        super.onStart()
        Log.d("TimerActivity", "started")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("TimerActivity", "restarted")
    }
}