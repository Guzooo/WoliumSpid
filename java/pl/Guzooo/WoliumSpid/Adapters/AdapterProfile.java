package pl.Guzooo.WoliumSpid.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.Guzooo.WoliumSpid.Database.Profile;
import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.R;

public class AdapterProfile extends RecyclerView.Adapter<ViewHolderProfile> {

    private ProfileListener listener;
    private ArrayList<Profile> profiles = new ArrayList<>();

    AdapterProfile (ArrayList<Profile> profiles, ProfileListener listener){
        this.profiles = profiles;
        this.listener = listener;
    }

    @Override
    public ViewHolderProfile onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card_view, parent, false);
        return new ViewHolderProfile(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderProfile holder, int position) {
        Profile profile = profiles.get(position);
        setVolumeBars(profile, holder);
        setTitle(profile, holder);
        setClickListeners(profile, holder);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    private interface ProfileListener{
        void onClickMainView(int id);
        void onClickPlay(Profile profile);
    }

    private void setVolumeBars(Profile profile, ViewHolderProfile holder){
        for(int i = 0; i < 10; i++){
            Stage stage = profile.getStage(i);
            if(stage != null) {
                int volume = stage.getVolume();
                holder.setVolumeBar(i, volume);
            } else
                holder.setVolumeBarGone(i);
        }
    }

    private void setTitle(Profile profile, ViewHolderProfile holder){
        String title = profile.getName(holder.itemView.getContext());
        holder.setTitle(title);
    }

    private void setClickListeners(Profile profile, ViewHolderProfile holder){
        View.OnClickListener onClickMainViewListener = getOnClickMainViewListener(profile.getId());
        View.OnClickListener onClickPlayListener = getOnClickPlayListener(profile);
        holder.setOnClickMainViewListener(onClickMainViewListener);
        holder.setOnClickPlayListener(onClickPlayListener);
    }

    private View.OnClickListener getOnClickMainViewListener(int id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickMainView(id);
            }
        };
    }

    private View.OnClickListener getOnClickPlayListener(Profile profile){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickPlay(profile);
            }
        };
    }
}