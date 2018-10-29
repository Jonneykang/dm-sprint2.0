package cn.dm.shopdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ShopDiscoveryEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopDiscoveryEurekaApplication.class, args);
	}
}
