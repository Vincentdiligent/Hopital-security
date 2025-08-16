package com.example.hopitalsecurity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var buildingSpinner: Spinner
    private lateinit var floorSpinner: Spinner
    private lateinit var alertButton: Button
    private lateinit var callButton: Button

    private val buildings = mapOf(
        "Tripode" to LatLng(44.8380, -0.6140),
        "Hôpital des Enfants" to LatLng(44.8385, -0.6150),
        "PQR" to LatLng(44.8374, -0.6136),
        "Centre François-Xavier Michelet" to LatLng(44.8378, -0.6145)
    )

    private val floors = arrayOf(
        "Rez-de-chaussée", "1er étage", "2ème étage", "3ème étage", "4ème étage", "5ème étage",
        "6ème étage", "7ème étage", "8ème étage", "9ème étage", "10ème étage", "11ème étage",
        "12ème étage", "13ème étage", "14ème étage", "15ème étage"
    )

    private val requestCallPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            makeCall()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buildingSpinner = findViewById(R.id.spinner_building)
        floorSpinner = findViewById(R.id.spinner_floor)
        alertButton = findViewById(R.id.button_alert)
        callButton = findViewById(R.id.button_call)

        buildingSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, buildings.keys.toList())
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        floorSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        alertButton.setOnClickListener {
            val selectedBuilding = buildingSpinner.selectedItem.toString()
            val selectedFloor = floorSpinner.selectedItem.toString()
            val intent = AlertActivity.newIntent(this, selectedBuilding, selectedFloor, buildings[selectedBuilding])
            startActivity(intent)
        }

        callButton.setOnClickListener {
            requestCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val hospitalCenter = LatLng(44.8377, -0.6140)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalCenter, 16f))

        buildings.forEach { (name, coord) ->
            map.addMarker(MarkerOptions().position(coord).title(name))
        }
    }

    private fun makeCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:10")
        startActivity(callIntent)
    }
}
