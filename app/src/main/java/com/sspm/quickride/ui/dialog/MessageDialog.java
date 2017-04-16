package com.sspm.quickride.ui.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sspm.quickride.ui.interfaces.Callback;

/**
 * Created by Siddhesh on 23-01-2017.
 */
public class MessageDialog {

    Context context;
    String title = "", content = "";
    boolean isCancelable = false;
    MaterialDialog dialog = null;
    String positiveText = "Ok";
    String negativeText = "";

    public MessageDialog(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPositiveText(String text) {
        positiveText = text;
    }

    public void setNegativeText(String text) {
        negativeText = text;
    }

    public void setCancelable() {
        isCancelable = true;
    }

    public void show(final Callback callback) {

        if (negativeText.equalsIgnoreCase("")) {
            /**
             * shows only positive button
             */
            dialog = new MaterialDialog.Builder(context)
                    .title(this.title)
                    .content(this.content)
                    .positiveText(positiveText)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                            if(callback != null) callback.onSuccess();
                        }
                    })
                    .cancelable(isCancelable)
                    .show();
        } else {
            /**
             * shows both positive and negative button
             */
            dialog = new MaterialDialog.Builder(context)
                    .title(this.title)
                    .content(this.content)
                    .positiveText(positiveText)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                            if(callback != null) callback.onSuccess();
                        }
                    })
                    .negativeText(negativeText)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                            if(callback != null) callback.onFailure();
                        }
                    })
                    .cancelable(isCancelable)
                    .show();
        }
    }

}