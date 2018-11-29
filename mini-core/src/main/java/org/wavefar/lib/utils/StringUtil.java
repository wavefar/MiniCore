package org.wavefar.lib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * @author Summer
 */
public class StringUtil {

	/**
	 * 手机号码
	 */
	private static Pattern mobile_pattern = Pattern.compile("^1[2-9]\\d{9}$");
	/**
	 * 数字
	 */
	private static Pattern num_pattern = Pattern.compile("[0-9]+");
	/**
	 * 获取HTML标签
	 */
	private static Pattern html_patten = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
	/**
	 * 获取网页中的图片地址
	 */
	private static Pattern  img_patten = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)");

	/**
	 * 金额验证
	 */
	private static  Pattern money_pattern = Pattern.compile("^([-+])?\\d+(\\.\\d+)?$");
	/**
	 * 是否是英文字
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0;
	}

	/**
	 * 获取英文字长度
	 * @param s
	 * @return
	 */
	public static int length(String s) {
		if (s == null) {
			return 0;
		}
		char[] c = s.toCharArray();
		int len = 0;
		for (char aC : c) {
			len++;
			if (!isLetter(aC)) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 去掉换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String removeLineChar(String str) {
		if (str == null) {
            return "";
        }
		return str.replaceAll("\r\n", "").replaceAll("\n", "");
	}

	/**
	 * 以中文字长度计算，截取字符串
	 * 
	 * @param origin 源字符串
	 * @param len 截取长度
	 * @param c 链接的字符串
	 * @return
	 */
	public static String substring(String origin, int len, String c) {
		if (origin == null || "".equals(origin) || len < 1) {
            return "";
        }
		String temp = removeLineChar(origin);
		byte[] strByte = new byte[len];
		if (len > length(origin)) {
			return temp;
		}
		try {
			System.arraycopy(temp.getBytes("GBK"), 0, strByte, 0, len);
			int count = 0;
			for (int i = 0; i < len; i++) {
				int value = (int) strByte[i];
				if (value < 0) {
					count++;
				}
			}
			if (count % 2 != 0) {
				len = (len == 1) ? ++len : --len;
			}
			return new String(strByte, 0, len, "GBK") + c;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static int parseInt(String s, int defaultValue) {
		int res = defaultValue;
		try {
			res = Integer.parseInt(s, 10);
		} catch (Exception e) {
			res = defaultValue;
		}
		return res;
	}
  /**
   *将数据集合转化拼成字符串
   * @param collection 集合
   * @param delimiter 分隔符
   * @return 
   */
	public static String join(Collection<?> collection, String delimiter) {
      StringBuilder builder = new StringBuilder();
      Iterator<?> iterator = collection.iterator();
      while (iterator.hasNext()) {
         builder.append(iterator.next());
          if (iterator.hasNext()) {
              builder.append(delimiter);
          }
      }
      return builder.toString();
}
	/**
	 * 检查是否是正确的email
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		    String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*" ;
		    Pattern p   =   Pattern.compile(regex);
		    Matcher m   =   p.matcher(email);
		   return  m.matches();
	}
	/**
	 * 检查是否是数字
	 * @param phone
	 * @return
	 */
	public static boolean checkNum(String phone){
        return num_pattern.matcher(phone).matches();
	}

	 /**
	  *检查是否是电话号码
	  */
	 public static boolean isMobileNo(String paramString) {
	    return mobile_pattern.matcher(paramString).matches();
	  }
	/**
	 * 检查用户输入账号格式是否正确,只能是邮箱或电话号码
	 * @param account
	 * @return
	 */
	public static boolean checkAccount(String account){
		return (isMobileNo(account)||checkEmail(account));
	}

    /**
     * 金额验证
     * @param str
     * @return
     */
    public static boolean checkMoney(String str) {
          Matcher m = money_pattern.matcher(str);
          return m.matches();
    }
    
    /**
     * 获取Manifest中meta_data数据
     * @param context
     * @param metaKey
     * @return
     */
    public static Object getMetaValue(Context context, String metaKey) {
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai && null != ai.metaData) {
				return ai.metaData.get(metaKey);
			}
        } catch (NameNotFoundException e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
	 * 判断给定字符串是否空白串。
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input )
	{
		if ( input == null || "".equals( input ) ) {
			return true;
		}
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 将输入流转化为String
	 * @param is
	 * @return
	 */
	public static String readDataFromStream(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder builder = new StringBuilder();
		String line;
		try {
			while((line = br.readLine()) != null) {
				builder.append(line);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	/**
	 * 补零操作
	 * 如：01
	 */
	public static String zeroFill(Object obj) {
		if(obj instanceof String) {
			obj = Double.parseDouble(obj.toString());
		}
		DecimalFormat decimal = new DecimalFormat("00");
		return decimal.format(obj);
    }

	
	/**
	 * 金币格式化
	 * @param sign  金币符号 如 ￥$ 为null则不带单位
	 * @param money 金币数
	 * @param pattern  如为null,则默认为0.00格式；
	 * @return 如:0.01
	 */
	public static String formatMoney(String sign,double money,String pattern) {
		try {
			if (pattern == null) {
				pattern = "0.00";
			}
			DecimalFormat df = new DecimalFormat(pattern);
			if (sign == null) {
				sign = "";
			}
			return money > 0 ?
					String.format("%s%s",sign,df.format(money)) :
					String.format("%s%s",sign,df.format(0d - money));
		} catch (Exception e) {
			return "0.00";
		}
	}


	/**
	 * 带正负符号人民币金币格式化
	 * @param value
	 *            金币字
	 * @return 如:0.01
	 */
	public static String formatSignMoney(double value) {
		return formatMoney("￥", value,null);
	}

    /**
     * 格式化小数
     * @param value 待处理数
     * @param pattern 如为null,则默认为0.00格式；
     * @return
     */
    public static String formatDecimal(double value,String pattern) {
        if (pattern == null) {
            pattern = "0.00";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

	/**
	 * 正则HTML字符串中的img url 地址
	 * @param htmlStr HTML字符串内容
	 * @return
	 */
	public static ArrayList<String> getImageUrl(String htmlStr){
		ArrayList<String> listImgUrl = new ArrayList<>();
		Matcher matcher= img_patten.matcher(htmlStr);
		while(matcher.find())
		{
			listImgUrl.add(matcher.group(1));
		}      
		return listImgUrl;
	}
	
	/***
	 * 去除HTML标签内容
	 * @param source
	 * @return
	 */
	public static String getHtmlString(String source){
	    Matcher html = html_patten.matcher(source);
		// 过滤html标签
	    return html.replaceAll("");
	}
	
	/**
	 * url编码处理
	 * @param url 请求的路径
	 * @return
	 */
	public static String urlEncode(String url)
	{
		return Uri.encode(url, "@#&=*+-_.,:!?()/~'%");
	} 
	
    /**
     * 隐藏中间4位手机号码
     * @param str
     * @return
     */
    public static String hidePhone(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int len = 10;
        if (str.length() > len) {
            return str.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return str;
    }
}
