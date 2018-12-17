package com.jyq.wm.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jyq.wm.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;


public class APPUtils
{

    /**
     * 〈获得本地MAC地址〉1
     *
     * @return String
     * @throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @SuppressLint(
            {"NewApi", "DefaultLocale"})
    public static String getMacAddress()
    {
        String mac = "";
        try
        {
            byte[] m = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()))
                    .getHardwareAddress();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < m.length; i++)
            {
                if (i != 0)
                {
                    sb.append("-");
                }
                String s = Integer.toHexString(m[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            mac = sb.toString().toUpperCase();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return mac;
    }

    /**
     * 设备id。如果device获取为空，测从preference取出. 如果preference为空就随机生成一个，并保存到preference.
     */
    public static String getUUid(Context mContext)
    {
        //        TelephonyManager telManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //        String uuid = telManager.getDeviceId();
        //
        //        if (uuid != null && uuid.length() == 14)
        //        {
        //            uuid += "1";
        //        }
        //        else if (uuid == null)
        //        {
        //            uuid = ConfigManager.instance().getUUID();
        //        }
        //        if (uuid == null)
        //        {
        //            Random random = new Random();
        //            StringBuilder sb = new StringBuilder();
        //            for (int i = 0; i < 15; i++)
        //            {
        //                sb.append(random.nextInt(10));
        //            }
        //            uuid = sb.toString();
        //            ConfigManager.instance().setUUID(uuid);
        //        }
        return "qwertyu12345678";
    }


