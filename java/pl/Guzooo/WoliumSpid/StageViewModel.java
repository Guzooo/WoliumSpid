package pl.Guzooo.WoliumSpid;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import pl.Guzooo.WoliumSpid.Database.Repository;
import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.Database.WSDatabase;

public class StageViewModel extends AndroidViewModel {

    private Repository repository;
    private Stage stage;
    private int initialOrder;
    private int maxOrder;

    public StageViewModel(Application application){
        super(application);
        repository = new Repository(application);
    }

    public void applyStageChange(){
        tidyUpOtherStages();
        if (stage.getId() == 0)
            repository.insertStage(stage);
        else
            repository.updateStage(stage);
    }

    public void deleteStage(){
        WSDatabase.getExecutor().execute(() -> {
            List<Stage> stagesToTidy = repository.getStagesFromOneProfileWithOrderGreaterOrEqualsThan(stage.getProfileId(), stage.getOrder());
            doTidy(-1, stagesToTidy);
            repository.deleteStage(stage);
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
        initialOrder = stage.getOrder();
    }

    public Stage getStage(){
        return stage;
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

    private void tidyUpOtherStages(){
        if(stage.getId() == 0) {
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

    private void doTidy(int addToOrder, List<Stage> stages){
        if (stages == null)
            return;
        for (Stage stage : stages) {
            int order = stage.getOrder();
            order += addToOrder;
            stage.setOrder(order);
            repository.updateStage(stage);
        }
    }
}