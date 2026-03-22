package com.example.ml_dsa

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.security.KeyPairGenerator
import java.security.Security
import java.security.Signature

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val result = checkMlDsaSupport()
        resultTextView.text = result
    }

    private fun checkMlDsaSupport(): String {
        val details = StringBuilder()

        // List providers to help diagnose where support comes from.
        Security.getProviders().forEach {
            Log.d("MLDSA", "Provider: ${it.name}")
            details.append("Provider: ${it.name}\n")
        }

        var signatureSupported = false
        var keyPairGeneratorSupported = false

        try {
            val sig = Signature.getInstance("ML-DSA")
            Log.d("MLDSA", "Signature supported via: ${sig.provider.name}")
            details.append("Signature: supported via ${sig.provider.name}\n")
            signatureSupported = true
        } catch (e: Exception) {
            Log.e("MLDSA", "Signature NOT supported: ${e.message}")
            details.append("Signature: NOT supported (${e.message ?: "unknown error"})\n")
        }

        try {
            val kpg = KeyPairGenerator.getInstance("ML-DSA")
            Log.d("MLDSA", "KeyPairGenerator supported via: ${kpg.provider.name}")
            details.append("KeyPairGenerator: supported via ${kpg.provider.name}\n")
            keyPairGeneratorSupported = true
        } catch (e: Exception) {
            Log.e("MLDSA", "KeyPairGenerator NOT supported: ${e.message}")
            details.append("KeyPairGenerator: NOT supported (${e.message ?: "unknown error"})\n")
        }

        val supported = signatureSupported && keyPairGeneratorSupported
        val verdict = if (supported) {
            "ML-DSA appears to be supported on this device."
        } else {
            "ML-DSA is not fully supported on this device."
        }

        return "$verdict\n\n$details"
    }
}

