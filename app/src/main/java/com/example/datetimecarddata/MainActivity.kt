package com.example.datetimecarddata

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    private var photoUri: Uri? = null
    private lateinit var toolbarMain: Toolbar
    private lateinit var editImageIV: ImageView
    private lateinit var nameET: EditText
    private lateinit var surNameET: EditText
    private lateinit var phoneET: EditText
    private lateinit var birthdayET: EditText
    private lateinit var saveBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbarMain = findViewById(R.id.toolbarMain)
        editImageIV = findViewById(R.id.editImageIV)
        nameET = findViewById(R.id.nameET)
        surNameET = findViewById(R.id.surNameET)
        phoneET = findViewById(R.id.phoneET)
        birthdayET = findViewById(R.id.birthdayET)
        saveBTN = findViewById(R.id.saveBTN)

        setSupportActionBar(toolbarMain)
        title = getString(R.string.title_toolbar)
        toolbarMain.subtitle = "by Rocky"

        photoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    photoUri = result.data?.data // для загрузки изображения
                    editImageIV.setImageURI(photoUri)
                }
            }

        editImageIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            photoPickerLauncher.launch(photoPickerIntent)
        }

        saveBTN.setOnClickListener {
            if (nameET.text.isEmpty() ||
                surNameET.text.isEmpty() ||
                birthdayET.text.isEmpty() ||
                phoneET.text.isEmpty() ||
                photoUri == null
            ) return@setOnClickListener

            val name = nameET.text.toString()
            val surName = surNameET.text.toString()
            val birthday = birthdayET.text.toString()
            val phone = phoneET.text.toString()
            val image = photoUri.toString()
            val person = Person(name, surName, phone, birthday, image)

            val intent = Intent(this, CardDataActivity::class.java)
            intent.putExtra("person", person)
            startActivity(intent)

            nameET.text.clear()
            surNameET.text.clear()
            phoneET.text.clear()
            birthdayET.text.clear()
            editImageIV.setImageResource(R.drawable.ic_product)
            photoUri = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> finishAffinity()
        }
        return super.onOptionsItemSelected(item)
    }
}