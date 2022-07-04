package pl.Guzooo.WoliumSpid;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import pl.Guzooo.WoliumSpid.Database.Repository;
import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.Database.WSDatabase;

public class StageViewModel extends AndroidViewModel {
    private Repository repository;
    private Stage stage;
    private int initialOrder;
    private int maxOrder;
    private MutableLiveData<Float> nextStageSpeedNext = new MutableLiveData<>(0.00f);

    public StageViewModel(Application application){
        super(application);
        repository = new Repository(application);
    }

    public void moveStage(int profileId, int positionStart, int positionEnd){
        WSDatabase.getExecutor().execute(() -> {
            Stage stage = repository.getStageFromOneProfileByAdapterPosition(profileId, positionStart);
            setStage(stage);
            int change = positionEnd - positionStart;
            int order = stage.getOrder();
            stage.setOrder(order + change);
            applyStageChange();
        });
    }

    public void applyStageChange(){
        WSDatabase.getExecutor().execute(() -> {
            tidyUpOtherStages();
            if (isNewStage())
                repository.insertStage(stage);
            else
                repository.updateStage(stage);
        });
    }

    public void deleteStage(){
        WSDatabase.getExecutor().execute(() -> {
            List<Stage> stagesToTidy = repository.getStagesFromOneProfileWithOrderGreaterOrEqualsThan(stage.getProfileId(), stage.getOrder());
            doTidy(-1, stagesToTidy);
            repository.deleteStage(stage);
        });
    }

    public Stage getStage(){
        return stage;
    }

    public void setStage(Stage stage){
        this.stage = stage;
        initialOrder = stage.getOrder();
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    public void increaseMaxOrder(){
        maxOrder++;
    }

    public MutableLiveData<Float> getNextStageSpeedNext(){
        return nextStageSpeedNext;
    }

    public void refreshNextStageSpeedNext(int id, int order){
        WSDatabase.getExecutor().execute(() -> {
            float speed = repository.getSpeedNext(id, order);
            speed = addSomeToZero(speed);
            nextStageSpeedNext.postValue(speed);
        });
    }

    private void tidyUpOtherStages(){
        if(isNewStage()) {
            List<Stage> stagesToTidy = repository.getStagesFromOneProfileWithOrderGreaterOrEqualsThan(stage.getProfileId(), stage.getOrder());
            doTidy(1, stagesToTidy);
        } else {
            int order;
            int biggestOrder;
            int addToOrder;
            if(initialOrder > stage.getOrder()) {
                biggestOrder = initialOrder-1;
                order = stage.getOrder();
                addToOrder = 1;
            } else {
                biggestOrder = stage.getOrder();
                order = initialOrder+1;
                addToOrder = -1;
            }
            List<Stage> stagesToTidy = repository.getStagesFromOneProfileWithOrderBetween(stage.getProfileId(), order, biggestOrder);
            doTidy(addToOrder, stagesToTidy);
        }
    }

    private boolean isNewStage(){
        if(stage.getId() == 0)
            return true;
        return false;
    }

    private void doTidy(int addToOrder, List<Stage> stages){
        if (stages == null)
            return;
        for (Stage stage : stages) {
            int order = stage.getOrder();
            order += addToOrder;
            stage.setOrder(order);
        }
        repository.updateStages(stages);
    }
    private float addSomeToZero(float speed){
        if(speed == 0)
            return 0.01f;
        return speed;
    }
}