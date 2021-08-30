package io.mvlchain.mvlswap.util;

public class MVContext {
    public enum DeployPhase {
        develop, production;
    }

    public enum APIPhase {
        testnet, mainnet
    }

    public static DeployPhase phase = DeployPhase.develop;
    public static APIPhase apiPhase = APIPhase.testnet;

    private static String apiKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static String getAPIHost(String code) {
        switch (code) {
            case "ETH":
                return "https://fragrant-aged-wood.ropsten.quiknode.pro/";
        }
        return "";
    }

    public static String getAPINode(String code) {
        return getAPIHost(code) + apiKey;
    }

    public static String getAPIKey() {
        return apiKey;
    }

    public static String getExplorerAddress(String code) {
        switch (code) {
            case "ETH":
                return "https://etherscan.io/address/";
        }
        return "";
    }

    public static String getExplorerTx(String code) {
        switch (code) {
            case "ETH":
                return "https://etherscan.io/tx/";
        }
        return "";
    }

}
