package com.example.beata.testapp.activity.dynamicload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.beata.testapp.R;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by huangbiyun on 16-12-26.
 */
public class DynamicLoadActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicload);

        findViewById(R.id.btn_load_proxy).setOnClickListener(this);
    }



    private void testLoadJar(){
        //dex压缩文件的路径(可以是apk,jar,zip格式,先要通过Android SDK提供的DX工具把.jar文件优化成.dex文件)
        String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"0/" + "leoadlib.dex";
        //dex解压释放后的目录
        File dexOutputDirs = this.getDir("dex",0);
        //定义DexClassLoader
        //第一个参数：是dex压缩文件的路径
        //第二个参数：是dex解压缩后存放的目录
        //第三个参数：是C/C++依赖的本地库文件目录,可以为null
        //第四个参数：是上一级的类加载器
        DexClassLoader cl = new DexClassLoader(dexPath,dexOutputDirs.getAbsolutePath(),null,getClassLoader());

        try {
            Class libProviderClazz = cl.loadClass("com.leo.leoadlib.model.Campaign");
            Object o = libProviderClazz.newInstance();
            if(null != o){
                Method[] methods = libProviderClazz.getDeclaredMethods();
                for (int i = 0; i < methods.length; i++) {
                    Log.d("loadjar", "method :"+methods[i].toString());
                }
                Log.d("loadjar","load success : "+o.getClass());
            }
        } catch (ClassNotFoundException e) {
            Log.e("loadjar","load error ClassNotFoundException");
            e.printStackTrace();
        } catch (InstantiationException e) {
            Log.e("loadjar","load error InstantiationException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e("loadjar","load error IllegalAccessException");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_load_proxy:
                String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"0/" + "shareme.apk";
                Intent intent = new Intent(this, ProxyActivity.class);
                intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, dexPath);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
