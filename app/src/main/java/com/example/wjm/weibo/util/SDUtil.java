package com.example.wjm.weibo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.example.wjm.weibo.base.C;

import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Wjm on 2016/6/30.
 */
public class SDUtil {
    private static  String TAG=SDUtil.class.getSimpleName();

    private static double MB=1024;
    private static double FREE_SD_SPACE_NEEDED_TO_CACHE=10;
    private static double IMAGE_EXPIRE_TIME=10;

    public static Bitmap getImage(String fileName){
        String realFileName= C.dir.faces+"/"+fileName;
        File file=new File(realFileName);
        if(!file.exists())
            return null;

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(realFileName,options);
    }

    public static Bitmap getSample(String fileName){
        String realFileName=C.dir.faces+"/"+fileName;
        File file=new File(realFileName);
        if(!file.exists())
            return null;
        //得到原始大小
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap=BitmapFactory.decodeFile(realFileName,options);
        int zoom=(int)(options.outHeight/(float)50);
        if(zoom<0)
            zoom=1;
        //得到sample大小的Bitmap
        options.inSampleSize=zoom;
        options.inJustDecodeBounds=false;
        bitmap=BitmapFactory.decodeFile(realFileName,options);
        return bitmap;
    }

    public static void saveImage(Bitmap bitmap,String fileName){
        if(bitmap==null){
            Log.w(TAG,"trying to save null bitmap");
            return;
        }
        //判断SD卡上的空间
        if(FREE_SD_SPACE_NEEDED_TO_CACHE>getfreeSpace()){
            Log.w(TAG,"low free space on sdcard,can not cache");
            return;
        }
        //不存在，则创建目录
        File dir=new File(C.dir.faces);
        if(!dir.exists())
            dir.mkdirs();
        //保存图片
        try{
            String realFileName=C.dir.faces+"/"+fileName;
            File file=new File(realFileName);
            file.createNewFile();
            OutputStream outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            Log.i(TAG,"Image save to sdcard");
        }catch (FileNotFoundException e){
            Log.w(TAG,"FileNotFoundException");
        }catch (IOException e){
            Log.w(TAG,"IOException");
        }
    }

    protected static void updateTime(String fileName){
        File file=new File(fileName);
        long newModifiedTime= System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    //计算SD卡上的剩余空间
    public static int getfreeSpace(){
        StatFs stat=new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB=((double)stat.getAvailableBlocks()*(double)stat.getBlockSize())/MB;
        return (int)sdFreeMB;
    }

    //清除过期/废弃的文件
    public static void removeExpiredCache(String dirPath,String fileName){
        File file=new File(dirPath,fileName);
        if(System.currentTimeMillis()-file.lastModified()>IMAGE_EXPIRE_TIME){
            Log.i(TAG,"clear some expired cached files");
            file.delete();
        }
    }

    public static void removeCache(String dirPath){
        File dir=new File(dirPath);
        File[] files=dir.listFiles();
        if(files==null)
            return;
        if(FREE_SD_SPACE_NEEDED_TO_CACHE>getfreeSpace()){
            int removeFactor=(int)(0.4*files.length+1);
            Arrays.sort(files, new FileLastModiSort());
            Log.i(TAG,"clear some expired csched files");
            for(int i=0;i<removeFactor;i++){
                files[i].delete();
            }
        }
    }

    private static class FileLastModiSort implements Comparator<File>{
        public int compare(File arg0,File arg1){
            if(arg0.lastModified()>arg1.lastModified())
                return 1;
            else if(arg0.lastModified()==arg1.lastModified())
                return 0;
            else
                return -1;
        }
    }
}
