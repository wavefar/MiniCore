package com.lq.cxy.shop.fragment;


import android.databinding.Observable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragmentSearchBinding;
import com.lq.cxy.shop.model.viewmodel.GoodViewModel;

import org.wavefar.lib.base.BaseFragment;
import org.wavefar.lib.utils.KeyBoardUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 商品列表搜索
 * @author Administrator
 */
public class SearchFragment extends BaseFragment<FragmentSearchBinding, GoodViewModel> implements View.OnClickListener{

    private GoodViewModel goodViewModel;
    private EditText keywordEt;
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_search;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public GoodViewModel initViewModel() {
        goodViewModel=  new GoodViewModel(getContext());
        return goodViewModel;
    }

    @Override
    public void initData() {
        //设置头部刷新样式
        binding.twinklingRefreshLayout.setHeaderView(new ProgressLayout(getContext()));
        View view = binding.getRoot();
        keywordEt = view.findViewById(R.id.keywordEt);
        showKeyBoard();
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        keywordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                goodViewModel.setKeyWords(keywordEt.getText().toString());
            }
        });

        keywordEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    goodViewModel.setKeyWords(keywordEt.getText().toString());
                }
                return false;
            }
        });
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.uc.isFinishRefreshing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.uc.isFinishLoadmore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                clearKeyBoard();
                getActivity().finish();
                break;
                default:break;
        }
    }

    /**
     *  显示键盘
     */
    private void showKeyBoard() {
        if (!getActivity().isFinishing()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    /**
     * 清除键盘
     */
    private void clearKeyBoard() {
        if (!getActivity().isFinishing()) {
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getWindow().getDecorView()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}