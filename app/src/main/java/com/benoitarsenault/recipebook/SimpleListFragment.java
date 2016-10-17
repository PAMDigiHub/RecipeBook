package com.benoitarsenault.recipebook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.benoitarsenault.recipebook.dialogs.EditItemDialogFragment;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SimpleListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimpleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleListFragment extends Fragment implements EditItemDialogFragment.EditItemDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_ITEMS = "items";
    private static final String TAG_ADD = "add";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private ArrayList<String> mItems;
    ArrayAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public SimpleListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param items Parameter 2.
     * @return A new instance of fragment SimpleListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimpleListFragment newInstance(String title, ArrayList<String> items) {
        SimpleListFragment fragment = new SimpleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putStringArrayList(ARG_ITEMS, items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mItems = getArguments().getStringArrayList(ARG_ITEMS);

            adapter = new ArrayAdapter(getContext(),R.layout.fragment_simple_list,R.id.fragment_simple_list_item_textview);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_simple_list, container, false);
        ListView listview = (ListView) layout.findViewById(R.id.fragment_simple_item_listview);
        listview.setAdapter(adapter);

        ImageButton addButton = (ImageButton) layout.findViewById(R.id.fragment_simple_item_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemDialogFragment dialog = EditItemDialogFragment.newInstance("","Add new item");
                dialog.show(getFragmentManager(),TAG_ADD);
            }
        });


        return layout;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ArrayList<String> getItems() {
        return mItems;
    }

    @Override
    public void onEditItemDialogPositiveClick(EditItemDialogFragment dialogFragment, String newText) {
        mItems.add(newText);
        adapter.notifyDataSetChanged();
    }
/*
    public void setItems(ArrayList<String> mItems) {
        this.mItems = mItems;
    }*/

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
