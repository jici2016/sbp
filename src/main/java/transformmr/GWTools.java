package transformmr;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class GWTools {
	
	static Logger log = LoggerFactory.getLogger(GWTools.class);
	/**
	 * 用于返回运营商
	 * ct:chinatelecom中国电信,cu:chinaunicom中国联通,cm:chinamobile 中国移动
	 * 更新时间2015年6月2日
	 * @param mobile手机号码
	 * @return cm 代表移动,ct代表电信,cu代表联通，unknown代表新号段 ,error代表号码有误 
	 */
	public static String  getCarrierOperator(String mobile){
		String i = "unknown";
		String[] ct ={"133","153","177","180","181","189","1349","1700"},
		cm={
		"134","135","136","137","138","139","150","151","152","157","158","159","182","183","184","187","178","188","147","1705"		
		},
		cu={"130","131","132","145","155","156","176","185","186","1709"};  
        if(mobile==null || mobile.trim().length()!=11){  
            return "illegal mobile";        //mobile参数为空或者手机号码长度不为11，错误！  
        }else{
        	mobile = mobile.trim();
        	
        	for (int j = 0; j < ct.length; j++) {
				if(mobile.startsWith(ct[j])){
					return "ct";
				}
			}
        	for (int j = 0; j < cu.length; j++) {
        		if(mobile.startsWith(cu[j])){
        			return "cu";
				}
			}
        	for (int j = 0; j < cm.length; j++) {
        		if(mobile.startsWith(cm[j])){
        			return "cm";
				}
			}
        }  
        
        
        return i;
	}
	
	/**
	 * 用户获取请求的ip地址
	 * @param request 请求
	 * @return ip地址
	 */
	public static String getIpAddr1(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}
	
	public static String getIpAddr(HttpServletRequest request) {   
	     String ipAddress = null;   
	     //ipAddress = this.getRequest().getRemoteAddr();   
	     ipAddress = request.getHeader("x-forwarded-for");   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	      ipAddress = request.getHeader("Proxy-Client-IP");   
	     }   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	         ipAddress = request.getHeader("WL-Proxy-Client-IP");   
	     }   
	     if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	      ipAddress = request.getRemoteAddr();   
	      if(ipAddress.equals("127.0.0.1")){   
	       //根据网卡取本机配置的IP   
	       InetAddress inet=null;   
	    try {   
	     inet = InetAddress.getLocalHost();   
	    } catch (UnknownHostException e) {   
	     e.printStackTrace();   
	    }   
	    ipAddress= inet.getHostAddress();   
	      }   
	            
	     }   
	  
	     //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
	     if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15   
	         if(ipAddress.indexOf(",")>0){   
	             ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));   
	         }   
	     }   
	     return ipAddress;    
	  }   
	private static String getUUID(){
		String num=UUID.randomUUID().toString().replace("-", "");
		return num;
	}
	public static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(ip!=null && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(ip!=null && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
	 }
	
	public static void main(String[] args) {
//		System.out.println(getCarrierOperator("14435170041"));
//		getIpInfo("114.241.185.182");
//		GetNetIp138();
//		getIp138Addr();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("hhh", 111);
		map.put("sss", "qwe");
		map.put("qqq", "2222");
		map.put("zzz", "1221");
		System.out.println(Map2KV(map));
//		for (int i = 0; i < 100; i++) {
//			System.out.println(orderidCreater(10));
//		}
	}
	
	/**
	 * 随机订单号生成器 订单号格式：   cp编号 |订单号|cp预留参数
	 * @param cpid cp编号
	 * @param count 生成的订单长度
	 * @param ext cp预留参数
	 * @return
	 */
	public static String orderidCreater(String cpid,int count,String ext){
//		count = count-cpid.length()-ext.length();
		return cpid+"_"+orderidCreater(count)+"_"+ext;
	}
	
	/**
	 * 随机订单号生成器    订单号格式：   cp编号 _订单号 
	 * @param cpid cp编号
	 * @param count 生成的订单长度
	 * @return
	 */
	public static String orderidCreater(String cpid,int count){
//		 count = count-cpid.length();
		return cpid+"_"+orderidCreater(count);
	}
	
	/**
	 * 随机订单号生成器
	 * @param count 生成的订单个数
	 * @return
	 */
	public static String orderidCreater(int count){
		Random r = new Random();
		  int i = 1;
		  String str = "";
		  String s = null;
		  while (i <=count) {    //这个地方的30控制产生几位随机数，这里是产生count位随机数
		   switch (r.nextInt(62)) {
		    case (0): s = "0"; break;
		    case (1): s = "1"; break;
		    case (2): s = "2"; break;
		    case (3): s = "3"; break;
		    case (4): s = "4"; break;
		    case (5): s = "5"; break;
		    case (6): s = "6"; break;
		    case (7): s = "7"; break;
		    case (8): s = "8"; break;
		    case (9): s = "9"; break;
		    case (10): s = "a"; break;
		    case (11): s = "b"; break;
		    case (12): s = "c"; break;
		    case (13): s = "d"; break;
		    case (14): s = "e"; break;
		    case (15): s = "f"; break;
		    case (16): s = "g"; break;
		    case (17): s = "h"; break;
		    case (18): s = "i"; break;
		    case (19): s = "j"; break;
		    case (20): s = "k"; break;
		    case (21): s = "m"; break;//l
		    case (22): s= "Z";break;//m
		    case (23): s = "n"; break;
		    case (24): s = "o"; break;
		    case (25): s = "p"; break;
		    case (26): s = "q"; break;
		    case (27): s = "r"; break;
		    case (28): s = "s"; break;
		    case (29): s = "t"; break;
		    case (30): s = "u"; break;
		    case (31): s = "v"; break;
		    case (32): s = "w"; break;
		    case (33): s = "l"; break;//x
		    case (34): s = "x"; break;//y
		    case (35): s = "y"; break;//z
		    case (36): s = "z"; break;
		    case (37): s = "A"; break;
		    case (38): s = "B"; break;
		    case (39): s = "C"; break;
		    case (40): s = "D"; break;
		    case (41): s = "E"; break;
		    case (42): s = "F"; break;
		    case (43): s = "G"; break;
		    case (44): s = "H"; break;
		    case (45): s = "I"; break;
		    case (46): s = "L"; break;
		    case (47): s = "J"; break;
		    case (48): s = "K"; break;
		    case (49): s = "M"; break;
		    case (50): s = "N"; break;
		    case (51): s = "O"; break;
		    case (52): s = "P"; break;
		    case (53): s = "Q"; break;
		    case (54): s = "R"; break;
		    case (55): s = "S"; break;
		    case (56): s = "T"; break;
		    case (57): s = "U"; break;
		    case (58): s = "V"; break;
		    case (59): s = "W"; break;
		    case (60): s = "X"; break;
		    case (61): s = "Y"; break;
//		    case (62): s = "Z"; break;
		   }
//		   if(s!=null){
		   i++;
		   str = s + str;
//		   }
		  }
		
//		  System.out.println(str);
		  return str;
		 }
	/**
	 * 根据IMSI返回运营商信息
	 * 该方法返回 英文简称
	 * @param imsi
	 * @return  cm 代表移动,ct代表电信,cu代表联通，unknown代表新号段 
	 */
	public static String  getCarrierOperatorByImsi(String imsi) {
		String i = "unknown";
		if (imsi == null)
			return i;
		if (imsi.startsWith("46003") || imsi.startsWith("46011")
				|| imsi.startsWith("46005")) {
			return "ct";
		} else if (imsi.startsWith("46006") || imsi.startsWith("46001")) {
			return "cu";
		} else if (imsi.startsWith("46000") || imsi.startsWith("46002")
				|| imsi.startsWith("46007")) {
			return "cm";
		}
		return i;
	}
	
	/**
	 * 根据IMSI返回运营商信息
	 * 该方法返回 英文简称
	 * @param imsi
	 * @return  1 代表移动,3代表电信,2代表联通，0代表新号段 
	 */
	public static int  getOperatorByImsi(String imsi) {
		int i = 0;
		if (imsi == null)
			return i;
		if (imsi.startsWith("46003") || imsi.startsWith("46011")
				|| imsi.startsWith("46005")) {
			return 3;
		} else if (imsi.startsWith("46006") || imsi.startsWith("46001")) {
			return 2;
		} else if (imsi.startsWith("46000") || imsi.startsWith("46002")
				|| imsi.startsWith("46007")) {
			return 1;
		}
		return i;
	}
	
	/**
	 * 根据IMSI返回运营商信息
	 * 该方法返回中文
	 * @param imsi
	 * @return "中国电信" "中国联通" "中国移动" "未知"
	 */
	public static String  getCarrierOperatorByImsi2(String imsi) {
		String i = "unknown";
		if (imsi == null)
			return i;
		if (imsi.startsWith("46003") || imsi.startsWith("46011")
				|| imsi.startsWith("46005")) {
			return "中国电信";
		} else if (imsi.startsWith("46006") || imsi.startsWith("46001")) {
			return "中国联通";
		} else if (imsi.startsWith("46000") || imsi.startsWith("46002")
				|| imsi.startsWith("46007")) {
			return "中国移动";
		}
		return i;
	}
	/**
	 * 获取请求的参数
	 * @param req
	 * @return
	 */
	public static String getQueryStr(HttpServletRequest req){
		try {
			req.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Map<String, String[]> params = req.getParameterMap();  
        String queryString = "";  
        for (String key : params.keySet()) {  
            String[] values = params.get(key);  
            for (int i = 0; i < values.length; i++) {  
                String value = values[i];  
                queryString += key + "=" + value + "&";  
            }  
        }  
        // 去掉最后一个空格  
        if(queryString.length()>0)
        queryString = queryString.substring(0, queryString.length()-1);  
        return queryString;
	}
	
	/**
	 * 获取ip138的那个页面
	 * 因为页面经常的变动，所以需要访问首页，然后从里面摘除来类似于的地址http://1212.ip138.com/ic.asp
	 * 
	 * @return
	 */
	public static String getIp138Addr(){
		String url = "http://ip138.com";
		String addr = null;
		String pageContent=sendGet(url, "", "utf-8", 1000);
		if(pageContent.contains("ic.asp")){
			int pos = pageContent.indexOf("ic.asp");
			if(pos!=-1){
				addr = pageContent.substring(pos-22, pos+6);
			System.out.println(addr);
			}
		}
		return addr;
	}
	
	/**
	 * 任何手机只要访问这个网站就会的到 该ip的省份信息
	 * 根据ip获取省份信息
	 */
	public static String GetNetIp138() {
		
		URL infoUrl = null;
		String ipLine = "";
		HttpURLConnection httpConnection = null;
		try {
			String addr = getIp138Addr();
			if(addr==null)
			addr = "http://1212.ip138.com/ic.asp";
			
			infoUrl = new URL(addr);
			URLConnection connection = infoUrl.openConnection();
			httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				InputStream is = httpConnection.getInputStream();
				while ((len = is.read(buf)) != -1) {
					// System.out.println(len);
					baos.write(buf, 0, len);
				}
				baos.flush();
				// 这个utf8给予了太多的希望
				String strResult = baos.toString("gb2312");
				// System.out.println(strResult);
				baos.close();
				is.close();
				//
				// inStream = httpConnection.getInputStream();
				// BufferedReader reader = new BufferedReader(
				// new InputStreamReader(inStream, "GB2312"));
				// StringBuilder strber = new StringBuilder();
				// String line = null;
				// while ((line = reader.readLine()) != null)
				// strber.append(line + "\n");

				System.out.println("Result:" + strResult);
				String srchStr = "来自：";
				if (strResult != null && strResult.contains(srchStr)) {
					String prov = strResult.substring(strResult
							.indexOf(srchStr) + srchStr.length());
					prov = prov.substring(0, prov.indexOf("</center>"));
					System.out.println(prov);
					if (prov != null) {
						String[] provs = prov.split(" ");
						if (provs != null)
							prov = provs[0];
						if (prov != null) {
							System.out.println(prov);
						}
						if (provs != null)
							prov = provs[1];
						if (prov != null) {
							System.out.println(provs[1]);
						}
					}
				}
				Pattern pattern = Pattern
						.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
				Matcher matcher = pattern.matcher(strResult);
				if (matcher.find()) {
					ipLine = matcher.group();
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
			// try {
			// inStream.close();
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		System.out.println("Net ip=" + ipLine);
//		if (ipLine != null && !"0.0.0.0".equals(ipLine))
//			AppGlobal.getInstance().ip = ipLine;
		return ipLine;
	}
	
	/**
	 * 获取ip对应的省份和城市
	 * @param ip
	 * @return String[]数组  String[0]=河北 String[1]=石家庄
	 */
	public static String[] getIpInfo(String ip){
		String [] loc = new String[2];
		String urlstr  ="http://www.ip138.com/ips138.asp";
		String result =  sendGet(urlstr, "ip="+ip,"gb2312");
		String searchstr = "本站主数据：";
		result = result.substring(result.indexOf(searchstr)+searchstr.length(),result.indexOf("参考数据一"));
		
//		result =  "河南省郑州市 河南新飞金信计算机有限公司 联通</li><li>";
		result = result.substring(0,result.indexOf("</li>"));
		System.out.println(result);
		int provindex=result.indexOf("省");
		int cityindex=result.indexOf("市");
		String prov = null,city=null ;
		if(provindex!=-1)
		prov = result.substring(provindex-2,provindex);
		if(cityindex!=-1)
		city= result.substring(cityindex-2,cityindex);
		
		loc[0]= prov;
		loc[1]=city;
		System.out.println("prov="+prov+",city="+city);
		return loc;
	}
	/*
	* 向指定URL发送GET方法的请求
	 * @param url  发送请求的URL
	 * @param param  请求参数，请求参数应该是name1=value1&name2=value2的形式
	 * @return URL所代表远程资源的响应
	 */
	public static String sendGet(String url, String param,String code) {
		String result = "";
		BufferedReader in = null;
		long start = System.currentTimeMillis();
		String num = getUUID();
		try {
			
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			
			
			log.info("GWTools-GET-Begin[{}]，connect【{}?{}】",num,url,param);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(1000);
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
//                               Map<String, List<String>> map = conn.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),code));
			String line;
			while ((line = in.readLine()) != null) {
				result +=  line;
			}
		} catch (Exception e) {
			System.out.println("SendGetRequestError！" + e);
			e.printStackTrace();
//			result = e.getMessage();
			long end = System.currentTimeMillis();
			long time = end-start;
			result="{\"resultCode\":"+504+",\"resultMsg\":\"Timeout,Cost"+time+"ms！\"}";

		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		long time = (end-start);
		log.info("ReqNum-End[{}]Cost[{}ms]Fetch【{}】",num,time,result);
		return result;
	}
	/**
	 * 向指定URL发送POST方法的请求
	 * @param url 发送请求的URL
	 * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式
	 * @return URL所代表远程资源的响应
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		String num = getUUID();
		log.info("GWTools-POST-Begin[{}]，connect【{}?{}】",num,url,param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestMethod("POST");
			conn.setReadTimeout(10000);
			conn.setDoOutput(true);// 发送POST请求必须设置如下两行
			conn.setDoInput(true);
			
			out = new PrintWriter(conn.getOutputStream());// 获取URLConnection对象对应的输出流s
			out.print(param);// 发送请求参数
			out.flush();// flush输出流的缓冲
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));// 定义BufferedReader输入流来读取URL的响应
			String line;
			while ((line = in.readLine()) != null) {
				result +=line;
			}
		} catch (Exception e) {
			System.out.println("SendPOSTRequestError！" + e);
			e.printStackTrace();
//			result  = e.getLocalizedMessage();
//			Constants.
			long end = System.currentTimeMillis();
			long time = end-start;
			result="{\"resultCode\":"+504+",\"resultMsg\":\"Timeout,Cost"+time+"ms！\"}";
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		float time = (end-start);
		log.info("ReqNum-End[{}]Cost[{}ms]Fetch【{}】",num,time,result);
		return result;
	}
	
	public static String sendPostJson(String url ,String jsonstr,Map<String,String> headers,int timeout){
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		String num = getUUID();
		log.info("GWTools-POST-Begin[{}]，connect【{}?{}】",num,url,jsonstr);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestMethod("POST");
			
			for(Entry<String, String> entry: headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());  
			}
			conn.setReadTimeout(timeout);
			conn.setDoOutput(true);// 发送POST请求必须设置如下两行
			conn.setDoInput(true);
			
			out = new PrintWriter(conn.getOutputStream());// 获取URLConnection对象对应的输出流s
			out.print(jsonstr);// 发送请求参数
			out.flush();// flush输出流的缓冲
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));// 定义BufferedReader输入流来读取URL的响应
			String line;
			while ((line = in.readLine()) != null) {
				result +=line;
			}
		} catch (Exception e) {
			System.out.println("SendPOSTRequestError！" + e);
			e.printStackTrace();
//			result  = e.getLocalizedMessage();
//			Constants.
			long end = System.currentTimeMillis();
			long time = end-start;
			result="{\"resultCode\":"+504+",\"resultMsg\":\"Timeout,Cost"+time+"ms！\"}";
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		float time = (end-start);
		log.info("ReqNum-End[{}]Cost[{}ms]Fetch【{}】",num,time,result);
		return result;
	}
	public static String sendPostJson(String url, String jsonstr,String charset,int timeout) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		String num = getUUID();
		log.info("GWTools-POST-Begin[{}]，connect【{}?{}】",num,url,jsonstr);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Charset",charset);
			conn.setRequestMethod("POST");
			conn.setReadTimeout(timeout);
			conn.setDoOutput(true);// 发送POST请求必须设置如下两行
			conn.setDoInput(true);
			
			out = new PrintWriter(conn.getOutputStream());// 获取URLConnection对象对应的输出流s
			out.print(jsonstr);// 发送请求参数
			out.flush();// flush输出流的缓冲
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));// 定义BufferedReader输入流来读取URL的响应
			String line;
			while ((line = in.readLine()) != null) {
				result +=line;
			}
		} catch (Exception e) {
			System.out.println("SendPOSTRequestError！" + e);
			e.printStackTrace();
//			result  = e.getLocalizedMessage();
//			Constants.
			long end = System.currentTimeMillis();
			long time = end-start;
			result="{\"resultCode\":"+504+",\"resultMsg\":\"Timeout,Cost"+time+"ms！\"}";
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		float time = (end-start);
		log.info("ReqNum-End[{}]Cost[{}ms]Fetch【{}】",num,time,result);
		return result;
	}
	public static String sendPostJson(String url, String jsonstr,int timeout) {
		
		return sendPostJson(url, jsonstr,"UTF-8", timeout);
	}
	
	/*
	* 向指定URL发送GET方法的请求
	 * @param url  发送请求的URL
	 * @param param  请求参数，请求参数应该是name1=value1&name2=value2的形式
	 * @return URL所代表远程资源的响应
	 */
	public static String sendGet(String url, String param,String code,int timeout) {
		String result = "";
		BufferedReader in = null;
		long start = System.currentTimeMillis();
		String num = getUUID();
		try {
			
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			
			
			log.info("GWTools-GET-Begin[{}]，connect【{}?{}】",num,url,param);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setConnectTimeout(timeout);
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
//                               Map<String, List<String>> map = conn.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),code));
			String line;
			while ((line = in.readLine()) != null) {
				result +=  line;
			}
		} catch (Exception e) {
			System.out.println("SendGetRequestError！" + e);
			e.printStackTrace();
//			result = e.getMessage();
			long end = System.currentTimeMillis();
			long time = end-start;
			result="{\"resultCode\":"+504+",\"resultMsg\":\"Timeout,Cost"+time+"ms！\"}";
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		long time = (end-start);
		log.info("ReqNum-End[{}]Cost[{}ms]Fetch【{}】",num,time,result);
		return result;
	}
	/**
	 * 向指定URL发送POST方法的请求
	 * @param url 发送请求的URL
	 * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式
	 * @return URL所代表远程资源的响应
	 */
	public static String sendPost(String url, String param,int timeout) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		long start = System.currentTimeMillis();
		String num = getUUID();
		log.info("GWTools-POST-Begin[{}]，connect【{}?{}】",num,url,param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestMethod("POST");
			conn.setReadTimeout(timeout);
			conn.setConnectTimeout(timeout);
			conn.setDoOutput(true);// 发送POST请求必须设置如下两行
			conn.setDoInput(true);
			
			out = new PrintWriter(conn.getOutputStream());// 获取URLConnection对象对应的输出流s
			out.print(param);// 发送请求参数
			out.flush();// flush输出流的缓冲
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));// 定义BufferedReader输入流来读取URL的响应
			String line;
			while ((line = in.readLine()) != null) {
				result +=line;
			}
		} catch (Exception e) {
			System.out.println("SendPOSTRequestError！" + e);
			e.printStackTrace();
//			result  = e.getLocalizedMessage();
//			Constants.
			long end = System.currentTimeMillis();
			float time = (end-start)/1000;
			result="{\"resultCode\":"+504+",\"resultMsg\":\"Timeout,Cost"+time+"s！\"}";
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		float time = (end-start);
		log.info("ReqNum-End[{}]Cost[{}ms]Fetch【{}】",num,time,result);
		return result;
	}
	
	/**
	 * MD5 加密
	 * 
	 * @param str
	 * @return
	 */
	public final static String MD5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String Map2KV(Map<String,Object> map){
		String param = null;
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			;
		}
		sb.deleteCharAt(sb.lastIndexOf("&"));
		param = sb.toString().trim();
		return param;
	}
	
	public static String getStrfromStream(InputStream is){
		
		return getStrfromStream(is,"utf-8");
	
		
	}
	public static String getStrfromStream(InputStream is,String charset){
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		String line = null;
		String result = null;
		try {
			br = new BufferedReader(new InputStreamReader(is,charset));
			while ((line=br.readLine())!=null) {
				sb.append(line);
			}
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	
		
	}
}
