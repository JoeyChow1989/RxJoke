package com.marco.www.rxjoke.ui.view;

import com.marco.www.rxjoke.model.ContentlistEntity;

import java.util.List;


/**
 * Created by JDD on 2016/4/21 0021.
 */
public interface JokeView extends MvpView {
    void refresh(List<ContentlistEntity> data);

    void loadMore(List<ContentlistEntity> data);

}
