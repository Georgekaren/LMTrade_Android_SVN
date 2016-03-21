package com.lianmeng.core.framework.engine;

import java.lang.ref.SoftReference;  
import java.net.URL;  
import java.util.HashMap;  
  
import android.graphics.drawable.Drawable;  
import android.os.Handler;  
import android.os.Message;  
  
public class SyncImageLoaderNoAdapter {  
    private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();  
  
    // 根据URL从网络上下载图片,生成Drawable对象  
    public Drawable loadImageFormUrl(String imageUrl) {  
        try {  
            return Drawable.createFromStream(new URL(imageUrl).openStream(),  
                    "src");  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        }  
    }  
  
    public Drawable loadDrawable(final String imageUrl,  
            final ImageCallback imageCallback) {  
        // 判断缓存中是否有imageUrl这个缓存存在  
        if (imageCache.containsKey(imageUrl)) {  
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);  
            if (softReference.get() != null) {  
                return softReference.get();  
            }  
        }  
  
        final Handler handler = new Handler() {  
  
            @Override  
            public void handleMessage(Message msg) {  
                imageCallback.imageLoaded((Drawable) msg.obj);  
            }  
  
        };  
  
        // 缓存中不存在的话,启动异步线程下载图片  
        new Thread() {  
  
            @Override  
            public void run() {  
                // 将图片放入缓存  
                Drawable drawable = loadImageFormUrl(imageUrl);  
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));  
                Message msg = handler.obtainMessage(0, drawable);  
                handler.sendMessage(msg);  
            }  
  
        }.start();  
  
        return null;  
    }  
  
    // 回调函数,图片加载完毕后调用该函数  
    public interface ImageCallback {  
        public void imageLoaded(Drawable imageDrawable);  
    }  
  
}  