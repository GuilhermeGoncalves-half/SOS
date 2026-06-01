package dev.guilherme.sos

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    //Declaração dos atributos da classe
    private lateinit var editTextContactName: EditText
    private lateinit var editTextContactPhone: EditText
    private lateinit var editTextCustomMsg: EditText

    //Armazena valores que serão disponíveis em todo o APP
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }//fim do ViewCompat

        //Inicializando o serviço SharedPreferences
        sharedPreferences = getSharedPreferences("socorro", MODE_PRIVATE)

        //Ligação entre o kotlin e o XML
        editTextContactName = findViewById(R.id.TextInputEditTextContactName)
        editTextContactPhone = findViewById(R.id.TextInputEditTextContactPhone)
        editTextCustomMsg = findViewById(R.id.TextInputEditTextCustomMessage)

        findViewById<Button>(R.id.button).setOnClickListener{
            savePreferences()
        }//fim do buttonSave

        //MELHORAR ISSO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        loadPreferences()

    }//fim do onCreate

    private fun savePreferences(){
        sharedPreferences.edit()
            .putString("contactName",editTextContactName.text.toString())
            .putString("contactPhone",editTextContactPhone.text.toString())
            .putString("msg", editTextCustomMsg.text.toString())
            .apply()
        Toast.makeText(this, "Susesso!!!", Toast.LENGTH_SHORT).show()
        loadPreferences()
    }//fim do savePreferences

    private fun loadPreferences(){
        editTextContactName.setText(sharedPreferences.getString("contactName",""))
        editTextContactPhone.setText(sharedPreferences.getString("contactPhone",""))
        editTextCustomMsg.setText(sharedPreferences.getString("msg",""))
    }//fim do loadPreferences

}//fim da classe