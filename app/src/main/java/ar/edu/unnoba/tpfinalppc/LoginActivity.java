package ar.edu.unnoba.tpfinalppc;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import ar.edu.unnoba.tpfinalppc.Utils.DBhelper;
import ar.edu.unnoba.tpfinalppc.Utils.Session;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;

    Button btnIngresar;
    Button btnRegistrarse;

    private DBhelper db;
    private Session session;

    private String passwordHash;
    String pass_desencriptada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBhelper(this);
        session = new Session(this);

        etEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        etPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        btnIngresar = (Button) findViewById(R.id.buttonLoginIngresar);
        btnRegistrarse = (Button) findViewById(R.id.buttonLoginRegistrarse);

        btnIngresar.setOnClickListener(this);
        btnRegistrarse.setOnClickListener(this);

        if (session.loggedin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLoginIngresar:
                login();
                break;
            case R.id.buttonLoginRegistrarse:
                Intent i2 = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i2);
                break;
            default:
        }
    }

    private void login() {
        String email = etEmail.getText().toString();
        if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            etEmail.setError("Ingrese un email valido");
        }else {
            String pass = etPassword.getText().toString();
            try {
                passwordHash = db.getPasswordHash(email);
                pass_desencriptada = desencriptar(passwordHash, email);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //devuelve true si email y pass coinciden
            if (pass.equals(pass_desencriptada)) {
                session.setLoggedin(true);
                session.guardarDatos(email, pass);
                Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i2);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Email y/o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String desencriptar(String password_a_desencriptar, String password_desencrip) throws Exception{
        SecretKeySpec secretKeySpec = generateKey(password_desencrip);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] passDesencriptada = Base64.decode(password_a_desencriptar, Base64.DEFAULT);
        byte[] passDesencriptadaEnByte = cipher.doFinal(passDesencriptada);
        String passDesencriptadaString = new String(passDesencriptadaEnByte);
        return passDesencriptadaString;
    }

    private SecretKeySpec generateKey(String password) throws  Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key =password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}