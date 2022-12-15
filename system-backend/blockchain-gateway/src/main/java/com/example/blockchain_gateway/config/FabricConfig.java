package com.example.blockchain_gateway.config;

import lombok.Data;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@ComponentScan(basePackages = {"com.example"})
@Configuration
@Data
public class FabricConfig {

    private static final Logger logger = LoggerFactory.getLogger(FabricConfig.class);

    @Autowired
    private FabricProperties fabricProperties;

    @Bean(name = "channel")
    public Channel getChannel() {
        return getRegulatorGateway().getNetwork(fabricProperties.getChannelName()).getChannel();
    }

    @Bean(name = "regulatorContract")
    public Contract getRegulatorContract() {
        return getRegulatorGateway().getNetwork(fabricProperties.getChannelName()).getContract(fabricProperties.getChaincodeName());
    }

    @Bean(name = "manufacturerContract")
    public Contract getManufacturerContract() {
        return getManufacturerGateway().getNetwork(fabricProperties.getChannelName()).getContract(fabricProperties.getChaincodeName());
    }

    @Bean(name = "distributorContract")
    public Contract getDistributorContract() {
        return getDistributorGateway().getNetwork(fabricProperties.getChannelName()).getContract(fabricProperties.getChaincodeName());
    }

    @Bean(name = "providerContract")
    public Contract getProviderContract() {
        return getProviderGateway().getNetwork(fabricProperties.getChannelName()).getContract(fabricProperties.getChaincodeName());
    }

    @Bean(name = "consumerContract")
    public Contract getConsumerContract() {
        return getConsumerGateway().getNetwork(fabricProperties.getChannelName()).getContract(fabricProperties.getChaincodeName());
    }

    @Bean(name = "regulatorGateway")
    public Gateway getRegulatorGateway() {
        return initGateway("regulator-supervision-com", "regulator",
                fabricProperties.getRegulatorCertificatePath(),
                fabricProperties.getRegulatorPrivateKeyPath(),
                fabricProperties.getRegulatorWalletDir());
    }

    @Bean(name = "providerGateway")
    public Gateway getProviderGateway() {
        return initGateway("provider-supervision-com", "provider",
                fabricProperties.getProviderCertificatePath(),
                fabricProperties.getProviderPrivateKeyPath(),
                fabricProperties.getProviderWalletDir());
    }

    @Bean(name = "manufacturerGateway")
    public Gateway getManufacturerGateway() {
        return initGateway("manufacturer-supervision-com", "manufacturer",
                fabricProperties.getManufacturerCertificatePath(),
                fabricProperties.getManufacturerPrivateKeyPath(),
                fabricProperties.getManufacturerWalletDir());
    }

    @Bean(name = "distributorGateway")
    public Gateway getDistributorGateway() {
        return initGateway("distributor-supervision-com", "distributor",
                fabricProperties.getDistributorCertificatePath(),
                fabricProperties.getDistributorPrivateKeyPath(),
                fabricProperties.getDistributorWalletDir());
    }

    @Bean(name = "consumerGateway")
    public Gateway getConsumerGateway() {
        return initGateway("consumer-supervision-com", "consumer",
                fabricProperties.getConsumerCertificatePath(),
                fabricProperties.getConsumerPrivateKeyPath(),
                fabricProperties.getConsumerWalletDir());
    }

    private Gateway initGateway(String mspId, String label, String certPath, String keyPath, String walletDir) {
        BufferedReader keyReader;
        BufferedReader certReader;
        try {
            /* set private key & certificate */
            /* set wallet for holding identity information */
            // client access organization: regulator.supervision.com
            certReader = Files.newBufferedReader(Paths.get(certPath), StandardCharsets.UTF_8);
            X509Certificate certificate = Identities.readX509Certificate(certReader);
            keyReader = Files.newBufferedReader(Paths.get(keyPath), StandardCharsets.UTF_8);
            PrivateKey privateKey = Identities.readPrivateKey(keyReader);

            Wallet wallet = Wallets.newFileSystemWallet(Paths.get(walletDir));
            Identity identity = Identities.newX509Identity(mspId, certificate, privateKey);
            wallet.put(label, identity);
            Gateway gateway = Gateway.createBuilder()
                    .networkConfig(Paths.get(fabricProperties.getConnectionConfigPath()))
                    .identity(wallet, label)
                    .connect();
            logger.info("Build connection to fabric network (MspId: " + mspId + ")......");
            return gateway;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error occurred when getting the fabric gateway......");
            return null;
        }
    }
}
