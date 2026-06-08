package dev.guilherme.sos

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var textContactInfo: TextView
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Inicializando o serviço SharedPreferences
        sharedPreferences = getSharedPreferences("soccoro", MODE_PRIVATE)

        //Inicializando o serviço de localização
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //ligação entre o kotlin e o XMl
        textContactInfo = findViewById(R.id.textContactInfo)

        // ========= botão config ===========
        findViewById<ImageButton>(R.id.imageButtonConfig).setOnClickListener{
            openConfigActivity()
        }

        // ========= botão sos =======
        findViewById<Button>(R.id.buttonSos).setOnClickListener{

            if(checkAndRequest(Manifest.permission.ACCESS_FINE_LOCATION)){
                //obter a localização
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    Toast.makeText(this, "LAT: ${location.latitude} | LONG:${location.longitude}", Toast.LENGTH_LONG).show()
                }
            }//fim do if


        }
    }//fim do OnCreate

    override fun onStart() {
        super.onStart()
        initSetup()
    }

    private fun displayContactInfo(){
        val contactName = sharedPreferences.getString("contactName", null)
        val contactPhone = sharedPreferences.getString("contactPhone", null)
        textContactInfo.setText("$contactName | $contactPhone")
    }//fim do displayContactInfo


    private fun initSetup(){
        //verificar se a propriedade "contactPhone" existe
        //se sim então mostre na tela, se não exiba o pop-up
        if (sharedPreferences.contains("contactPhone")) {
            displayContactInfo()
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bem vindo(a) ao App Socorro!")
            builder.setMessage("This is a simple alert message.")
            builder.setPositiveButton("Configurar agora"){dialog, which ->
                openConfigActivity()
            }
            builder.create()
            builder.show()
        }
    }//fim do initSetup

    private fun openConfigActivity(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }//fim do openConfigActivity

    private fun checkAndRequest(permission: String): Boolean{
        if (ActivityCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            return false
        }
        return true
    }//fim do checkAndRequest

}//fim da classe