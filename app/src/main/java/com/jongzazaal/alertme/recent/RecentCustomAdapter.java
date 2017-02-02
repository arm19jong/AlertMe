package com.jongzazaal.alertme.recent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.jongzazaal.alertme.MapActivity;
import com.jongzazaal.alertme.R;
import com.jongzazaal.alertme.ServiceLoc;
import com.jongzazaal.alertme.SwitchService;
import com.jongzazaal.alertme.databaseAlarm.ControlDatabase;
import com.jongzazaal.alertme.databaseAlarm.RecentClass;
import com.jongzazaal.alertme.databaseAlarm.SingleDBalarmMe;

import java.util.List;

/**
 * Created by jongzazaal on 17/12/2559.
 */


public class RecentCustomAdapter extends RecyclerSwipeAdapter<RecentCustomAdapter.ViewHolder> {
    Context context;
    private RecentClass mDataSet;

    RecyclerView.Adapter adapter;


    public void clearData() {
//        mDataSet.removeAll(mDataSet);

        mDataSet.ClearDate();
        notifyDataSetChanged();
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        TextView mTitle, mPlaceName, tvDel;
        RelativeLayout tvDel;
        TextView placeName;
        Switch aSwitch;
        public SwipeLayout swipeLayout;

        CardView cardView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            tvDel = (RelativeLayout) v.findViewById(R.id.Del);
            placeName = (TextView) v.findViewById(R.id.placeName);
            aSwitch = (Switch) v.findViewById(R.id.switchOnOff);

            itemView.setTag(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);

        }

    }

    public RecentCustomAdapter(Context context, RecentClass mDataSet){
        this.context = context;
        this.mDataSet = mDataSet;


    }

//    public void swap(List<CenteridValue> mDataSetL){
//        if (mDataSet != null) {
//            mDataSet.clear();
//            mDataSet=mDataSetL;
//        }
//        else {
//            mDataSet=mDataSetL;
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public RecentCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_layout_control, parent, false);
        // set the view's size, margins, paddings and layout parameters

        RecentCustomAdapter.ViewHolder vh = new RecentCustomAdapter.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final RecentCustomAdapter.ViewHolder viewHolder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        final CenteridValue centeridValue = mDataSet.get(position);
        viewHolder.placeName.setText(mDataSet.getPlaceName().get(position));

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        //viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));

//        viewHolder.mTitle.setText(valuesF.getTitles());
//        viewHolder.mPlaceName.setText(valuesF.getPlaceName());
//        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControlDatabase.getInstance(context).delRecord(mDataSet.getId().get(position));
                mDataSet.removeItem(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataSet.getId().size());

            }
        });
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleDBalarmMe.getInstance().setAll(mDataSet.getId().get(position),
                        mDataSet.getPlaceName().get(position), mDataSet.getLat().get(position),
                        mDataSet.getLng().get(position), mDataSet.getDistance().get(position));

                Intent i = new Intent(context, MapActivity.class);
                //role 0->new date
                //role 1->recent date
                i.putExtra("role", 1);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
                ((Activity) context).startActivityForResult(i,0);
            }
        });
        viewHolder.aSwitch.setChecked(SwitchService.getInstace().isOnService(mDataSet.getPlaceName().get(position)));

        viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(isChecked){
//                    Toast.makeText(context, "On", Toast.LENGTH_SHORT).show();
                    SwitchService.getInstace().addServiceName(mDataSet.getPlaceName().get(position));
                    ServiceLoc.context = context;
                    Intent intent = new Intent(context, ServiceLoc.class);
                    intent.putExtra("finLat", mDataSet.getLat().get(position));
                    intent.putExtra("finLng", mDataSet.getLng().get(position));
                    intent.putExtra("distance", mDataSet.getDistance().get(position));
                    intent.putExtra("placeName", mDataSet.getPlaceName().get(position));
                    context.startService(intent);
                }
                else {
//                    Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
                    SwitchService.getInstace().removeServiceName(mDataSet.getPlaceName().get(position));
//                    Intent intent = new Intent(context, ServiceLoc.class);
//                    context.stopService(intent);
//                    new ServiceLoc().stopThreadAlert(mDataSet.getPlaceName().get(position));
                }
            }
        });


        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.getPlaceName().size();
    }
//
//    public static void startResultActivity(Context context, String[] date) {
//        Intent intent = new Intent(context, MapActivity.class);
//        //String[] sqr = sqr;
//        //ArrayList<String> sqrr = new ArrayList<String>(sqr);
//        String[] sqrr = (String[]) sqr.toArray(new String[sqr.size()]);
//        intent.putExtra("date", sqrr);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }




}