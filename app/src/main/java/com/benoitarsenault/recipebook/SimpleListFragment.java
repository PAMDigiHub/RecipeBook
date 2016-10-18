package com.benoitarsenault.recipebook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.benoitarsenault.recipebook.dialogs.EditItemDialogFragment;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSimpleListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimpleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleListFragment extends android.support.v4.app.Fragment implements EditItemDialogFragment.EditItemDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_ADD = "add";
    private static final String TAG_RENAME = "rename";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> items;

    private ArrayAdapter<String> adapter;
    private ImageButton addButton;


    private OnSimpleListFragmentInteractionListener mListener;

    public SimpleListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SimpleListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimpleListFragment newInstance(String param1, String param2) {
        SimpleListFragment fragment = new SimpleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        items = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(),R.layout.fragment_simple_list_item,R.id.fragment_simple_list_item_textview,items);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_simple_list, container, false);

        ListView listView = (ListView) layout.findViewById(R.id.fragment_simple_list_listview);
        listView.setAdapter(adapter);



        addButton = (ImageButton) layout.findViewById(R.id.fragment_simple_list_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemDialogFragment dialog = EditItemDialogFragment.newInstance("","Add item","Add");
                dialog.setTargetFragment(SimpleListFragment.this,0);
                dialog.show(getFragmentManager(),TAG_ADD);
            }
        });

        return layout;
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSimpleListFragmentItemsChanged(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSimpleListFragmentInteractionListener) {
            mListener = (OnSimpleListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSimpleListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onEditItemDialogPositiveClick(String tag, int requestCode, String newText) {

        if(tag.equals(TAG_ADD)) {
            addItem(newText);
        }
        mListener.onSimpleListFragmentItemsChanged(getId());

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
    public interface OnSimpleListFragmentInteractionListener {
        void onSimpleListFragmentItemsChanged(int fragmentId);
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
        adapter.notifyDataSetChanged();
    }

    public void addItem(String item){
        items.add(item);
        adapter.notifyDataSetChanged();
    }
    public void removeItem(int index){
        items.remove(index);
        adapter.notifyDataSetChanged();
    }
}
