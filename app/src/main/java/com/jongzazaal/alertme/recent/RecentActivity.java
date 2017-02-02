package com.jongzazaal.alertme.recent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jongzazaal.alertme.MapActivity;
import com.jongzazaal.alertme.R;
import com.jongzazaal.alertme.databaseAlarm.ControlDatabase;

/**
 * Created by jongzazaal on 17/12/2559.
 */

public class RecentActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_layout);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7061678296174983~8218986555");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A5B51C44F9111E310325F0F0A8CFDD23")
                .build();
        mAdView.loadAd(adRequest);

        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MapActivity.class);

                //role 0->new date
                //role 1->recent date
                i.putExtra("role", 0);
                startActivityForResult(i, 0);
            }
        });



        //From Here Starts All The Swipe To
        // Refresh Initialisation And Setter Methods.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);//Initialising
        //Setting Up OnRefresh Listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                //onRefresh method is used to perform all the action
                // when the swipe gesture is performed.
                try {
                    //new RecentCustomAdapter().clearData();
                    initializeRecyclerView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //This are some optional methods for customizing
        // the colors and size of the loader.
        swipeRefreshLayout.setColorSchemeResources(
                R.color.blue,       //This method will rotate
                R.color.red,        //colors given to it when
                R.color.yellow,     //loader continues to
                R.color.green);     //refresh.

        //setSize() Method Sets The Size Of Loader
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //Below Method Will set background color of Loader
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager = new StaggeredGridLayoutManager(3,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (fab.isShown()) {
                        fab.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }


        });
        initializeRecyclerView();
    }
    public  void initializeRecyclerView(){

        fab.show();

        mAdapter = new RecentCustomAdapter(getApplicationContext(), ControlDatabase.getInstance(getApplicationContext()).viewRecent());
        new RecentCustomAdapter(getApplicationContext(), ControlDatabase.getInstance(getApplicationContext()).viewRecent()).clearData();
        mAdapter = new RecentCustomAdapter(getApplicationContext(), ControlDatabase.getInstance(getApplicationContext()).viewRecent());

//        ((MyAdapter) mAdapter).setMode(Attributes.Mode.Single);
//        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.invalidate();


        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);

        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        initializeRecyclerView();
//        Toast.makeText(this, "re naja", Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        initializeRecyclerView();
//    }

    @Override
    public void onResume() {
        super.onResume();
        initializeRecyclerView();
    }
}
