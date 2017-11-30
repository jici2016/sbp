package transformmr;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="trade")
public class TradeProperty {
	@Override
	public String toString() {
		return "TradeProperty [host=" + host + ", name=" + name + ", port=" + port + ", hosturl=" + hosturl + "]";
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHosturl() {
		return hosturl;
	}

	public void setHosturl(String hosturl) {
		this.hosturl = hosturl;
	}

	/**
	 * 主机ip
	 */
	private String host;
	
	/**
	 * 程序名称
	 */
	private String name;
	
	/**
	 * 端口
	 */
	private String port;
	
	/**
	 * 地址
	 */
	private String hosturl;
	
}
