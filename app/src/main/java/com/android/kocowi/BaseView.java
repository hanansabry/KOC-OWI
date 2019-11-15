package com.android.kocowi;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
