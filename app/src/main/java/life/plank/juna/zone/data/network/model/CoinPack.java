package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class CoinPack {
    private int coinCount;
    private float earlierPrice;
    private float currentPrice;
}