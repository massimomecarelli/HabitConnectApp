package com.example.habitconnect

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.habitconnect.databinding.ActivityTimerBinding
import com.google.android.material.snackbar.Snackbar
import com.example.habitconnect.util.PrefUtil

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private lateinit var notificationManager: NotificationManager
    private lateinit var audioManager: AudioManager

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

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // inserisco icona e titolo del timer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()
        }
        else if (timerState == TimerState.Paused){
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

        //il timer inizia comunque per evitare problemi con crash cliccando sul menu per la selezione temporale
        if (timerState != TimerState.Running){
            PrefUtil.setTimerState(TimerState.Running, this)
            Log.d("initTimer", "dentro if running")
            startTimer()
        } else startTimer()

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
            TimerState.Running -> { // viene settato il non disturbare  e la modalità silenziosa
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT)
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
                // solo gli alarms possono interrompere il filtro delle notifiche

                binding.fabStart.isEnabled = false
                binding.fabPause.isEnabled = true
                binding.fabStop.isEnabled = true
            }
            TimerState.Stopped -> {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                // tutte le notifiche di nuovo attive (nessun filtro)
                binding.fabStart.isEnabled = true
                binding.fabPause.isEnabled = false
                binding.fabStop.isEnabled = false
            }
            TimerState.Paused -> {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                // tutte le notifiche di nuovo attive (nessun filtro)
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
            R.id.Minuto1 -> {
                    // cancella l'esecuzione attuale come se fosse uno stop
                    timer.cancel()
                    onTimerFinished()
                    // imposta una nuova durata nelle preferences
                    PrefUtil.setTimerLength(1, this)
                    // imposta lo stato di Stopped
                    PrefUtil.setTimerState(TimerState.Stopped, this)
                    // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                    initTimer()
                    true
            }
            R.id.Minuti5 -> {
                // cancella l'esecuzione attuale come se fosse uno stop
                timer.cancel()
                onTimerFinished()
                // imposta una nuova durata nelle preferences
                PrefUtil.setTimerLength(5, this)
                // imposta lo stato di Stopped
                PrefUtil.setTimerState(TimerState.Stopped, this)
                // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                initTimer()
                true
            }
            R.id.Minuti10 -> {
                // cancella l'esecuzione attuale come se fosse uno stop
                timer.cancel()
                onTimerFinished()
                // imposta una nuova durata nelle preferences
                PrefUtil.setTimerLength(10, this)
                // imposta lo stato di Stopped
                PrefUtil.setTimerState(TimerState.Stopped, this)
                // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                initTimer()
                true
            }
            R.id.Minuti15 -> {
                // cancella l'esecuzione attuale come se fosse uno stop
                timer.cancel()
                onTimerFinished()
                // imposta una nuova durata nelle preferences
                PrefUtil.setTimerLength(15, this)
                // imposta lo stato di Stopped
                PrefUtil.setTimerState(TimerState.Stopped, this)
                // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                initTimer()
                true
            }
            R.id.Minuti20 -> {
                // cancella l'esecuzione attuale come se fosse uno stop
                timer.cancel()
                onTimerFinished()
                // imposta una nuova durata nelle preferences
                PrefUtil.setTimerLength(20, this)
                // imposta lo stato di Stopped
                PrefUtil.setTimerState(TimerState.Stopped, this)
                // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                initTimer()
                true
            }
            R.id.Minuti30 -> {
                // cancella l'esecuzione attuale come se fosse uno stop
                timer.cancel()
                onTimerFinished()
                // imposta una nuova durata nelle preferences
                PrefUtil.setTimerLength(30, this)
                // imposta lo stato di Stopped
                PrefUtil.setTimerState(TimerState.Stopped, this)
                // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                initTimer()
                true
            }
            R.id.Minuti45 -> {
                // cancella l'esecuzione attuale come se fosse uno stop
                timer.cancel()
                onTimerFinished()
                // imposta una nuova durata nelle preferences
                PrefUtil.setTimerLength(45, this)
                // imposta lo stato di Stopped
                PrefUtil.setTimerState(TimerState.Stopped, this)
                // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                initTimer()
                true
            }
            R.id.Minuti60 -> {
                // cancella l'esecuzione attuale come se fosse uno stop
                timer.cancel()
                onTimerFinished()
                // imposta una nuova durata nelle preferences
                PrefUtil.setTimerLength(60, this)
                // imposta lo stato di Stopped
                PrefUtil.setTimerState(TimerState.Stopped, this)
                // inizializza un nuovo timer con la nuova durata (che partirà in automatico)
                initTimer()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* logs per debug */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TimerActivity", "destroyed")
        /* questo setTimerState viene chiamato per sicurezza, perchè un utente può uscire dal focus
        mentre il timer è in stato Running, quindi viene sempre rimesso in stato paused
         */
        PrefUtil.setTimerState(TimerState.Paused, this)
        // per sicurezza vengono rimesse tutte le suonerie e disattivato do not disturb
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
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
        /* l'utente quando è dentro focus, può andare nelle impostazioni e togliere la permission
        per il do not disturb. Quindi al restart dell'activity viene rifatto il controllo
         */
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            Toast.makeText(this, "Allow Do Not Disturb Access!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}