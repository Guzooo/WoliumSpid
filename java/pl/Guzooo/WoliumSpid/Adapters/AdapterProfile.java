package pl.Guzooo.WoliumSpid.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

import pl.Guzooo.WoliumSpid.Database.Profile;
import pl.Guzooo.WoliumSpid.Database.ProfileWithStages;
import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.R;

public class AdapterProfile extends ListAdapter<ProfileWithStages, ViewHolderProfile> {

    private ProfileListener listener;

    public interface ProfileListener{
        void onClickMainView(int id);
        void onClickPlay(ProfileWithStages profileWithStages);
    }

    public AdapterProfile(ProfileListener listener) {
        super(getDiffCallback());
        this.listener = listener;
    }

    @Override
    public ViewHolderProfile onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card_view, parent, false);
        return new ViewHolderProfile(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderProfile holder, int position) {
        ProfileWithStages profileWithStages = getItem(position);
        Profile profile = profileWithStages.getProfile();
        List<Stage> stages = profileWithStages.getStages();
        holder.setVolumeBars(stages);
        holder.setTitle(profile);
        setClickListeners(profileWithStages, holder);
    }

    private static DiffUtil.ItemCallback<ProfileWithStages> getDiffCallback(){
        return new DiffUtil.ItemCallback<ProfileWithStages>() {
            @Override
            public boolean areItemsTheSame(ProfileWithStages oldItem, ProfileWithStages newItem) {
                Profile oldProfile = oldItem.getProfile();
                Profile newProfile = newItem.getProfile();
                if(oldProfile.getId() == newProfile.getId())
                    return true;
                return false;
            }

            @Override
            public boolean areContentsTheSame(ProfileWithStages oldItem, ProfileWithStages newItem) {
                Profile oldProfile = oldItem.getProfile();
                Profile newProfile = newItem.getProfile();
                List<Stage> oldStages = oldItem.getStages();
                List<Stage> newStages = newItem.getStages();
                if(!oldProfile.getName().equals(newProfile.getName()))
                    return false;
                if (oldProfile.getName().equals("") && oldProfile.getId() != newProfile.getId())
                     return false;
                if(oldStages.size() != newStages.size())
                    return false;
                for(int i = 0; i < 10 && i < oldStages.size(); i++){
                    if(oldStages.get(i).getVolume() != newStages.get(i).getVolume())
                        return false;
                }
                return true;
            }
        };
    }

    private void setClickListeners(ProfileWithStages profileWithStages, ViewHolderProfile holder){
        int id = profileWithStages.getProfile().getId();
        View.OnClickListener onClickMainViewListener = getOnClickMainViewListener(id);
        View.OnClickListener onClickPlayListener = getOnClickPlayListener(profileWithStages);
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

    private View.OnClickListener getOnClickPlayListener(ProfileWithStages profileWithStages){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickPlay(profileWithStages);
            }
        };
    }
}