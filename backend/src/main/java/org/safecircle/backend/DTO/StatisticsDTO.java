package org.safecircle.backend.dto;

public class StatisticsDTO {
    private String className;
    private int amount;

    public StatisticsDTO() {
    }

    public StatisticsDTO(String className, int amount) {
        this.className = className;
        this.amount = amount;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
