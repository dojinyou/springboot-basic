package org.prgrms.kdt.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
public class PercentDiscountVoucher implements Voucher{

    private final UUID voucherId;
    private final long percent;

    public PercentDiscountVoucher(UUID voucherId, long percent) {
        this.voucherId = voucherId;
        this.percent = percent;
    }

    @Override
    public String getType() {
        return "PERCENT";
    }

    @Override
    public long discountCoupon() {
        return percent;
    }

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    @Override
    public long discount(long beforeDiscount) {
        return beforeDiscount * (percent / 100);
    }

}