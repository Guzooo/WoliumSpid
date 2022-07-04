package pl.Guzooo.WoliumSpid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

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
    private TextView title;
    private View delete;
    private NumberPicker orderPicker;
    private NumberPicker volumePicker;
    private EditText speedNext;
    private EditText speedBack;
    private View speedBackHelp;
    private View helpText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View layout = getActivity().getLayoutInflater().inflate(R.layout.fragment_stage, null);
        initialization(layout);
        setTitle();
        setDelete();
        setOrderPicker();
        setVolumePicker();
        setSpeedStart();
        setSpeedBack();
        setSpeedBackHelp();
        return getAlertDialog(layout);
    }

    public void show(FragmentManager manager){
        super.show(manager, TAG);
    }

    private void initialization(View v){
        viewModel = new ViewModelProvider(requireActivity()).get(StageViewModel.class);
        stage = viewModel.getStage();
        title = v.findViewById(R.id.title);
        delete = v.findViewById(R.id.delete);
        orderPicker = v.findViewById(R.id.order_picker);
        volumePicker = v.findViewById(R.id.volume_picker);
        speedNext = v.findViewById(R.id.speed_next_edit);
        speedBack = v.findViewById(R.id.speed_back_edit);
        speedBackHelp = v.findViewById(R.id.speed_back_help);
        helpText = v.findViewById(R.id.help_text);
    }

    private void setTitle(){
        title.setText(getTitleResource());
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
        orderPicker.setWrapSelectorWheel(false);
        if(isNewStage())
            orderPicker.setValue(maxOrder);
        else
            orderPicker.setValue(stageOrder);
        orderPicker.setOnValueChangedListener((numberPicker, oldVal,  newVal) -> {
            int nextStageOrder;
            if(!isNewStage() && newVal >= stageOrder)
                nextStageOrder = newVal+1;
            else
                nextStageOrder = newVal;
            viewModel.refreshNextStageSpeedNext(stage.getProfileId(), nextStageOrder);
        });
    }

    private void setVolumePicker(){
        int maxVolume = VolumeUtils.getVolumeMax(getContext());
        int stageVolume = stage.getVolume();
        volumePicker.setMaxValue(maxVolume);
        volumePicker.setWrapSelectorWheel(false);
        volumePicker.setValue(stageVolume);
    }

    private void setSpeedStart(){
        if(!isNewStage()) {
            String text = stage.getSpeedNext() + "";
            speedNext.setText(text);
        }
    }

    private void setSpeedBack(){
        float speed = stage.getSpeedBack();
        if(!isNewStage() && speed != -1) {
            String text = speed + "";
            speedBack.setText(text);
        }
        viewModel.refreshNextStageSpeedNext(stage.getProfileId(), orderPicker.getValue()+1);
        viewModel.getNextStageSpeedNext().observe(this, aFloat -> {
            speedBack.setHint(aFloat + "");
        });
    }

    private void setSpeedBackHelp(){
        speedBackHelp.setOnClickListener(view -> reverseVisibilityOfHelpText());
    }

    private AlertDialog getAlertDialog(View layout){
        return new AlertDialog.Builder(getContext())
                .setView(layout)
                .setPositiveButton(android.R.string.ok, getPositiveButtonListener())
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(getNeutralButtonTextResource(), getNeutralButtonListener())
                .create();
    }

    private int getTitleResource(){
        if(isNewStage())
            return R.string.add_stage;
        return R.string.edit_stage;
    }

    private void reverseVisibilityOfHelpText(){
        if(helpText.getVisibility() == View.VISIBLE)
            setVisibilityOfHelpText(false);
        else
            setVisibilityOfHelpText(true);
    }

    private void setVisibilityOfHelpText(boolean visible){
        if(visible)
            helpText.setVisibility(View.VISIBLE);
        else
            helpText.setVisibility(View.GONE);
    }

    private DialogInterface.OnClickListener getPositiveButtonListener(){
        return (dialogInterface, i) -> WSDatabase.getExecutor().execute(this::saveStage);
    }

    private int getNeutralButtonTextResource(){
        if(isNewStage())
            return R.string.add_another;
        return R.string.add_new;
    }

    private DialogInterface.OnClickListener getNeutralButtonListener(){
        return (dialogInterface, i) -> {
            dismiss();
            WSDatabase.getExecutor().execute(() -> {
                saveStage();
                prepareNewStage();
            });
            show(getParentFragmentManager());
        };
    }

    private View.OnClickListener getDeleteListener(){
        return view -> {
            dismiss();
            viewModel.deleteStage();
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
        stage.setSpeedNext(EditTextUtils.getFloat(speedNext, 0));
        stage.setSpeedBack(EditTextUtils.getFloat(speedBack, -1));
        viewModel.applyStageChange();
    }

    private void prepareNewStage(){
        Stage newStage = new Stage();
        newStage.setProfileId(stage.getProfileId());
        viewModel.increaseMaxOrder();
        viewModel.setStage(newStage);
    }
}
