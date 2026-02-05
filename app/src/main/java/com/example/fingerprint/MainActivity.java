package com.example.fingerprint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> checkBiometric());
    }

    private void checkBiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);

        int result = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
        );

        if (result == BiometricManager.BIOMETRIC_SUCCESS) {
            showBiometricPrompt();
        } else if (result == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            toast("Thiết bị không hỗ trợ vân tay");
        } else if (result == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            toast("Vân tay tạm thời không khả dụng");
        } else if (result == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            toast("Chưa đăng ký vân tay trong máy");
        }
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(
                this,
                executor,
                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        toast("Đăng nhập thành công");
                        // TODO: chuyển sang màn hình Home
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        toast("Vân tay không đúng");
                    }
                });

        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Đăng nhập")
                        .setSubtitle("Xác thực bằng vân tay")
                        .setNegativeButtonText("Hủy")
                        .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}