package com.xm.simple.base;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xm.xdownload.interfac.NetBase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 功能:
 * 作者：小民
 * 创建时间：2017/5/26
 * 本例主要是回收资源功能
 * 因为使用了 DataBinding 如果你不熟悉，把释放资源的  复制到自己的 BaseActivity就好。继承NetBase
 */
public abstract class BaseFragment<T extends ViewDataBinding>  extends Fragment implements IBase<T>,NetBase {
    public T mBinding;
    /** 垃圾回收队列 */
    private List<Disposable> mRequestQueueList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mBinding == null){
            mBinding = DataBindingUtil.inflate(inflater, getLayoutResID(), container, false);
            initBinding(mBinding);
            initView(savedInstanceState);
        }
        return mBinding.getRoot();
    }

    /** 在自己的BaseActivity中 抄以下代码，并集成 NetBase */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //网络请求回收
        netGc();
        //资源回收
        mBinding.unbind();
        mBinding = null;

    }

    /** 网络请求 */
    @Override
    public boolean addRequestQueue(Disposable disposable) {
        if(mRequestQueueList == null){
            return false;
        }
        mRequestQueueList.add(disposable);
        return true;
    }
    @Override
    public void removeRequestQueue(Disposable disposable) {
        mRequestQueueList.remove(disposable);
    }
    @Override
    public void netGc() {
        //取消队列
        for (int i = 0; i < mRequestQueueList.size(); i++) {
            Disposable disposable = mRequestQueueList.get(i);
            if(!disposable.isDisposed()){
                disposable.dispose();
            }
        }
        mRequestQueueList.clear();
        mRequestQueueList = null;
    }
}
