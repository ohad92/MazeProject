package ViewModel;

import Model.IModel;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public void Save(File file){
        this.model.Save(file);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == this.model)
        {
            setChanged();
            notifyObservers();
        }
    }
}
