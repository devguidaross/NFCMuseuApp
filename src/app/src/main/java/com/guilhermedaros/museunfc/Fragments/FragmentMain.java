package com.guilhermedaros.museunfc.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.guilhermedaros.museunfc.Entities.ItemMuseu;
import com.guilhermedaros.museunfc.R;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static TextToSpeech mTs;

    private View lView;

    private OnFragmentInteractionListener mListener;

    public FragmentMain() {
        // Required empty public constructor
    }

    /**
     *
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(TextToSpeech aTextToSpeech) {
        FragmentMain fragment = new FragmentMain();
        mTs = aTextToSpeech;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void FalarItem(ItemMuseu aItemMuseu){
        if (!aItemMuseu.getNome().isEmpty()){
            mTs.speak(aItemMuseu.getNome(), TextToSpeech.QUEUE_ADD, null, "");
        }

        if (!aItemMuseu.getDescricao().isEmpty()){
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lView = inflater.inflate(R.layout.fragment_main, container, false);
        lView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTs.isSpeaking()){
                    mTs.stop();
                }
            }
        });
        return lView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
