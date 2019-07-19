package cn.brank.chuyiting.chuyitinggateway;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb

public class ChuyitingGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChuyitingGatewayApplication.class, args);
    }

}
