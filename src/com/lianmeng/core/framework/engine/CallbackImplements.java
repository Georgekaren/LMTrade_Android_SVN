package com.lianmeng.core.framework.engine;

import android.graphics.drawable.Drawable;  
import android.widget.ImageView;  
  
public class CallbackImplements implements SyncImageLoaderNoAdapter.ImageCallback {  
  
    private ImageView imageView;  
  
    public CallbackImplements(ImageView imageView) {  
        super();  
        this.imageView = imageView;  
    }  
  
    @Override  
    public void imageLoaded(Drawable imageDrawable) {  
        imageView.setImageDrawable(imageDrawable);  
    }  
  
}  
