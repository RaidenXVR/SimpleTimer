package com.isga.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class TimerViewModel : ViewModel() {
    private val _timerFinished = MutableLiveData<Boolean>()
    val timerFinished: LiveData<Boolean> = _timerFinished
    private val _minutesLeft = MutableLiveData<Long>()
    private val _secondsLeft = MutableLiveData<Long>()
    val minutesLeft :LiveData<Long> = _minutesLeft
    val secondsLeft : LiveData<Long> = _secondsLeft

    private var timer: CountDownTimer? = null

    fun startTimer(durationInMillis: Long) {
        timer?.cancel() // Cancel any existing timer

        timer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val totalSeconds = millisUntilFinished / 1000
                _minutesLeft.value = totalSeconds / 60
                _secondsLeft.value = totalSeconds % 60
            }

            override fun onFinish() {
                _minutesLeft.value = 0
                _secondsLeft.value = 0
                _timerFinished.value = true
            }
        }.start()
    }

    fun resetTimer() {
        timer?.cancel()
        _minutesLeft.value = 0
        _secondsLeft.value = 0
        _timerFinished.value = false
    }


}
