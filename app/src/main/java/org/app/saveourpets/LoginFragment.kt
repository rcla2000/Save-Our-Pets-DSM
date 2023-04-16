package org.app.saveourpets

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.facebook.appevents.AppEventsLogger


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //referencia a objeto FirebaseAuth
    private lateinit var btnFacebook : com.facebook.login.widget.LoginButton
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    var callbackManager: CallbackManager?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance() //inicializar
        callbackManager = CallbackManager.Factory.create()
        btnFacebook = view.findViewById(R.id.btn_facebook)
        btnFacebook.setReadPermissions("email")
        btnFacebook.setOnClickListener{
            signIn()
        }
        this.checkUser()

        val button = view.findViewById<Button>(R.id.btn_login)
        button.setOnClickListener {
            val intent = Intent(activity, AdminActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun signIn(){
        btnFacebook.registerCallback(callbackManager,object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
                Log.d("aaa","success")
            }

            override fun onCancel() {
                Log.d("aaa","cancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("aaa", error.toString())
            }

        })
    }
    private fun handleFacebookAccessToken(accessToken: AccessToken?){
        val credential: AuthCredential = FacebookAuthProvider.getCredential(accessToken!!.token)
        auth.signInWithCredential(credential).addOnFailureListener{e->
            Toast.makeText(activity,e.message, Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { result ->
            val email:String?= result.user?.email
            Toast.makeText(activity,"Te logueaste con el correo:"+email, Toast.LENGTH_LONG).show()
            //Cambiamos de vista
            val intent = Intent(activity, AdminActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode,resultCode,data)
    }

    private fun checkUser(){
        //verify user
        authStateListener = FirebaseAuth.AuthStateListener { auth->
            if(auth.currentUser != null){
                //cambiando vista
                val intent = Intent(activity, AdminActivity::class.java)
                startActivity(intent)
            }
        }
    }

}