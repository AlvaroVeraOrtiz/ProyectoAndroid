package com.example.proyectoandroid.ui.mastarde;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MasTardeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MasTardeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}