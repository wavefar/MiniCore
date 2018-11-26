package com.lq.cxy.shop.model.viewmodel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.EditTextInputLayoutBinding;
import com.lq.cxy.shop.model.entity.MerchantAssesItemEntity;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.utils.KeyBoardUtil;

public class MerchantAssessReplyItemViewModel extends BaseViewModel {
    public MerchantAssesItemEntity itemEntity;

    public OnSaveListener getListener() {
        return listener;
    }

    public void setListener(OnSaveListener listener) {
        this.listener = listener;
    }

    private OnSaveListener listener;

    public MerchantAssessReplyItemViewModel(Context context, MerchantAssesItemEntity entity) {
        super(context);
        this.itemEntity = entity;
    }

    public void onClick() {
        final EditTextInputLayoutBinding binding = EditTextInputLayoutBinding.inflate(LayoutInflater.from(context));
        String replyTo = itemEntity.getName();
        if(TextUtils.isEmpty(replyTo)){
            replyTo = itemEntity.getLoginName();
        }
        final Dialog alertDialog = new AlertDialog.Builder(context).setTitle("回复:  " + replyTo)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = binding.editTextInput.getText().toString().trim();
                        if (!TextUtils.isEmpty(str) && listener != null) {
                            listener.saveReply(str, itemEntity);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                KeyBoardUtil.showSoftInput(binding.editTextInput);
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    public interface OnSaveListener {
        void saveReply(String msg, MerchantAssesItemEntity data);
    }
}
