package com.example.blockchain_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlockchainGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainGatewayApplication.class, args);
        System.out.println("****************************************************");
        System.out.println("Channel: supervision-channel");
        System.out.println("Chaincode: mycode");
        System.out.println("Blockchain Gateway Starts......");
        System.out.println("****************************************************");
    }

}
