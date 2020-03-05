package com.example.tingximalaya.views

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import com.example.tingximalaya.R

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/3/4$ 16:46$
 */
open class ConfirmBoxDialog : Dialog {


    private var mOnDialogActionClickListener: OnDialogActionClickListener? = null
    private var mTextCanal: TextView? = null

    private var mBoxClan: CheckBox? = null
    private var mTextconfirm: TextView? = null


    constructor(context: Context?) : this(context, 0)
    constructor(context: Context?, attrs: Int) : this(context, true, null)
    constructor(
        context: Context?,
        cancelble: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(
        context,
        cancelble,
        cancelListener
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_check_box_confirm)
        initView()
        initEevent()
    }


    private fun initEevent() {
        mTextCanal?.setOnClickListener {
            if (mOnDialogActionClickListener != null) {
                mOnDialogActionClickListener?.onCancelClick()
                dismiss()
            }
        }
        mTextconfirm?.setOnClickListener {
            if (mOnDialogActionClickListener != null) {

                mOnDialogActionClickListener?.onGiveUpClick(mBoxClan?.isChecked!!)
                dismiss()

            }
        }


    }

    private fun initView() {
        mTextCanal = this.findViewById(R.id.dialog_check_box_can)
        mTextconfirm = this.findViewById(R.id.dialog_check_box_cfim)
        mBoxClan = this.findViewById(R.id.dialog_check_clean)
    }


    fun SetOnDialogActionClickListener(onDialogActionClickListener: OnDialogActionClickListener) {

        this.mOnDialogActionClickListener = onDialogActionClickListener

    }

    interface OnDialogActionClickListener {
        fun onCancelClick()

        fun onGiveUpClick(isCheck:Boolean)
    }

}
