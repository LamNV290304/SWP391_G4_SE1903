    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package Models;

    import java.sql.Timestamp;
    import java.sql.Date;

    /**
     *
     * @author duckh
     */
    public class Promotion {

        private int promotionId;
        private String promotionName;
        private Date startDate;
        private Date endDate;
        private boolean status;
        private Timestamp createdDate;
        private String createdBy;
        private double discountRate;
        private int categoryId;
        private String categoryName;

        public Promotion() {
        }

        public Promotion(int promotionId, String promotionName, Date startDate, Date endDate, boolean status) {
            this.promotionId = promotionId;
            this.promotionName = promotionName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
        }


        public Promotion(int promotionId, String promotionName, Date startDate, Date endDate, boolean status, Timestamp createdDate, String createdBy) {
            this.promotionId = promotionId;
            this.promotionName = promotionName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
            this.createdDate = createdDate;
            this.createdBy = createdBy;
        }

        public Promotion(int promotionId, String promotionName, Date startDate, Date endDate, boolean status, Timestamp createdDate, String createdBy, double discountRate, int categoryId, String categoryName) {
            this.promotionId = promotionId;
            this.promotionName = promotionName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
            this.createdDate = createdDate;
            this.createdBy = createdBy;
            this.discountRate = discountRate;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public double getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(double discountRate) {
            this.discountRate = discountRate;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(int promotionId) {
            this.promotionId = promotionId;
        }

        public String getPromotionName() {
            return promotionName;
        }

        public void setPromotionName(String promotionName) {
            this.promotionName = promotionName;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public Timestamp getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Timestamp createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

    }
