package com.example.beata.testapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stone on 15/12/29.
 */
public class DeviceInfoUtil2 {
	
	private static final String TAG = "DeviceInfoUtil";

	private static final String DEFAULT_LOCALE = "IN";

	public static JSONObject getResolutionObj(Context context) throws JSONException {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("x", dm.widthPixels);
		jsonObject.put("y", dm.heightPixels);
		
		return jsonObject;
	}

	public static String getVendor() {
		return Build.BRAND != null ? Build.BRAND 
				: (Build.MANUFACTURER != null ? Build.MANUFACTURER : "unknow");
	}

	public static String getDeviceOrientation(Context context) {
		final String orientation_land = "landscape";
		final String orientation_port = "portrait";
		String screenOrientation = orientation_port;
		if (context != null) {
			int orientation = context.getResources().getConfiguration().orientation;
			screenOrientation = orientation == Configuration.ORIENTATION_LANDSCAPE ? orientation_land : orientation_port;
		}
		return screenOrientation;
	}


	public static String getImei(Context aContext) {
		TelephonyManager tm = (TelephonyManager) aContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * Get Android Id
	 * @return Android Id of current device
	 */
	public static String getAndroidId(Context ctx) {
		return  Settings.Secure.getString(ctx.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}

	/**
	 * 取得android sdk版本
	 * @return
	 */
	public static String getAndroidVersion() {
		return Build.VERSION.RELEASE;
	}

	public static String getPhoneDeviceModel() {
		return Build.MODEL;
	}

	public static String getResolution(Context aContext) {
		DisplayMetrics display = aContext.getResources().getDisplayMetrics();
		return display.widthPixels + "*" + display.heightPixels;
	}

	public static String getVersionName(Context aContext) {
		try {
			PackageManager packageManager = getPackageManager(aContext);
			PackageInfo packInfo = packageManager.getPackageInfo(
					aContext.getPackageName(),  0);
			String version = packInfo.versionName;
            Log.d("versionName","version = "+ Utils.encryptSHA1(version));
			return version;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getVersionCode(Context aContext) {
		try {
			PackageManager packageManager = aContext.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					aContext.getPackageName(), 0);
			int versioncode = packInfo.versionCode;
			return String.valueOf(versioncode);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * locationdata[0] : Latitude locationdata[1] : Longitude;
	 *
	 * @param aContext
	 * @return
	 */
	public static double[] getLocation(Context aContext) {
		LocationManager locationManager = (LocationManager) aContext
				.getSystemService(Context.LOCATION_SERVICE);
		double[] locationdata = new double[]{0.0,0.0};
        Location location = null;
        try{
            List<String> provides = locationManager.getProviders(true);
            for(String pro : provides){
                location = locationManager
                        .getLastKnownLocation(pro);
                if(null != location){
                    break;
                }
            }
            /*Location location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(null == location){
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }*/
            if (location != null) {
                locationdata[0] = location.getLatitude();
                locationdata[1] = location.getLongitude();
            }
        }catch (SecurityException e){
            Log.d("utils",e.getMessage());
        }

		return locationdata;
	}

    private static LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double[] locationdata = new double[]{0.0,0.0};
            locationdata[0] = location.getLatitude();
            locationdata[1] = location.getLongitude();
            Log.e("test","locationdata[0]: "+locationdata[0]+" locationdata[1]:"+locationdata[1]);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

	public static String getMacAddress(Context aContext) {
		String result = null;
		WifiManager wifiManager = (WifiManager) aContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}

	private static String getLocalIpAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAddress = wifiInfo.getIpAddress();

		//返回整型地址转换成“*.*.*.*”地址
		return String.format("%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
	}

	public static String getIpAddresses(Context aContext) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if(intf.isVirtual()){
                    Log.d("d","d");
                }
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getOperator(Context aContext) {
 		TelephonyManager tm = (TelephonyManager) aContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simoperator = tm.getSimOperator();

		String[] liantong = {
				"46001", "46006"
		};
		String[] yidong = {
				"46000", "46002", "46007"
		};
		String[] dianxin = {
				"46003", "46005"
		};

		if(!TextUtils.isEmpty(simoperator)){
			for (String data : yidong) {
				if (data.equals(simoperator)) {
					return 1;
				}
			}
			for (String data : liantong) {
				if (data.equals(simoperator)) {
					return 2;
				}
			}
			for (String data : dianxin) {
				if (data.equals(simoperator)) {
					return 3;
				}
			}
			return 4;
		}else {
			return -1;
		}
	}

    public static PackageManager getPackageManager(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            resetIPackageManager(context);
            pm = context.getPackageManager();
        }
        return pm;
    }


    public static void resetIPackageManager(Context context) {
        Context contextImpl;
        if (context instanceof ContextWrapper) {
            contextImpl = ((ContextWrapper) context).getBaseContext();
        } else {
            return;
        }

        try {
            Class<?> clazz1 = context.getClassLoader().loadClass("android.app.ActivityThread");
            Field field1 = clazz1.getDeclaredField("sPackageManager");
            field1.setAccessible(true);
            field1.set(null, null);

            Class<?> clazz2 = context.getClassLoader().loadClass("android.app.ContextImpl");
            Field field2 = clazz2.getDeclaredField("mPackageManager");
            field2.setAccessible(true);
            field2.set(contextImpl, null);

            Class<?> clazz3 = context.getClassLoader().loadClass("android.os.ServiceManager");
            Field field3 = clazz3.getDeclaredField("sServiceManager");
            field3.setAccessible(true);
            field3.set(null, null);

            Field field4 = clazz3.getDeclaredField("sCache");
            field4.setAccessible(true);
            HashMap<String, IBinder> map = (HashMap<String, IBinder>) field4.get(null);
            if (map != null) {
                IBinder oldValue = map.remove("package");
                if (oldValue != null) {
                    Method method1 = clazz3.getDeclaredMethod("getService", String.class);
                    method1.setAccessible(true);
                    IBinder newValue = (IBinder) method1.invoke(null, "package");
                    if (newValue != null) {
                        map.put("package", newValue);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

}
