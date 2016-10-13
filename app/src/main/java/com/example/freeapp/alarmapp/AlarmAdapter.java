package com.example.freeapp.alarmapp;

/**
 * Created by vinove on 13/10/16.
 */




        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.Switch;
        import android.widget.TextView;

        import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyViewHolder> {

    private List<AlarmModel> alarmModelList;
    DatabaseHandler databaseHandler;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, dateTime;
        public ImageView crossImage;
        public Switch toggleSwitch;

        public MyViewHolder(View view) {
            super(view);
             title=(TextView)view.findViewById(R.id.title);
              dateTime=(TextView)view.findViewById(R.id.dataTime);
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
        holder.dateTime.setText(model.getDateTime());
        holder.crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 databaseHandler.deleteRecord(model.getId());
                alarmModelList.remove(position);
                 notifyItemRemoved(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return alarmModelList.size();
    }
}