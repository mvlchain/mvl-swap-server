package io.mvlchain.mvlswap.controller;

import com.binance.dex.api.client.BinanceDexApiClientFactory;
import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.domain.*;
import com.binance.dex.api.client.domain.broadcast.Transaction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ComponentScan
@RestController
public class bep2ToErc20 {

    @GetMapping("/bTest")
    public String bTest(@RequestHeader(value = "Authorization") String var1, Model model){

        BinanceDexApiNodeClient client1 = BinanceDexApiClientFactory.newInstance().newNodeRpcClient("http://data-seed-pre-0-s1.binance.org:80", BinanceDexEnvironment.TEST_NET.getHrp(), BinanceDexEnvironment.TEST_NET.getValHrp());
        AtomicSwap as = client1.getSwapByID(var1);

        return as.toString();

    }


}
