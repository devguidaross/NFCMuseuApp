package com.guilhermedaros.museunfc.Acitivities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.speech.tts.TextToSpeech;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.guilhermedaros.museunfc.Fragments.FragmentEscrever;
import com.guilhermedaros.museunfc.Fragments.FragmentMain;
import com.guilhermedaros.museunfc.Entities.ItemMuseu;
import com.guilhermedaros.museunfc.Listeners.Listener;
import com.guilhermedaros.museunfc.Fragments.FragmentNFC;
import com.guilhermedaros.museunfc.R;

import java.io.IOException;
import java.util.Locale;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements Listener
{
    private boolean isDialogDisplayed = false;
    public boolean isWrite = false;
    private NfcAdapter mNfcAdapter;
    private TextToSpeech mTs;
    private boolean UsuarioLogado = false;


    private FragmentNFC mNfcWriteFragment;



    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onDestroy() {
        //Close the Text to Speech Library
        if(mTs != null) {

            mTs.stop();
            mTs.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (mViewPager.getCurrentItem() == 1)
            mViewPager.setCurrentItem(0);
        else
            super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences lSharedPreferences = getSharedPreferences("museunfc", MODE_PRIVATE);

        UsuarioLogado = lSharedPreferences.getBoolean("usuariologado", false);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        initViews();
        initNFC();
        initSpeech();

//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

    }

    private void initSpeech(){
        try {

            if (mTs == null){
                mTs = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            Locale myLocale = new Locale("pt", "BR");
                            mTs.setLanguage(myLocale);
                        }
                    }

                });
            }
        }catch (Exception e){

        }
    }

    private void initViews() {

    }

    public void showWriteFragment(String message) {

        isWrite = true;

        mNfcWriteFragment = (FragmentNFC) getFragmentManager().findFragmentByTag(FragmentNFC.TAG);

        if (mNfcWriteFragment == null) {
            mNfcWriteFragment = FragmentNFC.newInstance();
            mNfcWriteFragment.aMessage = message;
        }
        mNfcWriteFragment.show(getFragmentManager(), FragmentNFC.TAG);

    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            SharedPreferences lSharedPreferences = getSharedPreferences("museunfc", MODE_PRIVATE);
            SharedPreferences.Editor lEditor =  lSharedPreferences.edit();
            lEditor.putBoolean("usuariologado", false);
            lEditor.putBoolean("primeiravez", true);
            lEditor.apply();
            Intent intent = new Intent(this, EscolhaUsuarioActivity.class);
            startActivity(intent);
        }

        if (id == R.id.site) {
            Uri uri = Uri.parse("http://museudezoologia.unesc.net");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return FragmentMain.newInstance(mTs);
            } else {
                return FragmentEscrever.newInstance("","");
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return UsuarioLogado ? 2 : 1;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag != null) {
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {
                if (isWrite) {
                    mNfcWriteFragment = (FragmentNFC) getFragmentManager().findFragmentByTag(FragmentNFC.TAG);
                    mNfcWriteFragment.onNfcDetected(ndef);
                    mNfcWriteFragment.dismiss();
                }
            }
            else {
                try {
                    ndef.connect();
                    NdefMessage ndefMessage = ndef.getNdefMessage();
                    String message = new String(ndefMessage.getRecords()[0].getPayload());

                    try {
                        Realm realm = Realm.getDefaultInstance();
                        ItemMuseu lItem = realm.where(ItemMuseu.class).equalTo("id", Integer.valueOf(message)).findFirst();

                        if (lItem != null) {
                            FalarItem(lItem);
                        }else{
                            mTs.speak("Nenhum item encontrado!", TextToSpeech.QUEUE_ADD, null, "");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    ndef.close();
                } catch (IOException | FormatException e) {
                    e.printStackTrace();

                }
            }

        }
    }

    private void FalarItem(ItemMuseu aItemMuseu){
        if (!aItemMuseu.getNome().isEmpty()){
            mTs.speak("Nome", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getNome(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getDescricao().isEmpty()){
            mTs.speak("uma curiosidade", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getDescricao(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getFamilia().isEmpty()){
            mTs.speak("a família", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getFamilia(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getDistribuicaoGeografica().isEmpty()){
            mTs.speak("A distribuição geográfica é ", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getDistribuicaoGeografica(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getHabitat().isEmpty()){
            mTs.speak("o habitat é", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getHabitat(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getHabitosAlimentares().isEmpty()){
            mTs.speak("Os Hábitos Alimentares são", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getHabitosAlimentares(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getOrdem().isEmpty()){
            mTs.speak("A ordem é", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getOrdem(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getPeriodoDeVida().isEmpty()){
            mTs.speak("Seu período de vida é", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getPeriodoDeVida(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getReproducao().isEmpty()){
            mTs.speak("Reprodução", TextToSpeech.QUEUE_ADD, null, "");
            mTs.speak(aItemMuseu.getReproducao(), TextToSpeech.QUEUE_ADD, null, "");
        }

    }
}
