package com.guilhermedaros.museunfc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.guilhermedaros.museunfc.Acitivities.MainActivity;
import com.guilhermedaros.museunfc.Entities.ItemMuseu;
import com.guilhermedaros.museunfc.Listeners.Listener;
import com.guilhermedaros.museunfc.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class FragmentEscrever extends Fragment implements Listener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MaterialSpinner mMsMessage;
    private Button mBtWrite;

    private View lView;

    private boolean isDialogDisplayed = false;
    public boolean isWrite = false;

    private FragmentNFC mNfcWriteFragment;


    public FragmentEscrever() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEscrever.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEscrever newInstance(String param1, String param2) {
        FragmentEscrever fragment = new FragmentEscrever();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private List<String> ItensMuseu(){

        Realm realm = Realm.getDefaultInstance();
        List<ItemMuseu> lItens = realm.where(ItemMuseu.class).findAll();

        List<String> strings = new ArrayList<>(lItens.size());

        for (ItemMuseu object : lItens) {
            strings.add(object.getNome());
        }

        return strings;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lView = inflater.inflate(R.layout.fragment_escrever, container, false);
        mMsMessage = (MaterialSpinner) lView.findViewById(R.id.ms_message);

        MaterialSpinner spinner = (MaterialSpinner) lView.findViewById(R.id.ms_message);

        spinner.setItems(ItensMuseu());

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            }
        });

        mBtWrite = (Button) lView.findViewById(R.id.btn_write);

        mBtWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                ItemMuseu itemMuseu = realm.where(ItemMuseu.class).equalTo("nome", mMsMessage.getText().toString()).findFirst();
                int lId = itemMuseu.getId();
                ((MainActivity) getActivity()).showWriteFragment(String.valueOf(lId));
                mMsMessage.setText("");
            }
        });
        return lView;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
