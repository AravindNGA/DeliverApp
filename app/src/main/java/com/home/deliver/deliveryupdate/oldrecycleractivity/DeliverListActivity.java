package com.home.deliver.deliveryupdate.oldrecycleractivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;


import com.home.deliver.deliveryupdate.MapsActivity;
import com.home.deliver.deliveryupdate.NewDeliveryData;
import com.home.deliver.deliveryupdate.R;

import java.util.ArrayList;


public class DeliverListActivity extends AppCompatActivity implements DeliverItemAdapter.ItemClickCallBack {


    RecyclerView review;
    DeliverItemAdapter adapter;
    private ArrayList listData;
    private static final int REQUEST_INTENT = 1;

    FloatingActionButton addButton;

    public static final String BUNDLE_STRING = "BUNDLE_STRING";
    public static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String BUNDLE_DIST = "BUNDLE_DIST";
    public static final String BUNDLE_NAME = "BUNDLE_INT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        review = (RecyclerView) findViewById(R.id.recyclerViewmain);
        review.setLayoutManager(new LinearLayoutManager(this));
        addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        listData = new ArrayList<>();
        adapter = new DeliverItemAdapter(listData, this);
        prepareData();

        review.setAdapter(adapter);
        adapter.setItemClickCallBack(this);



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
        itemTouchHelper.attachToRecyclerView(review);

        review.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    addButton.hide();
                else if (dy < 0)
                    addButton.show();
            }
        });

    }

    //for the display of content on the reView
    private void prepareData() {

        adapter.notifyDataSetChanged();

    }

    //delete slide actions declared but no undo is present
    private ItemTouchHelper.Callback createHelperCallBack() {
        ItemTouchHelper.SimpleCallback simpleCallBack = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());
            }
        };
        return simpleCallBack;
    }

    //delete item
    private void deleteItem(int adapterPosition) {
        listData.remove(adapterPosition);
        adapter.notifyItemRemoved(adapterPosition);
        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();

    }

    //slide and move  item
    private void moveItem(int adapterPosition, int adapterPosition1) {
        DeliverListItem item = (DeliverListItem) listData.get(adapterPosition);
        listData.remove(adapterPosition);
        listData.add(adapterPosition1, item);
        adapter.notifyItemMoved(adapterPosition, adapterPosition1);

    }

    //to add new item to the reView
    public void onClicking(View view) {
        Intent in = new Intent(this, NewDeliveryData.class);
        startActivityForResult(in, REQUEST_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INTENT) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(BUNDLE_STRING);
                String Consignees_name = data.getStringExtra(BUNDLE_NAME);
                String distance = data.getStringExtra(BUNDLE_DIST);
                DeliverListItem item = new DeliverListItem(Consignees_name, name, distance);
                listData.add(item);
                adapter.notifyItemInserted(listData.indexOf(item));
                Toast.makeText(getApplicationContext(), "Item Insereted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "No content Recieved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(int p) {
        DeliverListItem item = (DeliverListItem) listData.get(p);
        Toast.makeText(getApplicationContext(), "Clicked ", Toast.LENGTH_SHORT).show();


        Intent in = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_STRING, item.getName());
        bundle.putString(BUNDLE_DIST, item.getDistance());
        bundle.putString(BUNDLE_NAME, item.getLatlng());
        in.putExtra(BUNDLE_EXTRAS, bundle);
        startActivity(in);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("If you exit this app the data stored will be earsed, Since we Havent Stored this data any where \nDo you really wanna exit?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //thankyou
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}


