package com.jasonstudio.cookbook2.helper

//import android.security.keystore.KeyGenParameterSpec
//import android.security.keystore.KeyProperties
//import com.jasonstudio.cookbook2.util.LogUtil
//import java.security.KeyPairGenerator
//import java.security.KeyStore
//import java.security.Signature
//
//class EncrpytedSharedPref {
//    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
//        KeyProperties.KEY_ALGORITHM_EC,
//        "AndroidKeyStore"
//    )
//    val alias = "ALIAS"
//    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
//        alias,
//        KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
//    ).run {
//        setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
//        build()
//    }
//    val kp = kpg.generateKeyPair()
//    init {
//        kpg.initialize(parameterSpec)
//    }
//
//    fun signData(data: ByteArray) {
//        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
//            load(null)
//        }
//        val entry: KeyStore.Entry = ks.getEntry(alias, null)
//        if (entry !is KeyStore.PrivateKeyEntry) {
//            LogUtil.log("Not an instance of a PrivateKeyEntry")
//            return
//        }
//        val signature: ByteArray = Signature.getInstance("SHA256withECDSA").run {
//            initSign(entry.privateKey)
//            update(data)
//            sign()
//        }
//    }
//}