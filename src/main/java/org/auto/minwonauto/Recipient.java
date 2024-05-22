package org.auto.minwonauto;

public class Recipient {

    private String recipientName;
    private String residentRegistrationNumber;
    private String defaultAddress;
    private String detailAddress;
    private String applyCategory;

    private Recipient(RecipientBuilder builder) {
        this.recipientName = builder.recipientName;
        this.residentRegistrationNumber = builder.residentRegistrationNumber;
        this.defaultAddress = builder.defaultAddress;
        this.detailAddress = builder.detailAddress;
        this.applyCategory = builder.applyCategory;
    }

    public String getRecipientName() { return recipientName; }
    public String getResidentRegistrationNumber() {
        return residentRegistrationNumber.replace("-","");
    }
    public String getDefaultAddress() {
        return defaultAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getApplyCategory() {
        return applyCategory;
    }

    public static class RecipientBuilder {
        private String recipientName;
        private String residentRegistrationNumber;
        private String defaultAddress;
        private String detailAddress;
        private String applyCategory;

        public RecipientBuilder recipientName(String recipientName) {
            this.recipientName = recipientName;
            return this;
        }

        public RecipientBuilder residentRegistrationNumber(String residentRegistrationNumber) {
            this.residentRegistrationNumber = residentRegistrationNumber;
            return this;
        }

        public RecipientBuilder defaultAddress(String defaultAddress) {
            this.defaultAddress = detailAddress;
            return this;
        }

        public RecipientBuilder detailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
            return this;
        }

        public RecipientBuilder applyCategory(String applyCategory) {
            this.applyCategory = applyCategory;
            return this;
        }

        public Recipient build() {
            return new Recipient(this);
        }
    }
}
