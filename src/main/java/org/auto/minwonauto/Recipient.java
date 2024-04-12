package org.auto.minwonauto;

public class Recipient {

    private String recipientName;
    private String residentRegistrationNumber;
    private String defaultAddress;
    private String detailAddress;
    private String applyCategory;

    public Recipient(String recipientName, String residentRegistrationNumber, String defaultAddress, String detailAddress, String applyCategory) {
        this.recipientName = recipientName;
        this.residentRegistrationNumber = residentRegistrationNumber;
        this.defaultAddress = defaultAddress;
        this.detailAddress = detailAddress;
        this.applyCategory = applyCategory;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getResidentRegistrationNumber() {
        return residentRegistrationNumber.replace("-","");
    }

    public void setResidentRegistrationNumber(String residentRegistrationNumber) {
        this.residentRegistrationNumber = residentRegistrationNumber;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getApplyCategory() {
        return applyCategory;
    }

    public void setApplyCategory(String applyCategory) {
        this.applyCategory = applyCategory;
    }
}
