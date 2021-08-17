package pl.Guzooo.WoliumSpid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import pl.Guzooo.Base.Utils.EditTextUtils;
import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.Database.WSDatabase;
import pl.Guzooo.WoliumSpid.Utils.VolumeUtils;

public class StageFragment extends DialogFragment {

    private final String TAG = "addstage";

    private StageViewModel viewModel;
    private Stage stage;
    private View delete;
    private NumberPicker orderPicker;
    private NumberPicker volumePicker;
    private EditText speedStart;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View layout = getActivity().getLayoutInflater().inflate(R.layout.fragment_stage, null);
        initialization(layout);
        setDelete();
        setOrderPicker();
        setVolumePicker();
        setSpeedStart();
        return getAlertDialog(layout);
    }

    public void show(FragmentManager manager){
        super.show(manager, TAG);
    }

    private void initialization(View v){
        viewModel = new ViewModelProvider(requireActivity()).get(StageViewModel.class);
        stage = viewModel.getStage();
        delete = v.findViewById(R.id.delete);
        orderPicker = v.findViewById(R.id.order_picker);
        volumePicker = v.findViewById(R.id.volume_picker);
        speedStart = v.findViewById(R.id.speed_edit);
    }

    private AlertDialog getAlertDialog(View layout){
        return new AlertDialog.Builder(getContext())
                .setTitle(getTitleResource())
                .setView(layout)
                .setPositiveButton(android.R.string.ok, getPositiveButtonListener())
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(getNeutralButtonTextResource(), getNeutralButtonListener())
                .create();
    }

    private void setDelete(){
        if(isNewStage())
            delete.setVisibility(View.GONE);
        else
            delete.setOnClickListener(getDeleteListener());
    }

    private void setOrderPicker(){
        int maxOrder = viewModel.getMaxOrder();
        int stageOrder = stage.getOrder();
        orderPicker.setMinValue(1);
        orderPicker.setMaxValue(maxOrder);
        if(isNewStage())
            orderPicker.setValue(maxOrder);
        else
            orderPicker.setValue(stageOrder);
    }

    private void setVolumePicker(){
        int maxVolume = VolumeUtils.getVolumeMax(getContext());
        int stageVolume = stage.getVolume();
        volumePicker.setMaxValue(maxVolume);
        volumePicker.setValue(stageVolume);
    }

    private void setSpeedStart(){
        if(!isNewStage()) {
            String text = stage.getSpeed() + "";
            speedStart.setText(text);
        }
    }

    private int getTitleResource(){
        if(isNewStage())
            return R.string.add_stage;
        return R.string.edit_stage;
    }

    private DialogInterface.OnClickListener getPositiveButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WSDatabase.getExecutor().execute(() -> {
                    saveStage();
                });
            }
        };
    }

    private int getNeutralButtonTextResource(){
        if(isNewStage())
            return R.string.add_another;
        return R.string.add_new;
    }

    private DialogInterface.OnClickListener getNeutralButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
                WSDatabase.getExecutor().execute(() -> {
                    saveStage();
                    prepareNewStage();
                });
                show(getParentFragmentManager());
            }
        };
    }

    private View.OnClickListener getDeleteListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                viewModel.deleteStage();
            }
        };
    }

    private boolean isNewStage(){
        if(stage.getId() == 0)
            return true;
        return false;
    }

    private void saveStage(){
        stage.setOrder(orderPicker.getValue());
        stage.setVolume(volumePicker.getValue());
        stage.setSpeed(EditTextUtils.getFloat(speedStart, 0));
        viewModel.applyStageChange();
    }

    private void prepareNewStage(){
        Stage newStage = new Stage();
        newStage.setProfileId(stage.getProfileId());
        viewModel.increaseMaxOrder();
        viewModel.setStage(newStage);
    }
}
