package pl.Guzooo.WoliumSpid.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.R;

public class AdapterStage extends ListAdapter<Stage, ViewHolderStage> {
    private StageListener listener;

    public interface StageListener{
        void onClick(Stage stage);
    }

    public AdapterStage(StageListener listener){
        super(getDiffCallback());
        this.listener = listener;
    }

    @Override
    public ViewHolderStage onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stage_card_view, parent, false);
        return new ViewHolderStage(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderStage holder, int position) {
        Stage stage = getItem(position);
        View.OnClickListener onClickListener = getOnClickListener(stage);
        holder.setStageInfo(stage);
        holder.setTopArrows(position);
        holder.setOnClickMainViewListener(onClickListener);
    }

    private static DiffUtil.ItemCallback<Stage> getDiffCallback(){
        return new DiffUtil.ItemCallback<Stage>() {
            @Override
            public boolean areItemsTheSame(Stage oldItem, Stage newItem) {
                if(oldItem.getId() == newItem.getId())
                    return true;
                return false;
            }

            @Override
            public boolean areContentsTheSame(Stage oldItem, Stage newItem) {
                if(oldItem.getOrder() != newItem.getOrder())
                    return false;
                if(oldItem.getVolume() != newItem.getVolume())
                    return false;
                if(oldItem.getSpeed() != newItem.getSpeed())
                    return false;
                if(oldItem.isActive() != newItem.isActive())
                    return false;
                return true;
            }
        };
    }

    private View.OnClickListener getOnClickListener(Stage stage){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(stage);
            }
        };
    }
}
