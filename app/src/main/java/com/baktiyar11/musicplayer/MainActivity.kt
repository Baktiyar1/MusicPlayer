package com.baktiyar11.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    var seekBar: SeekBar? = null
    var backButton: ImageButton? = null
    var playBackButton: ImageButton? = null
    var nextButton: ImageButton? = null
    var mp: MediaPlayer? = null
    var fulTime: TextView? = null
    var progressTime: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBarID)
        backButton = findViewById(R.id.backButton)
        playBackButton = findViewById(R.id.playPauseButton)
        nextButton = findViewById(R.id.nextButton)
        fulTime = findViewById(R.id.fulTimeMusicID)
        progressTime = findViewById(R.id.progressMusicID)

        playBackButton?.setOnClickListener {
            if (mp == null) {
                try {
                    mp = MediaPlayer.create(this, R.raw.sujup_kaldym_kantejin)
                    seekBar?.max = mp!!.duration
                    fulTime?.text = seekBar?.max.toString()
                    val time = fulTime?.text.toString().toInt()
                    val minutes: String = TimeUnit.MILLISECONDS.toMinutes(time.toLong()).toString()
                    var seconds: String = TimeUnit.MILLISECONDS.toSeconds(time.toLong()).toString()
                    seconds = (seconds.toInt() - (minutes.toInt()*60)).toString()
                    if (seconds.toInt() < 10){
                        seconds = ("0" + minutes).toString()
                    }
                    fulTime?.text = (minutes + ":" + seconds)
                    initialedSeekBar()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (mp != null && mp?.isPlaying == true) {
                mp?.pause()
                playBackButton?.setImageResource(R.drawable.play_buttonpng)
            } else {
                mp?.start()
                playBackButton?.setImageResource(R.drawable.pause_button)
            }
        }

        backButton?.setOnClickListener {
            if (mp != null) {
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
                progressTime?.text = "0:00"
                playBackButton?.setImageResource(R.drawable.play_buttonpng)
            }
        }

        nextButton?.setOnClickListener {
            if (mp != null) {
                try {
                    mp?.pause()
                    seekBar?.max = mp!!.duration
                    fulTime?.text = seekBar?.max.toString()
                    val time = fulTime?.text.toString().toInt()
                    val minutes: String = TimeUnit.MILLISECONDS.toMinutes(time.toLong()).toString()
                    var seconds: String = TimeUnit.MILLISECONDS.toSeconds(time.toLong()).toString()
                    seconds = (seconds.toInt() - (minutes.toInt()*60)).toString()
                    if (seconds.toInt() < 10){
                        seconds = ("0" + minutes)
                    }
                    fulTime?.text = (minutes + ":" + seconds)
                    progressTime?.text = fulTime?.text
                    seekBar?.progress = seekBar?.max!!
                    playBackButton?.setImageResource(R.drawable.play_buttonpng)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, user: Boolean) {
                if (user) {
                    mp?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(progress: SeekBar?) {
                mp?.pause()
            }

            override fun onStopTrackingTouch(progress: SeekBar?) {
                mp?.start()
            }

        })



    }

    private fun initialedSeekBar() {
        seekBar?.max = mp!!.duration
        var handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar?.progress = mp!!.currentPosition
                    main(seekBar?.progress)
                    progressTime?.text = seekBar?.progress.toString()
                    val time = progressTime?.text.toString().toInt()
                    val minutes: String = TimeUnit.MILLISECONDS.toMinutes(time.toLong()).toString()
                    var seconds: String = TimeUnit.MILLISECONDS.toSeconds(time.toLong()).toString()
                    seconds = (seconds.toInt() - (minutes.toInt()*60)).toString()
                    if (seconds.toInt() < 10){
                        seconds = ("0" + minutes).toString()
                    }
                    progressTime?.text = (minutes + ":" + seconds)
                    handler.postDelayed(this, 15)
                } catch (e: Exception) {
                    seekBar?.progress = 0
                }
            }
        }, 0)
    }

    fun main (args: Int?) {
        val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("mm:ss.SSS")
    val formatted = current.format(formatter)
    println("Current Date and Time is: $formatted")
    }

}