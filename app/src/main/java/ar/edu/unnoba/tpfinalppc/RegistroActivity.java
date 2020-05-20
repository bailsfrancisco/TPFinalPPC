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

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etPaswordCheck;

    Button btnRegistrarme, btnIngresar;

    private String pass_encriptada;

    private DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = new DBhelper(this);

        etEmail = (EditText) findViewById(R.id.editTextRegEmail);
        etPassword = (EditText) findViewById(R.id.editTextRegPassword);
        etPaswordCheck = (EditText) findViewById(R.id.editTextRegPasswordCheck);
        btnRegistrarme = (Button) findViewById(R.id.buttonRegRegistrarme);
        btnIngresar = (Button) findViewById(R.id.buttonRegIngresar);

        btnRegistrarme.setOnClickListener(this);
        btnIngresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegRegistrarme:
                register();
                break;
            case R.id.buttonRegIngresar:
                Intent i2 = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(i2);
                finish();
                break;
        }
    }

    private void register() {
        String email = etEmail.getText().toString();
        if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            etEmail.setError("Ingrese un email valido");
        }else {
            String pass = etPassword.getText().toString();
            String pass_check = etPaswordCheck.getText().toString();
            if (pass.equals(pass_check)) {
                try {
                    //con el email encripto y desencripto (es la password para realizar dichas acciones)
                    pass_encriptada = encriptar(pass, email);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (email.isEmpty() || pass.isEmpty()) {
                    displayToast("Campos vacios.");
                } else if (db.getUserByEmail(email)) {
                    displayToast("El usuario ya se encuentra registrado.");
                } else {
                    db.addUser(email, pass_encriptada);
                    displayToast("Usuario registrado.");
                    finish();
                }
            } else {
                displayToast("Las contraseñas no coinciden !!!");
            }
        }
    }

    private String encriptar(String password_a_encriptar, String password_encrip) throws Exception {
        SecretKeySpec secretKeySpec = generateKey(password_encrip);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] passEncriptadaEnBytes = cipher.doFinal(password_a_encriptar.getBytes());
        String passEncriptadaString = Base64.encodeToString(passEncriptadaEnBytes, Base64.DEFAULT);
        return passEncriptadaString;
    }

    private SecretKeySpec generateKey(String password_encrip) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password_encrip.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}