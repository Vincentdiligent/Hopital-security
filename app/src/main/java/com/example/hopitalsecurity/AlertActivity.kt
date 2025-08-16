package com.example.hopitalsecurity

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class AlertActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var infoText: TextView

    private lateinit var building: String
    private lateinit var floor: String
    private var buildingCoord: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        building = intent.getStringExtra(EXTRA_BUILDING) ?: ""
        floor = intent.getStringExtra(EXTRA_FLOOR) ?: ""
        buildingCoord = intent.getParcelableExtra(EXTRA_COORD)

        infoText = findViewById(R.id.text_info)
        messageInput = findViewById(R.id.edit_message)
        sendButton = findViewById(R.id.button_send)

        infoText.text = "Déclarez la nature de l'agression\nBâtiment: $building\nÉtage: $floor"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sendButton.setOnClickListener { sendAlert() }
    }

    private fun sendAlert() {
        val messageText = messageInput.text.toString().trim()
        if (messageText.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_message_prompt), Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val alert = AlertData(
                    building = building,
                    floor = floor,
                    message = messageText,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timestamp = System.currentTimeMillis()
                )
                FirebaseHelper.sendAlert(alert) { success ->
                    if (success) {
                        Toast.makeText(this, getString(R.string.alert_sent), Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.alert_send_error), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.location_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val EXTRA_BUILDING = "extra_building"
        private const val EXTRA_FLOOR = "extra_floor"
        private const val EXTRA_COORD = "extra_coord"

        fun newIntent(context: Context, building: String, floor: String, coordinate: LatLng?) =
            Intent(context, AlertActivity::class.java).apply {
                putExtra(EXTRA_BUILDING, building)
                putExtra(EXTRA_FLOOR, floor)
                putExtra(EXTRA_COORD, coordinate)
            }
    }
}

data class AlertData(
    val building: String,
    val floor: String,
    val message: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
