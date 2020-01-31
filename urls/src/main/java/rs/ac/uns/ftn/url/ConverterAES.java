package rs.ac.uns.ftn.url;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.persistence.AttributeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.cert.CertificateException;

public class ConverterAES implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            String ksPass="secret";
            keystore.load(new FileInputStream(new File("ssl/aesStore.jks")),ksPass.toCharArray());
            String aesPass="secret123";
            Key key = keystore.getKey("AESKey", aesPass.toCharArray());
            return Base64Utility.encode(encrypt(attribute,key));
        } catch (KeyStoreException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            String ksPass="secret";
            keystore.load(new FileInputStream(new File("ssl/aesStore.jks")),ksPass.toCharArray());
            String aesPass="secret123";
            Key key = keystore.getKey("AESKey", aesPass.toCharArray());
            return new String(decrypt(Base64Utility.decode(dbData),key));
        } catch (IOException | KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    private byte[] encrypt(String plainText, Key key) {
        try {
            byte[] iv = new byte[12];
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.nextBytes(iv);
            Cipher aesCipherEnc = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv); //128 bit auth tag length
            aesCipherEnc.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] cipherText = aesCipherEnc.doFinal(plainText.getBytes());
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            byte[] cipherMessage = byteBuffer.array();
            return cipherMessage;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] decrypt(byte[] cipherMessage, Key key) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
            int ivLength = byteBuffer.getInt();
            if (ivLength < 12 || ivLength >= 16) { // zastita od DoS napada
                throw new IllegalArgumentException("invalid iv length");
            }
            byte[] iv = new byte[ivLength];
            byteBuffer.get(iv);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
            byte[] plainText = cipher.doFinal(cipherText);
            return plainText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
