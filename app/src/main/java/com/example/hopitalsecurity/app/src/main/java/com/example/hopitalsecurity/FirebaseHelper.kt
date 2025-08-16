package com.example.hopitalsecurity

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseHelper {

    private val db = FirebaseFirestore.getInstance()
    private const val COLLECTION_ALERTS = "alerts"

    fun sendAlert(alert: AlertData, callback: (Boolean) -> Unit) {
        db.collection(COLLECTION_ALERTS)
            .add(alert)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
}
