package com.example.contactcrud

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactcrud.com.jeeves.extensions.Constants
import com.google.firebase.dynamiclinks.DynamicLink.*
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //For dyanmic link testing, so the action is plain. Since the app is not present in the play Store. The link will redirect to Youtube
        btnDynamic.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, buildDynamicLink())
            startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))

        }

        //Sending a Text message to the next activity
        btnSendMsgToNextActivity.setOnClickListener {
            val message: String = etUserMessage.text.toString()
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra(Constants.USER_MSG_KEY, message)
            startActivity(intent)
        }

        //BUtton for sharing via intents. Basically to understand intent sharing
        btnShareToOtherApps.setOnClickListener {
            val message: String = etUserMessage.text.toString()
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Please select app: "))
        }

        //This button takes to the next activity where you can see the contacts list there.
        btnRecyclerViewDemo.setOnClickListener {
            val intent = Intent(this, ContactsActivity::class.java)
            startActivity(intent)
        }
    }

    //Building the dynamic link using Firebase
    private fun buildDynamicLink(): String? {
        val path = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setDomainUriPrefix("contactcrud.page.link/6RQi")
            .setLink(Uri.parse("https://www.youtube.com/user/CokeStudioPk"))
            .setAndroidParameters(AndroidParameters.Builder().build())
            .setSocialMetaTagParameters(
                SocialMetaTagParameters.Builder().setTitle("Share this App").setDescription(
                    "CokeStudio"
                ).build()
            )
            .setGoogleAnalyticsParameters(GoogleAnalyticsParameters.Builder().setSource("AndroidApp").build())
            .buildDynamicLink().uri.toString()

        return path
    }
}
