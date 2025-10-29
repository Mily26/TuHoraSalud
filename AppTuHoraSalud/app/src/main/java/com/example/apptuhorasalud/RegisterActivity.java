package com.example.apptuhorasalud;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;
import com.example.apptuhorasalud.application.useCase.User.AddUser;
import com.example.apptuhorasalud.domain.models.User;
import com.example.apptuhorasalud.domain.interfaces.IUserRepository;
import com.example.apptuhorasalud.infrastructure.data.AppDatabase;
import com.example.apptuhorasalud.infrastructure.repository.UsuarioRepositoryImpl;
import com.example.apptuhorasalud.utils.FormUtils;

import org.xml.sax.Parser;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputDocument, inputName, inputLastname, inputEmail, inputPassword, inputConfirmPassword, inputBirthDate;
    private Button btnSaveUser;

    private String document, name, lastname, email, birthDate, password, confirmPassword;

    private static final String DB_NAME = "usuarios-db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollRegistro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inputDocument = findViewById(R.id.inputDocument);
        inputName = findViewById(R.id.inputName);
        inputLastname = findViewById(R.id.inputLastname);
        inputBirthDate = findViewById(R.id.inputBirthDate);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword= findViewById(R.id.inputConfirmPassword);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        btnSaveUser.setOnClickListener(v -> onRegisterClick());
    }

    // methods
    private void onRegisterClick() {
        getValues();
        if (!validateInput()) return;
        saveUser();
    }
    private void getValues(){
        document = inputDocument.getText().toString().trim();
        name = inputName.getText().toString().trim();
        lastname = inputLastname.getText().toString().trim();
        birthDate = inputBirthDate.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        confirmPassword = inputConfirmPassword.getText().toString().trim();
    }
    private boolean validateInput(){
        if (TextUtils.isEmpty(document)) {
            FormUtils.showError(this, "Ingrese el documento", inputDocument);
            return false;
        }

        if (document.length() < 8) {
            FormUtils.showError(this, "El documento debe tener al menos 8 dígitos", inputDocument);
            return false;
        }

        try {
            Long.parseLong(document);
        } catch (NumberFormatException e) {
            FormUtils.showError(this, "El documento debe ser numérico", inputDocument);
            return false;
        }

        if (name.isEmpty()) {
            FormUtils.showError(this, "Ingrese un nombre", inputName);
            return false;
        }
        if (lastname.isEmpty()) {
            FormUtils.showError(this, "Ingrese un apellido", inputLastname);
            return false;
        }
        if (email.isEmpty()) {
            FormUtils.showError(this, "El correo es obligatorio", inputEmail);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FormUtils.showError(this, "Ingrese un correo válido", inputEmail);
            return false;
        }
        if (password.isEmpty()) {
            FormUtils.showError(this, "Ingrese una contraseña", inputPassword);
            return false;
        }
        if (birthDate.isEmpty()) {
            FormUtils.showError(this, "Ingrese una fecha de nacimiento", inputBirthDate);
            return false;
        }
        if (birthDate.length() != 8 || !birthDate.matches("\\d{8}")) {
            FormUtils.showError(this, "La fecha debe tener 8 dígitos numéricos", inputBirthDate);
            return false;
        }
        if (!password.equals(confirmPassword)) {
            FormUtils.showError(this, "Las contraseñas no coinciden", inputConfirmPassword);
            return false;
        }
        return true;
    }


    private void saveUser(){
        User user = new User(null, Integer.parseInt(document), name, lastname, birthDate, email, password);

        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DB_NAME).build();
            IUserRepository repo = new UsuarioRepositoryImpl(db.usuarioDao());
            AddUser useCase = new AddUser(repo);

            useCase.execute(user);
            Log.d("DB", "Usuario guardado correctamente: " + user.getName() + " - " + user.getEmail());

            runOnUiThread(() -> FormUtils.showSuccess(this, "Usuario guardado correctamente"));
        }).start();
    }

}
