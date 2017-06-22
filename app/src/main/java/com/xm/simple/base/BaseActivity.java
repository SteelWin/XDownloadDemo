package com.xm.simple.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.xm.xdownload.interfac.NetBase;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 功能:
 * 作者：小民
 * 创建时间：2017/5/26
 * ps:为什么继承：AutoLayoutActivity
 * 使用了 适配终结者，有兴趣的可以看下，如果不需要。基础其他也一样
 * 本例主要是回收资源功能
 * 因为使用了 DataBinding 如果你不熟悉，把释放资源的  复制到自己的 BaseActivity就好。继承NetBase
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AutoLayoutActivity implements IBase<T>,NetBase{
    public T mBinding;
    /** 垃圾回收队列 */
    private List<Disposable> mRequestQueueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutResID());
        initBinding(mBinding);
        initView(savedInstanceState);
    }


    /** 在自己的BaseActivity中 抄以下代码，并集成 NetBase */
    @Override
    protected void onDestroy() {
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
