package W3D2.jcu.voucher;

public enum VoucherStatus {
    // 미완

    FIXED("FIXED"),
    PERCENT("PERCENT");

    private String voucherType;

    VoucherStatus(String voucherType){
        this.voucherType = voucherType;
    }
}