package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class LastTransaction {
    private String date;
    private String type;
    private float debit;
    private float credit;
    private float balance;
}