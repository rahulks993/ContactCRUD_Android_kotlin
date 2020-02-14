package com.example.contactcrud

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactcrud.com.jeeves.extensions.Constants
import com.example.contactcrud.com.jeeves.extensions.showToast
import kotlinx.android.synthetic.main.activity_second.*


class SecondActivity : AppCompatActivity() {

    companion object {
        val TAG: String = SecondActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Safe Call   ?.
        // Safe Call with let  ?.let {  }

        val bundle: Bundle? = intent.extras

        bundle?.let {
            val msg = bundle.getString(Constants.USER_MSG_KEY)
            if (msg != null) {
                showToast(msg)
            }
            txvUserMessage.text = msg
        }
    }
}