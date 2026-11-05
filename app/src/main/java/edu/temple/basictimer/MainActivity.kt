package edu.temple.basictimer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {

    var timerBinder: TimerService.TimerBinder? = null

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            timerBinder = service as TimerService.TimerBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        findViewById<Button>(R.id.startButton).setOnClickListener {
            timerBinder?.run {
                if (!isRunning) {
                    timerBinder?.start(100)
                    (it as Button).text = "Pause"
                } else {
                    timerBinder?.pause()
                    (it as Button).text = "Resume"
                }
            }
        }

        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder?.run {
                timerBinder?.stop()
                findViewById<Button>(R.id.startButton).text = "Start"
            }
        }
    }
}