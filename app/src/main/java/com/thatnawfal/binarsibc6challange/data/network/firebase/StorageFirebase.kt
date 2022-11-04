package com.thatnawfal.binarsibc6challange.data.network.firebase

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity

class StorageFirebase {
    val db = Firebase.firestore

    fun addFavorite(uid: String, idFilm : Int, poster : String) : String  {
        var msg = ""
        val favorite = hashMapOf(
            "uid" to uid,
            "idFilm" to idFilm,
            "poster" to poster
        )

        db.collection("favorite")
            .add(favorite)
            .addOnSuccessListener {
                msg = "Success"
            }
            .addOnFailureListener {
                msg = "Failed"
            }

        return msg
    }

    fun getFavorite(uid : String) {
        db.collection("favorite")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    Log.d("Firebase", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener {
                Log.w("Firebase", "Error getting documents.", it)
            }
    }
}