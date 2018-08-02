package com.lihao.words.widgets;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.lihao.words.R;

public class EditTextWithDel extends android.support.v7.widget.AppCompatEditText {
	private Drawable imgInable;
	private Context mContext;

	public EditTextWithDel(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public EditTextWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init() {
		setFocusableInTouchMode(true);
		imgInable = mContext.getResources().getDrawable(R.drawable.ic_delete);
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,18,getResources().getDisplayMetrics());
		imgInable.setBounds(0,0,size,size);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();

			}
		});
	}
	
	private void setDrawable() {
		Drawable[] drawables = getCompoundDrawables();
		if(length() > 0){
			setCompoundDrawables(drawables[0], drawables[1], imgInable, drawables[3]);
		}else{
			setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
		}

	}
	
	 // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgInable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - imgInable.getIntrinsicWidth()- getPaddingRight();
            if(rect.contains(eventX, eventY)) {
            	setText(null);
                if(textDeleteListener !=null){
                	textDeleteListener.deleteText();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    
    private TextDeleteListener textDeleteListener;
    
    public  interface TextDeleteListener {
    	public void deleteText();
    }
    
    
    public void setTextDeleteListener(TextDeleteListener textDeleteListener) {
    	this.textDeleteListener =textDeleteListener;
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
