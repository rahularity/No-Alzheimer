package com.ithought.rahul.nozimers.PeopleAroundYou;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ithought.rahul.nozimers.models.Person;
import com.ithought.rahul.nozimers.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rahul on 10/19/2017.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ChildViewHolder> {

    private List<Person> person;
    private int layout;
    private Context context;

    public PersonAdapter(List<Person> person, int layout, Context context) {
        this.person = person;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent ,false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        holder.name.setText(person.get(position).getName());
        holder.livesIn.setText(person.get(position).getLives_in());
        holder.placeOfMeeting.setText(person.get(position).getPlace_of_meeting());
        holder.relation.setText(person.get(position).getRelation());


        String url = "http://18.215.250.153:8080" + person.get(position).getImage_url();
        Picasso.get().load(url).into(holder.personPic);

    }

    @Override
    public int getItemCount() {
        return person.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder{

        CircleImageView personPic;
        TextView name;
        TextView livesIn;
        TextView placeOfMeeting;
        TextView relation;

        public ChildViewHolder(View itemView) {
            super(itemView);
            personPic = (CircleImageView) itemView.findViewById(R.id.person_photo);
            name = (TextView)itemView.findViewById(R.id.name);
            livesIn = (TextView)itemView.findViewById(R.id.lives_in);
            placeOfMeeting = (TextView)itemView.findViewById(R.id.place_of_meeting);
            relation = (TextView)itemView.findViewById(R.id.relation_with_you);
        }
    }
}
