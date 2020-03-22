package ar.edu.unnoba.tpfinalppc;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.unnoba.tpfinalppc.Utils.DBhelper;
import ar.edu.unnoba.tpfinalppc.Utils.Session;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;

    Button btnIngresar;
    Button btnRegistrarse;

    private DBhelper db;
    private Session session;

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
        String pass = etPassword.getText().toString();
        if (db.getUser(email, pass)) {
            session.setLoggedin(true);
            session.guardarDatos(email,pass);
            Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i2);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Email y/o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
        }
    }
}