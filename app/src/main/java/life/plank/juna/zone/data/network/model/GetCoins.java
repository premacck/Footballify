package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class GetCoins {
    private int coinCount;
    private float earlierPrice;
    private float currentPrice;
}