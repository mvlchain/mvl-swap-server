package io.mvlchain.mvlswap.controller;

import com.binance.dex.api.client.BinanceDexApiClientFactory;
import com.binance.dex.api.client.BinanceDexApiNodeClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.domain.AtomicSwap;
import io.mvlchain.mvlswap.util.MVContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import java.io.IOException;

@RestController
public class erc20ToBep2 {

    private static String GETPROVIDER_DEV = MVContext.getAPINode("ETH");

    @GetMapping("/ebTest")
    public String ebTest(@RequestHeader(value = "Authorization") String strPrivateKey, Model model)
    {
        // *!*!*!*!*! DEV *!*!*!*!*!
        Web3j web3 = Web3j.build(new HttpService(	GETPROVIDER_DEV	));

        Credentials credentials = Credentials.create(strPrivateKey);

        try {
            System.out.println("Balance: "
                    + Convert.fromWei(web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .send().getBalance().toString(), Unit.ETHER));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return strPrivateKey;

    }


}
