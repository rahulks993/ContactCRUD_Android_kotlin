package com.example.contactcrud

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.edit_contacts.*
import kotlinx.android.synthetic.main.list_item.*
import java.text.FieldPosition


class ContactsActivity : AppCompatActivity() {

    companion object {
        val TAG: String = ContactsActivity::class.java.simpleName
    }

    private val adapter = MyAdapter(this, Supplier.contactDtls)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        ///Lsit of contacts we will get by this
        Supplier.contactDtls.sortBy { it.name }
/*        adapter.onShareClick =
            { name: String, number: String, email: String -> onShareClick(name, number, email) }*/
        adapter.onCardClick =
            { name: String, number: String, email: String, imgPth: String, pos: Int ->
                onCardClick(
                    name,
                    number,
                    email,
                    imgPth,
                    pos
                )
            }
        recyclerView.adapter = adapter

        ///Button for adding the contact
        val floatBtn: View = findViewById(R.id.fab1)
        floatBtn.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "Add Button Clicked")
            val i = Intent(applicationContext, Add_EditContactsActivity::class.java)
//            i.putExtra("1", true)
            startActivityForResult(i, 1)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra("Name") ?: ""
            val phne = data?.getStringExtra("Number") ?: ""
            val email = data?.getStringExtra("Email") ?: ""
            val img = data?.getStringExtra("imagePath") ?: ""
//            val addImg = data?.getStringExtra("addImgPath") ?:""
            val position = data?.getIntExtra("postion", -1) ?: -1
            val pstn = data?.getIntExtra("pos", -1) ?: -1
            val modelContact = Model(name, phne, email, img)
            when {
                pstn != -1 -> Supplier.contactDtls.removeAt(pstn)
                position != -1 -> Supplier.contactDtls.set(position, modelContact)
                else -> Supplier.contactDtls.add(modelContact)
            }

            Supplier.contactDtls.sortBy { it.name }
            adapter.notifyDataSetChanged()
        }
    }

    ///Function for sharing the data of the contact card
    /*fun onShareClick(name: String, number: String, email: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, name + " - " + number + " - " + email);
        startActivity(Intent.createChooser(shareIntent,getString(com.example.contactcrud.R.string.send_to)))

    }*/

    ////Function for displaying the data on the click of the card
    fun onCardClick(name: String, number: String, email: String, imgPth: String, pos: Int) {
        val intent = Intent(this, Add_EditContactsActivity::class.java)
        intent.putExtra("postion", pos)
        intent.putExtra("name", name)
        intent.putExtra("number", number)
        intent.putExtra("email", email)
        intent.putExtra("imgPth", imgPth)
        startActivityForResult(intent, 1)
    }

}
