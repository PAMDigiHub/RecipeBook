package com.benoitarsenault.recipebook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.benoitarsenault.recipebook.dialogs.AddFragmentItemDialog;
import com.benoitarsenault.recipebook.dialogs.DeleteFragmentItemDialog;
import com.benoitarsenault.recipebook.dialogs.UpdateFragmentItemDialog;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSimpleListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimpleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleListFragment extends android.support.v4.app.Fragment implements AddFragmentItemDialog.AddFragmentItemDialogListener,UpdateFragmentItemDialog.UpdateFragmentItemDialogListener, DeleteFragmentItemDialog.DeleteFragmentItemDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_ADD = "add";
    private static final String TAG_RENAME = "rename";
    private static final String TAG_UPDATE = "update";
    private static final String TAG_DELETE = "delete" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private boolean displayOrder = true;
    private String title;
    private ImageButton addButton;
    private TextView titleTextView;



    private OnSimpleListFragmentInteractionListener mListener;

    public SimpleListFragment() {
        // Required empty public constructor
    }


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

        titleTextView = (TextView) layout.findViewById(R.id.fragment_simple_name_textview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                UpdateFragmentItemDialog dialog = UpdateFragmentItemDialog.newInstance(item,position);
                dialog.setTargetFragment(SimpleListFragment.this,0);
                dialog.show(getFragmentManager(),TAG_UPDATE);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteFragmentItemDialog dialog = DeleteFragmentItemDialog.newInstance(position);
                dialog.setTargetFragment(SimpleListFragment.this,0);
                dialog.show(getFragmentManager(),TAG_DELETE);
                return true;
            }
        });

        addButton = (ImageButton) layout.findViewById(R.id.fragment_simple_list_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFragmentItemDialog dialog = AddFragmentItemDialog.newInstance();
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
    public void addFragmentItemDialogPositiveClick(String tag, String newText) {
            addItem(newText);
        mListener.onSimpleListFragmentItemsChanged(getId());
    }

    @Override
    public void updateFragmentItemDialogPositiveClick(String tag, String newText, int position) {
        update(newText,position);
        mListener.onSimpleListFragmentItemsChanged(getId());
    }

    @Override
    public void deleteFragmentItemDialogPositiveClick(String tag, int position) {
        deleteItem(position);
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

    public void setItems(ArrayList<String> newItems) {
        //Need to manually add each item to avoid losing the adapter's data reference
        items.clear();
        for(String item : newItems){
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    public void addItem(String item){
        items.add(item);
        adapter.notifyDataSetChanged();
    }


    public void deleteItem(int index){
        items.remove(index);
        adapter.notifyDataSetChanged();
    }

    private void update(String newText,int position) {
        items.set(position,newText);
        adapter.notifyDataSetChanged();
    }



    public boolean isDisplayOrderEnabled() {
        return displayOrder;
    }

    public void setDisplayOrderEnabled(boolean displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
        titleTextView.setText(title);
    }


}
