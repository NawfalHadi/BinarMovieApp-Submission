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
            .document(idFilm.toString())
            .set(favorite)
            .addOnSuccessListener {
                msg = "Success"
            }
            .addOnFailureListener {
                msg = "Failed"
            }

        return msg
    }

    fun deleteFavorite(idFilm: Int): Boolean {
        var msg = false
        db.collection("favorite")
            .document(idFilm.toString())
            .delete()
            .addOnSuccessListener {
                Log.e("Delete Favorite", "deleteFavorite: Deleted Success")
                msg = true
            }
            .addOnFailureListener {
                Log.e("Delete Favorite", "deleteFavorite: Deleted Failed")
            }

        return msg
    }

    fun getFavorite(uid : String) : List<FavoriteEntity>{
        val listFavoriteEntity = mutableListOf<FavoriteEntity>()
        db.collection("favorite")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val favorite = FavoriteEntity(
                        document.id.toInt(),
                        document.data["uid"] as String?,
                        document.data["poster"] as String?
                    )
                    listFavoriteEntity.add(favorite)
                    Log.d("Firebase", "${document.id} => ${document.data["poster"].toString()}")
                }
            }
            .addOnFailureListener {
                Log.w("Firebase", "Error getting documents.", it)
            }
        return listFavoriteEntity
    }

    fun checkFavorite(filmId: Int): Boolean {
        var isAny = false

        db.collection("favorite")
            .document(filmId.toString())
            .get()
            .addOnSuccessListener {
                isAny = true
            }
            .addOnFailureListener {
                isAny = false
            }

        return isAny
    }
}