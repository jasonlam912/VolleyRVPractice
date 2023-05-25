package com.jasonstudio.cookbook2.view.activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.jasonstudio.cookbook2.databinding.ActivityBiometricBinding
import com.jasonstudio.cookbook2.helper.SharedPref
import com.jasonstudio.cookbook2.util.LogUtil
import java.security.InvalidKeyException
import java.security.KeyStore
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class BiometricActivity: BaseActivity<ActivityBiometricBinding>(ActivityBiometricBinding::inflate) {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val KEY_NAME = "KEYSTORE_KEY_NAME"

    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lets the user authenticate using either a Class 3 biometric or
        // their lock screen credential (PIN, pattern, or password).
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
        executor = ContextCompat.getMainExecutor(this)
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                LogUtil.log("MY_APP_TAG", "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                LogUtil.log("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                LogUtil.log("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                startActivityForResult(enrollIntent, 1)
            }
        }

//        generateSecretKey(KeyGenParameterSpec.Builder(
//            KEY_NAME,
//            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
//            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//            .setUserAuthenticationRequired(true)
//            .build())

        generateSecretKey(KeyGenParameterSpec.Builder(
            KEY_NAME,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationValidityDurationSeconds(30)
            .build())

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val encryptedInfo: ByteArray = encryptSecretInformation()!!
                    LogUtil.log("MY_APP_TAG", "Encrypted information: " +
                            encryptedInfo.contentToString()
                    )
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })
        binding.bioBtn.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                encryptSecretInformation()
            }
        })
        binding.btnDecodeData.setOnClickListener {
            decrpytSecretInformation()
        }
    }

    private fun encryptSecretInformation(): ByteArray? {
        // Exceptions are unhandled for getCipher() and getSecretKey().
        val cipher = getCipher()
        val secretKey = getSecretKey()
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val ivParameterSpec = cipher.parameters.getParameterSpec(IvParameterSpec::class.java)
            SharedPref.setIv(ivParameterSpec)
            val encryptedInfo: ByteArray = cipher.doFinal(
                // plaintext-string text is whatever data the developer would
                // like to encrypt. It happens to be plain-text in this example,
                // but it can be anything
                "wwwwwwww".toByteArray(Charsets.ISO_8859_1)
            )
            SharedPref.setEncryptedData(encryptedInfo)
            LogUtil.log(
                "MY_APP_TAG", "Encrypted information: " +
                        encryptedInfo.toString(Charsets.ISO_8859_1)
            )
            return encryptedInfo
        } catch (e: UserNotAuthenticatedException) {
            LogUtil.log("MY_APP_TAG", "The key's validity timed out.", e)
            biometricPrompt.authenticate(promptInfo)
        } catch (e: InvalidKeyException) {
            LogUtil.log("MY_APP_TAG", "Key is invalid.", e)
        }
        return null
    }

    private fun decrpytSecretInformation(): String {
        val cipher = getCipher()
        val secretKey = getSecretKey()
        Charsets.ISO_8859_1
        cipher.init(Cipher.DECRYPT_MODE, secretKey, SharedPref.getIv())
        val decryptedInfo = cipher.doFinal(SharedPref.getEncryptedData())
        val res = decryptedInfo.toString(Charsets.ISO_8859_1)
        LogUtil.log(res)
        return res
    }
}