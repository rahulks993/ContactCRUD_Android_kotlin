package com.example.contactcrud

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.edit_contacts.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class Add_EditContactsActivity : AppCompatActivity() {

    companion object {
        val TAG: String = Add_EditContactsActivity::class.java.simpleName
        val IMAGE_DIRECTORY = "/demoApp123"
    }

    private var isShowOptionsItem = true
    private val GALLERY = 1
    private val CAMERA = 2
    private val PERMISSIONCODE = 1000
    var imageBit: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.contactcrud.R.layout.edit_contacts)

        isShowOptionsItem = false
        invalidateOptionsMenu()


        //Onclick of add Icon
        val add_action: Boolean = intent.getBooleanExtra("1", true)
        if (add_action != false) {
            fab2.hide()
            txtName.visibility = View.GONE
            txtNumber.visibility = View.GONE
            txtMail.visibility = View.GONE
            image1.setOnClickListener { showPictureDialog() }
        }

        //For editing a contact
        val edit_action: Boolean = intent.getBooleanExtra("0", false)

        if (!edit_action) {
            val name = intent.getStringExtra("name")
            val num = intent.getStringExtra("number")
            val mail = intent.getStringExtra("email")
            val imPath = intent.getStringExtra("imgPth")


            if (name != null) {
//                val nameTextView = findViewById<TextView>(R.id.txtName)
                isShowOptionsItem = true
                txtName.text = name
                txtName.visibility = View.VISIBLE
                editTextName.visibility = View.GONE
                fab2.show()
                buttonSave.visibility = View.GONE
            }
            if (num != null) {
//                val numTextView = findViewById<TextView>(R.id.txtNumber)
                txtNumber.text = num
                txtNumber.visibility = View.VISIBLE
                editTextPhone.visibility = View.GONE
            }

            if (mail != null) {
//                val mailTextView = findViewById<TextView>(R.id.txtMail)
                txtMail.text = mail
                txtMail.visibility = View.VISIBLE
                editTextMail.visibility = View.GONE
            }

            if (!imPath.isNullOrEmpty()) {
                Glide.with(this)
                    .load(imPath)
                    .into(image1)
            }


        }

        ///On click of the saving the  contact
        buttonSave.setOnClickListener {
            val intent = Intent()
            intent.putExtra("Name", editTextName.text.toString())
            intent.putExtra("Number", editTextPhone.text.toString())
            intent.putExtra("Email", editTextMail.text.toString())
            if (imageBit != null) {
                val path = saveImage(imageBit!!)
                intent.putExtra("imagePath", path)
            }
            setResult(Activity.RESULT_OK, intent)
            Log.d(TAG, "Data sent for add from Add ContactsActivity onClick of Button Save")
            Toast.makeText(this, "Contact Added", Toast.LENGTH_LONG).show()
            finish()

        }

        ///for editing a contact on click of edit
        fab2.setOnClickListener {
            Log.d(TAG, "Edit icon clicked from the contact card")
            isShowOptionsItem = false
            invalidateOptionsMenu()
            txtName.visibility = View.GONE
            txtNumber.visibility = View.GONE
            txtMail.visibility = View.GONE
            editTextName.visibility = View.VISIBLE
            editTextPhone.visibility = View.VISIBLE
            editTextMail.visibility = View.VISIBLE
//            imageButton.visibility = View.VISIBLE
//            image1.visibility = View.GONE
            fab2.hide()
            buttonSave.visibility = View.VISIBLE
            image1.setOnClickListener { showPictureDialog() }    ////For adding image onClick of edit imagebutton
            if (txtName.toString() == null || txtNumber.toString() == null || txtMail.toString() == null) {
            } else {
                editTextName.setText(txtName.text.toString())
                editTextPhone.setText(txtNumber.text.toString())
                editTextMail.setText(txtMail.text.toString())

            }
            buttonSave.setOnClickListener {
                Toast.makeText(this, "Contact Edited", Toast.LENGTH_LONG).show()
                val returnIntent = Intent()
                returnIntent.putExtra("postion", intent.getIntExtra("postion", -1))
                if (imageBit != null) {
                    val path = saveImage(imageBit!!)
                    returnIntent.putExtra("imagePath", path)
                }
                returnIntent.putExtra("Name", editTextName.text.toString())
                returnIntent.putExtra("Number", editTextPhone.text.toString())
                returnIntent.putExtra("Email", editTextMail.text.toString())
                setResult(Activity.RESULT_OK, returnIntent)
                Log.d(TAG, "Data sent for edit from Edit ContactsActivity onClick of Button Save")
                finish()
            }
        }
    }

    ///adding choice for setting up of the image
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), PERMISSIONCODE
                )
            }
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONCODE) {
            takePhotoFromCamera()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    imageBit = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //val path = saveImage(imageBit!!)
//                    Toast.makeText(this@Add_EditContactsActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    //image1!!.setImageBitmap(imageBit)
                    Glide.with(this).load(imageBit).into(image1!!)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@Add_EditContactsActivity, "Failed!", Toast.LENGTH_SHORT)
                        .show()
                }

            }

        } else if (requestCode == CAMERA) {

            imageBit = data!!.extras!!.get("data") as Bitmap
//            image1!!.setImageBitmap(imageBit)
            Glide.with(this).load(imageBit).into(image1!!)
            /* saveImage(thumbnail)
             Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()*/
        }
    }


    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        val abc = wallpaperDirectory.mkdirs()

        try {
            Log.d("heel", wallpaperDirectory.toString())
//            val f = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".png"))
            val f = File(wallpaperDirectory, editTextName.text.toString() + "contactImage.png")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this@Add_EditContactsActivity,
                arrayOf(f.getPath()),
                arrayOf("image/png"),
                null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!isShowOptionsItem) return false
        menuInflater.inflate(com.example.contactcrud.R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val name = txtName.text.toString()
        val number = txtNumber.text.toString()
        val email = txtMail.text.toString()

        if (item.itemId == com.example.contactcrud.R.id.delete) {
            Toast.makeText(this, "Contact Deleted", Toast.LENGTH_LONG).show()
            Log.d(TAG, "Delete Item Clicked fromt he three dot menu")
            val retrnIntent = Intent()
            retrnIntent.putExtra("pos", intent.getIntExtra("postion", 0))
            setResult(Activity.RESULT_OK, retrnIntent)
            finish()
        }

        if (item.itemId == R.id.share) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, name + " - " + number + " - " + email);
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
        return true
    }
}

