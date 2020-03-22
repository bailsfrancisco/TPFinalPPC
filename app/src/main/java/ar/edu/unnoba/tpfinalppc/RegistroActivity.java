package ar.edu.unnoba.tpfinalppc;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.unnoba.tpfinalppc.Utils.DBhelper;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;

    Button btnRegistrarme, btnIngresar;

    private DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = new DBhelper(this);

        etEmail = (EditText) findViewById(R.id.editTextRegEmail);
        etPassword = (EditText) findViewById(R.id.editTextRegPassword);
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
        String pass = etPassword.getText().toString();
        if (email.isEmpty() || pass.isEmpty()) {
            displayToast("Campos vacios.");
        }else if(db.getUser(email,pass)){
            displayToast("El usuario ya se encuentra registrado.");
        }else if(db.getUserByEmail(email)){
            displayToast("El usuario ya se encuentra registrado.");
        }else {
            db.addUser(email, pass);
            displayToast("Usuario registrado.");
            finish();
        }
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}