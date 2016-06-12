package com.marco.www.rxjoke.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marco.www.rxjoke.R;
import com.marco.www.rxjoke.common.AutoLoadRecylerView;
import com.marco.www.rxjoke.common.DividerItemDecoration;
import com.marco.www.rxjoke.model.ContentlistEntity;
import com.marco.www.rxjoke.presenter.JokePresenter;
import com.marco.www.rxjoke.ui.BaseActivity;
import com.marco.www.rxjoke.ui.adapter.JokeAdapter;
import com.marco.www.rxjoke.ui.view.JokeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JDD on 2016/4/8.
 */
public class MainActivity extends BaseActivity implements JokeView,
        SwipeRefreshLayout.OnRefreshListener, AutoLoadRecylerView.loadMoreListener
{

    @Bind(R.id.record_recycleview)
    AutoLoadRecylerView recordRecycleview;
    @Bind(R.id.common_error_txt)
    TextView commonErrorTxt;
    @Bind(R.id.retry_btn)
    Button retryBtn;
    @Bind(R.id.common_error)
    RelativeLayout commonError;
    @Bind(R.id.joke_refresh_layout)
    SwipeRefreshLayout jokeRefreshLayout;
    private JokePresenter jokePresenter;
    private LinearLayoutManager layoutManager;
    private int page = 1;
    private List<ContentlistEntity> jokeList;
    private JokeAdapter jokeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
        loadData();
    }

    private void initView()
    {
        jokeRefreshLayout.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(this);
        recordRecycleview.setLayoutManager(layoutManager);
        recordRecycleview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recordRecycleview.setLoadMoreListener(this);
    }

    private void initData()
    {
        jokeList = new ArrayList<>();
        jokeAdapter = new JokeAdapter(jokeList);
        recordRecycleview.setAdapter(jokeAdapter);

        jokePresenter = new JokePresenter();
        jokePresenter.attachView(this);
    }

    private void loadData()
    {
        jokePresenter.loadList(page);
        page++;
    }

    @Override
    public void onRefresh()
    {
        page = 1;
        loadData();
    }


    @Override
    public void onLoadMore()
    {
        loadData();
    }

    /**
     * @param data 回调 以下三个方法是presenter回调的函数 用于更新界面
     */
    @Override
    public void refresh(List<ContentlistEntity> data)
    {
        commonError.setVisibility(View.GONE);
        jokeList.clear();
        jokeList.addAll(data);
        jokeAdapter.notifyDataSetChanged();
        jokeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(List<ContentlistEntity> data)
    {
        commonError.setVisibility(View.GONE);
        jokeList.addAll(data);
        jokeAdapter.notifyDataSetChanged();
        recordRecycleview.setLoading(false);
    }

    @Override
    public void showError(String msg, View.OnClickListener onClickListener)
    {
        super.showError(msg, onClickListener);
        commonError.setVisibility(View.VISIBLE);
        recordRecycleview.setLoading(false);
        jokeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.retry_btn)
    void setRetryBtnonClick()
    {
        onRefresh();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        jokePresenter.detachView();
    }
}
