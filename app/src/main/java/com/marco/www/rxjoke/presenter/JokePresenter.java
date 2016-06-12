package com.marco.www.rxjoke.presenter;

import android.util.Log;

import com.marco.www.rxjoke.api.JokeApi;
import com.marco.www.rxjoke.api.RxService;
import com.marco.www.rxjoke.model.ContentlistEntity;
import com.marco.www.rxjoke.model.JokeEntity;
import com.marco.www.rxjoke.ui.view.JokeView;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by JDD on 2016/4/21 0021.
 */
public class JokePresenter extends BasePresenter<JokeView> {

    @Override
    public void attachView(JokeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void loadList(final int page) {
        RxService.createApi(JokeApi.class).getJoke(page)
                .subscribeOn(Schedulers.io())//在非UI线程中获取数据
                .map(new Func1<JokeEntity, List<ContentlistEntity>>() {
                    @Override
                    public List<ContentlistEntity> call(JokeEntity jokeEntity) {

                        for (ContentlistEntity c:jokeEntity.getShowapi_res_body().getContentlist()) {
                            Log.d("TAG",c.getTitle()+","+c.getText());
                        }

                        return jokeEntity.getShowapi_res_body().getContentlist();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//在UI线程中执行更新UI
                .subscribe(new Observer<List<ContentlistEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        getMvpView().showError(null, null);
                    }

                    @Override
                    public void onNext(List<ContentlistEntity> contentlistEntities) {
                        if (page == 1) {
                            getMvpView().refresh(contentlistEntities);
                        } else {
                            getMvpView().loadMore(contentlistEntities);
                        }
                    }
                });

    }
}
