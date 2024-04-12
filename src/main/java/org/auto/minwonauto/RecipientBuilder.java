package org.auto.minwonauto;

public class RecipientBuilder {
    private String recipientName;
    private String residentRegistrationNumber;
    private String defaultAddress;
    private String detailAddress;
    private String applyCategory;

    public RecipientBuilder recipientName(String recipientName){
        this.recipientName = recipientName;
        return this;
    }

    public RecipientBuilder residentRegistrationNumber(String residentRegistrationNumber){
        this.residentRegistrationNumber = residentRegistrationNumber;
        return this;
    }

    public RecipientBuilder defaultAddress(String defaultAddress){
        this.defaultAddress = detailAddress;
        return this;
    }

    public RecipientBuilder detailAddress(String detailAddress){
        this.detailAddress = detailAddress;
        return this;
    }

    public RecipientBuilder applyCategory(String applyCategory){
        this.applyCategory = applyCategory;
        return this;
    }

    public Recipient build(){
        return new Recipient(recipientName,residentRegistrationNumber,defaultAddress,detailAddress,applyCategory);
    }
}
