package com.example.apptuhorasalud;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroActivity extends AppCompatActivity {
    EditText etNombre, etApellido, etCorreo, etContraseña, etFechaNacimiento;
    Button btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCorreo = findViewById(R.id.etCorreo);
        etContraseña = findViewById(R.id.etContraseña);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });


    }
    private void validarCampos() {
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String correo = etCorreo.getText().toString();
        String contraseña = etContraseña.getText().toString();
        String fechaNacimiento = etFechaNacimiento.getText().toString();
        if (nombre.isEmpty()) {
            //etNombre.setError("Nombre requerido");
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
            etNombre.requestFocus();
            return;
        }
        if (apellido.isEmpty()) {
            //etApellido.setError("Apellido requerido");
            Toast.makeText(this, "Ingrese un apellido", Toast.LENGTH_SHORT).show();
            etApellido.requestFocus();
            return;
        }
        if (correo.isEmpty()) {
            Toast.makeText(this, "El correo es obligatorio", Toast.LENGTH_SHORT).show();
            //txtCorreo.setError("Correo requerido");
            etCorreo.requestFocus();
            return;
        }
        // Valida el formato del email
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contraseña.isEmpty()) {
            //etContraseña.setError("Contraseña requerida");
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            etContraseña.requestFocus();
            return;
        }
        if (fechaNacimiento.isEmpty()) {
            //etFechaNacimiento.setError("Fecha de nacimiento requerida");
            Toast.makeText(this, "Ingrese una fecha de nacimiento", Toast.LENGTH_SHORT).show();
            etFechaNacimiento.requestFocus();
            return;
        }
        if (fechaNacimiento.length() != 8) {
            Toast.makeText(this, "La fecha debe tener 8 dígitos", Toast.LENGTH_SHORT).show();
        } else if (!fechaNacimiento.matches("\\d{8}")) {
            Toast.makeText(this, "Solo se permiten números en la fecha", Toast.LENGTH_SHORT).show();
        }

    }

}