    /**
     * <获取本机IP>
     *
     * @return String
     */
    public static String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); )
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); )
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex)
        {
            LogUtil.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    /**
     * <获取当前应用的版本号>
     *
     * @param mContext
     * @return String
     */
    public static String getVersionName(Context mContext)
    {
        String version = "2.2.2";
        if (null != mContext)
        {
            try
            {
                PackageManager packageManager = mContext.getPackageManager();
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                version = packInfo.versionName;
            } catch (NameNotFoundException e)
            {
                e.printStackTrace();
                return version;
            }
        }

        LogUtil.e("TAG", "version--->" + version);
        return version;
    }

    /**
     * 〈获取手机设备号〉
     *
     * @return String
     * @throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     * @since [起始版本]
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context)
    {
        String id = "";

        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != mTelephony && mTelephony.getDeviceId() != null)
        {
            id = mTelephony.getDeviceId();
        }
        else
        {
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }


        //android.telephony.TelephonyManager
        return id;
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID()
    {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try
        {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception)
        {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String getUniqueId(Context context)
    {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try
        {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return id;
        }
    }


    private static String toMD5(String text) throws NoSuchAlgorithmException
    {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++)
        {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2)
            {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }

    /**
     * 获取设备IMEI
     *
     * @param mContext
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context mContext)
    {
        String mImei = "NULL";
        try
        {
            mImei = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e)
        {
            System.out.println("获取IMEI码失败");
            mImei = "000000000000000";
        }
        return mImei;
    }

    /**
     * 〈获取手机分辨率〉
     *
     * @param mContext
     * @return String
     * @throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     * @since [起始版本]
     */
    public static String getResolution(Activity mContext)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        return heightPixels + "*" + widthPixels;
    }

    /**
     * 获取屏幕分辨率宽
     */
    public static int getScreenWidth(Context context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕分辨率高
     */
    public static int getScreenHeight(Context context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕分辨率宽计算dialog的宽度
     */
    public static int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static boolean isAppInstalled(Context context, String packageName)
    {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null)
        {
            for (int i = 0; i < pinfo.size(); i++)
            {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /*获取当前系统的android版本号*/
    public static int getCurrentapiVersion()

    {
        return Build.VERSION.SDK_INT;

    }

    /**
     * 配置ImageLoder
     */
    public static void configImageLoader(Context mContext)
    {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_default) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_default) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_default) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中

                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .imageDownloader(new AuthImageDownloader(mContext))
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    /**
     * 配置ImageLoder
     */
    public static void configImageLoader(Context mContext, int stubImg, int emptyImg, int errorImg)
    {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(stubImg) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(emptyImg) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(errorImg) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    //获取权限
    public static void getPermission(Activity mContext)
    {
        String[] mPermissionList = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission
                .SYSTEM_ALERT_WINDOW, Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= 23)
        {
            List<String> denyPermissions = findDeniedPermissions(mContext, Arrays.asList(mPermissionList));
            String[] mPermissionList1 = new String[denyPermissions.size()];
            for (int i = 0; i < denyPermissions.size(); i++)
            {

                mPermissionList1[i] = denyPermissions.get(i);
            }
            if (denyPermissions.size() != 0)
            {

                ActivityCompat.requestPermissions(mContext, mPermissionList1, 100);

            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, List<String> permission)
    {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission)
        {
            if ((activity.checkSelfPermission(value)) != PackageManager.PERMISSION_GRANTED)
            {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }


    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className)
    {
        if (context == null || TextUtils.isEmpty(className))
        {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0)
        {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
            {
                return true;
            }
        }

        return false;
    }





    public static void logout(Context mContext)
    {
        //        AppDelegate.getInstance().setUser(null);
        //        //如果要清除密码，就启用下面这段代码
        //        SharedPreferenceUtil preference = SharedPreferenceUtil.getInstance();
        //        preference.setUrlSuffix(null);
        //        Intent useChanged = new Intent(UserChangeReceiver.ACTION_USER_CHANGED);
        //        mContext.sendBroadcast(useChanged);

        ConfigManager.instance().setUniqueCode(null);
        ConfigManager.instance().setUserId("");
        //                Intent useChanged = new Intent(UserChangeReceiver.ACTION_USER_CHANGED);
        //                       mContext.sendBroadcast(useChanged);

    }


    /**
     * 复制文件到SD卡
     *
     * @param context
     * @param apkName 复制的文件名
     * @param path    保存的目录路径
     * @return
     */
    public static boolean copyAssetsFile(Context context, String apkName, String path)
    {
        // TODO Auto-generated method stub

        try
        {
            InputStream mInputStream = context.getAssets().open(apkName);
            File file = new File(path);
            if (!file.exists())
            {
                file.mkdir();
            }
            File mFile = new File(path + apkName);
            if (!mFile.exists())
                mFile.createNewFile();
            FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
            byte[] mbyte = new byte[1024];
            int i = 0;
            while ((i = mInputStream.read(mbyte)) > 0)
            {
                mFileOutputStream.write(mbyte, 0, i);
            }
            mInputStream.close();
            mFileOutputStream.close();
            return true;
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("TAG", apkName + "not exists" + "or write err");
            return false;
        } catch (Exception e)
        {
            // TODO: handle exception
            return false;
        }
    }


    /**
     * 安装apk
     *
     * @param mContext
     * @param path     apk存放的路径
     */
    public static void installApk(final Context mContext, final String path, final String className)
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.ic_launcher)
                .setMessage("是否安装");
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.setDataAndType(Uri.parse("file://" + path),
                        "application/vnd.android.package-archive");
                mContext.startActivity(mIntent);
                try
                {
                    runApk(mContext, path, className);
                } catch (Exception e)
                {
                    // TODO: handle exception
                }
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        mBuilder.show();
    }


    /**
     * 运行安装好的APK
     *
     * @param mContext
     * @param packageName
     * @param className
     */
    public static void runApk(Context mContext, String packageName, String className)
    {
        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName mComponentName = new ComponentName(packageName, className);
        mIntent.setComponent(mComponentName);
        mIntent.putExtra("content", "第一个app传过来的数据");
        mContext.startActivity(mIntent);
    }


    /**
     * App是否已安装
     *
     * @param mContext
     * @param packageName 包名
     * @return
     */
    public static boolean isAppInstall(Context mContext, String packageName)
    {
        PackageInfo mInfo;
        try
        {
            mInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e)
        {
            // TODO: handle exception
            mInfo = null;
            Log.i("TAG", "没有发现安装的包名");
        }
        if (mInfo == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public static boolean fileIsExists(String filePath)
    {
        try
        {
            File f = new File(filePath);
            if (!f.exists())
            {
                return false;
            }

        } catch (Exception e)
        {
            // TODO: handle exception
            return false;
        }
        return true;
    }


    public static void startYgfp(Context mContext)
    {
        if (!APPUtils.isAppInstall(mContext, ConstantUtil.THIRD_PACKAGENAME))
        {
            if (APPUtils.copyAssetsFile(mContext, ConstantUtil.THIRD_APKNAME,
                    ConstantUtil.THIRD_PATH))
            {
                APPUtils.installApk(mContext, ConstantUtil.THIRD_PATH + ConstantUtil.THIRD_APKNAME, ConstantUtil.THIRD_CLASSNAME);
            }
        }
        else
        {
            APPUtils.runApk(mContext, ConstantUtil.THIRD_PACKAGENAME, ConstantUtil.THIRD_CLASSNAME);
        }
    }


    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public static void callPhone(Context cxt, String phone)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cxt.startActivity(intent);
    }
}
