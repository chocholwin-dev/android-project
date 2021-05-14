package com.udemy.projemanag.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.udemy.projemanag.R
import com.udemy.projemanag.firebase.FirestoreClass
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // CustomFontを追加する
        val typeFace: Typeface = Typeface.createFromAsset(assets, "DaystreamDemoRegular.ttf")
        tv_app_name.typeface = typeFace

        // 2.5秒経った後、IntroActivity画面へ移動する
        Handler().postDelayed({
            // CurrentUserIdを取得する
            var currentUserID = FirestoreClass().getCurrentUserID()
            // currentUserIDがNullではない場合、MainActivityへ移動する
            if(currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // TODO (Uncomment IntroActivity and comment MainActivity)
                //startActivity(Intent(this, MainActivity::class.java))
                startActivity(Intent(this, IntroActivity::class.java))
            }
            // SplashActivityを閉じる
            finish()
        }, 2500)
    }
}