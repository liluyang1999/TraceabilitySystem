package com.example.blockchain_gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FabricProperties {

    @Value("${fabric.connection-config-path}")
    private String connectionConfigPath;

    @Value("${fabric.channel-name}")
    private String channelName;
    @Value("${fabric.chaincode-name}")
    private String chaincodeName;

    @Value("${fabric.regulator-certificate-path}")
    private String regulatorCertificatePath;
    @Value("${fabric.regulator-private-key-path}")
    private String regulatorPrivateKeyPath;
    @Value("${fabric.regulator-wallet-dir}")
    private String regulatorWalletDir;

    @Value("${fabric.manufacturer-certificate-path}")
    private String manufacturerCertificatePath;
    @Value("${fabric.manufacturer-private-key-path}")
    private String manufacturerPrivateKeyPath;
    @Value("${fabric.manufacturer-wallet-dir}")
    private String manufacturerWalletDir;

    @Value("${fabric.distributor-certificate-path}")
    private String distributorCertificatePath;
    @Value("${fabric.distributor-private-key-path}")
    private String distributorPrivateKeyPath;
    @Value("${fabric.distributor-wallet-dir}")
    private String distributorWalletDir;

    @Value("${fabric.provider-certificate-path}")
    private String providerCertificatePath;
    @Value("${fabric.provider-private-key-path}")
    private String providerPrivateKeyPath;
    @Value("${fabric.provider-wallet-dir}")
    private String providerWalletDir;

    @Value("${fabric.consumer-certificate-path}")
    private String consumerCertificatePath;
    @Value("${fabric.consumer-private-key-path}")
    private String consumerPrivateKeyPath;
    @Value("${fabric.consumer-wallet-dir}")
    private String consumerWalletDir;
}
