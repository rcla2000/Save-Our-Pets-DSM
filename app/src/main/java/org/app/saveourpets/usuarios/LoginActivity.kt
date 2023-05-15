package org.app.saveourpets.usuarios

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.ListarEspeciesActivity
import org.app.saveourpets.reportes.CrearReporteActivity
import org.app.saveourpets.usuarios.particular.MenuParticularActivity
import org.app.saveourpets.usuarios.particular.RegistroParticularActivity
import org.app.saveourpets.utils.Validaciones
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnRegistro : Button
    private lateinit var btnLogin : Button
    private lateinit var btnReporte : Button
    private lateinit var btnGoogle : com.google.android.gms.common.SignInButton
    //Face
    private lateinit var auth: FirebaseAuth //referencia a objeto FirebaseAuth
    private lateinit var btnFacebook : com.facebook.login.widget.LoginButton
    var callbackManager: CallbackManager?=null
    //Google
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var googleSighInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance() //inicializar
        callbackManager = CallbackManager.Factory.create()
        btnFacebook = findViewById(R.id.btn_facebook)
        btnFacebook.setReadPermissions("email")
        btnFacebook.setOnClickListener{
            signIn()
        }
        accionBtnRegistro()
        accionBtnLogin()
        accionBtnReporte()
    }

    private fun accionBtnReporte() {
        btnReporte = findViewById(R.id.btn_reporte)
        btnReporte.setOnClickListener {
            val anonimo : Usuario = Usuario(
                23,
                3,
                "Anónimo",
                "Anónimo",
                "anonimo@gmail.com",
                "22222222",
                "11111111-1",
                "No especificada",
                "0000-00-00",
                "anonimo"
            )
            Sesion.usuario = anonimo
            val intent = Intent(this, CrearReporteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Función para validar los campos de entrada
    private fun errores() : Int {
        var errores : Int = 0

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)

        if (Validaciones.estaVacio(edtEmail.text.toString())) {
            errores += 1
            edtEmail.error = resources.getString(R.string.error_email)
        } else if (!Validaciones.validarEmail(edtEmail.text.toString())) {
            errores += 1
            edtEmail.error = resources.getString(R.string.login_error_email)
        }
        if (Validaciones.estaVacio(edtPassword.text.toString())) {
            errores += 1
            edtPassword.error = resources.getString(R.string.login_error_password)
        }
        return errores
    }

    private fun accionBtnRegistro() {
        btnRegistro = findViewById(R.id.btn_registro)

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroParticularActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun redireccionUsuario(tipoUsuario : Int) {
        var intent = Intent(baseContext, MenuParticularActivity::class.java)
        if (tipoUsuario == 1) {
            intent = Intent(baseContext, ListarEspeciesActivity::class.java)
        } else if (tipoUsuario == 3) {
            intent = Intent(baseContext, MenuParticularActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun accionBtnLogin() {
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            if (errores() == 0) {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val usuario = Usuario(email, password)

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://saveourpets.probalosv.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)

                api.login(usuario).enqueue(object :
                    Callback<Login> {
                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        if (response.isSuccessful) {
                            val login : Login? = response.body()
                            if (login?.estado == true) {
                                val usuario : Usuario = login.usuario
                                Sesion.usuario = usuario
                                Toast.makeText(this@LoginActivity, resources.getString(R.string.login_correcto), Toast.LENGTH_SHORT).show()
                                redireccionUsuario(Sesion.usuario.id_tipo)
                            } else {
                                Toast.makeText(this@LoginActivity, resources.getString(R.string.error_login_1), Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(this@LoginActivity, resources.getString(R.string.error_login_2), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, resources.getString(R.string.error_login_3), Toast.LENGTH_LONG).show()
                    }
                })

            }
        }
    }
    //Face
    private fun signIn(){
        btnFacebook.registerCallback(callbackManager,object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {
                Log.d(error.toString(),error.toString())
            }

        })
    }
    private fun handleFacebookAccessToken(accessToken: AccessToken?){
        val credential: AuthCredential = FacebookAuthProvider.getCredential(accessToken!!.token)
        auth.signInWithCredential(credential).addOnFailureListener{e->
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { result ->
            val intent:Intent = Intent(this,MenuParticularActivity::class.java)
            startActivity(intent)
            val email:String?= result.user?.email
            Toast.makeText(this,"Te logueaste con el correo:"+email,Toast.LENGTH_LONG).show()
        }
    }
    //Google
    private fun signInGoogle(){
        val  signInIntent = googleSighInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }
    private fun handleResults(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                //val intent:Intent = Intent(this,MainActivity::class.java)
                //startActivity(intent)
                Toast.makeText(this,"Se pudo",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode,resultCode,data)
    }
}