package com.udemy.texttospeechdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import java.util.*

// Extending our MainActivity with the TextToSpeech OnInitListener
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    // Variable for TextToSpeech
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Text to Speech
        tts = TextToSpeech(this, this)

        // Speakボタンを押す場合、
        btnSpeak.setOnClickListener {
            if(edEnteredText.text.isEmpty()){
                Toast.makeText(this,
                    "Enter a text to use TTS.", Toast.LENGTH_SHORT).show()
            }else{
                speakOut(edEnteredText.text.toString())
            }
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not supported.")
            }
        }else{
            Log.e("TTS","Initialization Failed!")
        }
    }

    /**
     * Function is used to speak the text what we pass to it.
     */
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    /**
     * Here is a onDestroy Function we will stop and shutdown the tts
     */
    override fun onDestroy() {
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}