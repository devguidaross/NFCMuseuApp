package com.guilhermedaros.museunfc.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.guilhermedaros.museunfc.Acitivities.MainActivity;
import com.guilhermedaros.museunfc.Listeners.Listener;
import com.guilhermedaros.museunfc.R;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class FragmentNFC extends DialogFragment {
    
    public static final String TAG = FragmentNFC.class.getSimpleName();
    public String aMessage;

    public static FragmentNFC newInstance() {

        return new FragmentNFC();
    }

    private TextView mTvMessage;
    private ProgressBar mProgress;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){
        mProgress.setVisibility(View.VISIBLE);
        writeToNfc(ndef,aMessage);
    }

    public void formatNdef(Ndef ndef, String message) throws IOException {
        NdefFormatable formatable= NdefFormatable.get(ndef.getTag());

        if (formatable != null) {
            try {
                formatable.connect();

                try {
                    formatable.format(ndef.getNdefMessage());
                }
                catch (Exception e) {
                    // let the user know the tag refused to format
                }
            }
            catch (Exception e) {
                // let the user know the tag refused to connect
            }
            finally {
                formatable.close();
            }
        }
        else {
            // let the user know the tag cannot be formatted
        }

    }


    private String Criptografia(String message, boolean crypt) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        String text = message;
        String key = "Bar12345Bar12345"; // 128 bit key
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        if (crypt){
            return new String(cipher.doFinal(text.getBytes()));
        }
        else{
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            return new String(cipher.doFinal(text.getBytes()));
        }

    }

    private void writeToNfc(Ndef ndef, String message){

        if (ndef != null) {
            try {
                ndef.connect();
                //String crypto = Criptografia(message, true);
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                Toast.makeText(getActivity(), "Salvo com sucesso!", Toast.LENGTH_SHORT).show();

            } catch (IOException | FormatException e) {
                e.printStackTrace();
                mTvMessage.setText("Erro ao salvar!");

            } finally {
                mProgress.setVisibility(View.GONE);
            }

        }
    }
}
