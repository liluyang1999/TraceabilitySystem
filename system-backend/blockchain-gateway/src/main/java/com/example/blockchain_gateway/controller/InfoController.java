package com.example.blockchain_gateway.controller;

import com.example.blockchain_gateway.domain.AjaxResult;
import com.example.blockchain_gateway.domain.BlockRecord;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.Channel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/blockchain/info")
@RestController
public class InfoController {

    @Resource(name = "channel")
    private Channel channel;

    @RequestMapping("/getBasicInfo")
    public AjaxResult getBasicInfo() {
        try {
            BlockchainInfo blockchainInfo = channel.queryBlockchainInfo();
            Long height = blockchainInfo.getHeight();
            String channelId = channel.queryBlockByNumber(0).getChannelId();
            AjaxResult ajaxResult = AjaxResult.success("Get blockchain current height successfully");
            ajaxResult.put("height", height);
            ajaxResult.put("channelId", channelId);
            return ajaxResult;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getAllBlockHashes")
    public AjaxResult getAllBlockHashes() {
        List<String> hashes = new ArrayList<>();
        try {
            BlockchainInfo blockchainInfo = channel.queryBlockchainInfo();
            String curHash = Hex.encodeHexString(blockchainInfo.getCurrentBlockHash());
            hashes.add(curHash);

            long height = blockchainInfo.getHeight();
            for (long num = height - 1; num >= 1; num--) {
                BlockInfo blockInfo = channel.queryBlockByNumber(num);
                String preHash = Hex.encodeHexString(blockInfo.getPreviousHash());
                hashes.add(preHash);
            }
            return AjaxResult.success("Get blockchain hashes successfully", hashes);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getAllBlockRecords")
    public AjaxResult getAllBlockRecords() {
        List<BlockRecord> blockRecords = new ArrayList<>();
        try {
            BlockchainInfo blockchainInfo = channel.queryBlockchainInfo();

            long height = blockchainInfo.getHeight();
            for (long num = height - 1; num >= 0; num--) {
                BlockRecord blockRecord = new BlockRecord();
                BlockInfo blockInfo = channel.queryBlockByNumber(num);
                blockRecord.setBlockNumber(blockInfo.getBlockNumber());
                blockRecord.setTxCount(blockInfo.getTransactionCount());
                blockRecord.setDataHash(Hex.encodeHexString(blockInfo.getDataHash()));
                blockRecords.add(blockRecord);
            }
            return AjaxResult.success("Get all block records successfully", blockRecords);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
