package net.xdclass.demo.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//服务器配置

@Component
@PropertySource({"classpath:application.properties"})
//@ConfigurationProperties
@ConfigurationProperties

public class ServerSettings {

	//名称
	
	//@Value("${appname}")
	private String name;
	
	//@Value("${domain}")
	private String domain;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	
	
}
