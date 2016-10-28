package com.example.beata.testapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.json.JSONArray;

import java.util.List;
import java.util.Locale;

/**
 * Created by stone on 15/12/29.
 */
public class DeviceInfoUtil {
	
	private static final String TAG = "DeviceInfoUtil";

	public static String android_id;
	public static String packageName;
	public static String advertistingId;
	
	/**
	 * 取得连接wifi的ssid
	 * @param ctx
	 * @return
	 */
	public static String getWifiSSID(Context ctx) {
		if (ctx != null) {
			WifiManager wifiMgr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
			if (wifiMgr != null && wifiMgr.getConnectionInfo() != null) {
				return wifiMgr.getConnectionInfo().getSSID().replace("\"", "");
			}
		}
		return "";
	}
	
	/**
	 * 获取当前的网络类型
	 * 包含WIFI | 2G | 3G | 4G | 其他
	 * @param ctx
	 * @return
	 */
	public static String getNetworkStatus(Context ctx) {
		String network = "";
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager == null){
			return network;
		}
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				network = "wifi";
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				int mobileType = info.getSubtype();

				switch (mobileType) {
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_IDEN:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
						network = "2G";
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						network = "3G";
						break;
					case TelephonyManager.NETWORK_TYPE_LTE:
						network = "4G";
						break;
					default:
						if (info.getSubtypeName().equalsIgnoreCase("TD-SCDMA")
								|| info.getSubtypeName().equalsIgnoreCase("WCDMA")
								|| info.getSubtypeName().equalsIgnoreCase("CDMA2000")) {
							network = "3G";
						} else {
							network = info.getSubtypeName();
						}

				}
			}
			return network;//JsonUtil.getJsonFromMap(map);
		}
		return network;//new JSONObject();
	}
	
	public static String getDeviceCpuInfo() {
		StringBuffer cup = new StringBuffer();
		return cup.append(Build.CPU_ABI2).append("|").append(Build.CPU_ABI).toString();
	}

    /***
     * Get application version name
     * @param ctx
     * @return version name of current application
     */
    public static String getAppVersionName(Context ctx) {
        String mAppVer = "1.0";
        /*try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            mAppVer = pi.versionName != null ? pi.versionName : mAppVer ;
        } catch (Exception e) {
        }*/
        return mAppVer;
    }

    /***
     * Get application package name
     * @param ctx
     * @return package name of current application
     */
    public static String getPackageName(Context ctx) {
        return packageName = ctx.getPackageName();
    }

    /**
     * Get Android Id
     * @return Android Id of current device
     */
    public static String getAndroidId(Context ctx) {
        return android_id = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

	/**
	 * 取得platform类型
	 * @return
	 */
    public static String getOsName() {
		return "a";
//        return "android";
    }

	/**
	 * 取得android sdk版本
	 * @return
	 */
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

	/**
	 * 取得手机model型号
	 * @return
	 */
    public static String getDeviceModel() {
        return Build.MODEL.replace("\"", "");
    }

	/**
	 * 取得手机环境语言
	 * @return
	 */
    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

	/**
	 * 取得国家码
	 * @param context
	 * @return
	 */
    public static String getCountry(Context context) {
        String id = null;
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            id = tm.getSimCountryIso();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(id)) {
            id = "unknown";
        } else {
            id = id.toLowerCase();
        }
        return id;
    }

	/**
	 * 取得手机屏密度
	 * @param context
	 * @return
	 */
	public static String getDensity(Context context) {
		if (context != null) {
			WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics metrics = new DisplayMetrics();
			windowMgr.getDefaultDisplay().getMetrics(metrics);
			return String.valueOf(metrics.densityDpi);
		}
		return "";
	}

	/**
	 * 取得分辨率
	 * @param context
	 * @return
	 */
    public static String getResolution(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        String res = dm.heightPixels + "x" + dm.widthPixels;
        return res;
    }

	/**
	 * 取得运营商信息
	 * @param context
	 * @return
	 */
	public static String getSimOper(Context context) {
		try {
			if (context != null) {
				TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				if (telephonyMgr != null) {
					return telephonyMgr.getSimOperator();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 取得地点信息
	 * @param context
	 * @return
	 */
    public static String getLocation(Context context) {
        LocationManager locationManager;
        String locationStr;
        try {
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationStr = location.getLongitude() + "/" + location.getLatitude();

            return locationStr;
        } catch (Exception e) {
        }
        return "";
    }


	/**
	 * 取得用户应用列表
	 * @param ctx
	 * @return
	 */
	public static JSONArray getAppList(Context ctx) {
		JSONArray jsonArray = new JSONArray();
		
		if (ctx != null) {
			PackageManager packageMgr = ctx.getPackageManager();
			List<PackageInfo> packageInfos =  packageMgr.getInstalledPackages(0);
			if (packageInfos != null) {
				for (PackageInfo info : packageInfos) {
					if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
						jsonArray.put(info.applicationInfo.packageName);
					}
				}
			}
		}
		
		return jsonArray;
	}
}
