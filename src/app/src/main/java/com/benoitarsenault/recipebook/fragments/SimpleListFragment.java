package com.benoitarsenault.recipebook.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.benoitarsenault.recipebook.R;
import com.benoitarsenault.recipebook.dialogs.AddFragmentItemDialog;
import com.benoitarsenault.recipebook.dialogs.ManageFragmentItemDialog;
import com.benoitarsenault.recipebook.dialogs.UpdateFragmentItemDialog;

import java.util.ArrayList;


public class SimpleListFragment extends android.support.v4.app.Fragment implements AddFragmentItemDialog.AddFragmentItemDialogListener, UpdateFragmentItemDialog.UpdateFragmentItemDialogListener, ManageFragmentItemDialog.ManageFragmentItemDialogListener {

    private static final String TAG_ADD = "add";
    private static final String TAG_UPDATE = "update";
    private static final String TAG_MANAGE = "manage";

    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private boolean displayOrder = true;
    private String title;
    private ImageButton addButton;
    private TextView titleTextView;

    public static SimpleListFragment newInstance() {
        SimpleListFragment fragment = new SimpleListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), R.layout.fragment_simple_list_item, R.id.fragment_simple_list_item_textview, items) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (isDisplayOrderEnabled()) {
                    TextView textView = (TextView) view.findViewById(R.id.fragment_simple_list_item_textview);
                    textView.setText((position + 1) + " - " + textView.getText());
                }
                return view;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_simple_list, container, false);

        titleTextView = (TextView) layout.findViewById(R.id.fragment_simple_name_textview);

        ListView listView = (ListView) layout.findViewById(R.id.fragment_simple_list_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                UpdateFragmentItemDialog dialog = UpdateFragmentItemDialog.newInstance(item, position);
                dialog.setTargetFragment(SimpleListFragment.this, 0);
                dialog.show(getFragmentManager(), TAG_UPDATE);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ManageFragmentItemDialog dialog = ManageFragmentItemDialog.newInstance(position, adapter.getItem(position));
                dialog.setTargetFragment(SimpleListFragment.this, 0);
                dialog.show(getFragmentManager(), TAG_MANAGE);
                return true;
            }
        });

        addButton = (ImageButton) layout.findViewById(R.id.fragment_simple_list_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFragmentItemDialog dialog = AddFragmentItemDialog.newInstance();
                dialog.setTargetFragment(SimpleListFragment.this, 0);
                dialog.show(getFragmentManager(), TAG_ADD);
            }
        });

        return layout;
    }

    //-----------------
    // CRUD
    //-----------------

    public void addItem(String item) {
        items.add(item);
        adapter.notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        items.remove(index);
        adapter.notifyDataSetChanged();
    }

    private void update(String newText, int position) {
        items.set(position, newText);
        adapter.notifyDataSetChanged();
    }

    //----------------
    // Dialog events
    //----------------
    @Override
    public void addFragmentItemDialogPositiveClick(String tag, String newText) {
        addItem(newText);
    }

    @Override
    public void updateFragmentItemDialogPositiveClick(String tag, String newText, int position) {
        update(newText, position);
    }

    @Override
    public void manageFragmentItemDialogUpdateClick(String tag, int position, String newText) {
        update(newText, position);
    }

    @Override
    public void manageFragmentItemDialogDeleteClick(String tag, int position) {
        deleteItem(position);
    }

    //--------------------
    // Getter setter
    //--------------------
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleTextView.setText(title);
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> newItems) {
        //Need to manually add each item to avoid losing the adapter's data reference
        items.clear();
        for (String item : newItems) {
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    public boolean isDisplayOrderEnabled() {
        return displayOrder;
    }

    public void setDisplayOrderEnabled(boolean displayOrder) {
        this.displayOrder = displayOrder;
    }
}
