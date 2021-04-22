package com.example.vdzmonitoring.data.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


class FirebaseAuthSource {

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    suspend fun login(email: String, password: String): FirebaseUser {
        mAuth.signInWithEmailAndPassword(email, password).await().let {

        }
        return mAuth.currentUser ?: throw  FirebaseAuthException("", "")
    }

    suspend fun register(email: String, password: String): FirebaseUser {
        mAuth.createUserWithEmailAndPassword(email, password).await().let {

        }
        return mAuth.currentUser ?: throw  FirebaseAuthException("", "")
    }

    fun logout() = mAuth.signOut()

    fun currentUser() = mAuth.currentUser
}