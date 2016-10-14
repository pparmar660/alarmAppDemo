package com.example.freeapp.alarmapp;

/**
 * Created by vinove on 13/10/16.
 */




        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.CompoundButton;
        import android.widget.ImageView;
        import android.widget.Switch;
        import android.widget.TextView;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyViewHolder> {

    private List<AlarmModel> alarmModelList;
    DatabaseHandler databaseHandler;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, dateTime,date;
        public ImageView crossImage;
        public Switch toggleSwitch;

        public MyViewHolder(View view) {
            super(view);
             title=(TextView)view.findViewById(R.id.title);
              dateTime=(TextView)view.findViewById(R.id.dataTime);
            date=(TextView)view.findViewById(R.id.date);
              crossImage=(ImageView)view.findViewById(R.id.cross);
              toggleSwitch=(Switch) view.findViewById(R.id.switch1);

        }
    }


    public AlarmAdapter(List<AlarmModel> moviesList,DatabaseHandler databaseHandler) {
        this.alarmModelList = moviesList;
        this.databaseHandler=databaseHandler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AlarmModel model = alarmModelList.get(position);
        holder.title.setText(model.getMessage());


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.dateFormat);

        try {
            cal.setTime(sdf.parse(model.getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat("h:mm a");
        holder.dateTime.setText(sdf.format(cal.getTime()));

        sdf = new SimpleDateFormat("EEE MMM d yyyy");
        holder.date.setText(sdf.format(cal.getTime()));


        if(model.getToggle()>0)
            holder.toggleSwitch.setChecked(true);
        else holder.toggleSwitch.setChecked(false);
        holder.crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 databaseHandler.deleteRecord(model.getId());
                 if(alarmModelList.size()>(position)) {
                     alarmModelList.remove((position));
                 }
                 notifyItemRemoved(position);
            }
        });

        holder.toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    databaseHandler.updateRecord(model.getId(),1);
                }
                else  databaseHandler.updateRecord(model.getId(),0);
            }
        });


    }



    @Override
    public int getItemCount() {
        return alarmModelList.size();
    }
}