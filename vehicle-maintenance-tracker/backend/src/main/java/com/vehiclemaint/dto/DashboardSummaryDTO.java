package com.vehiclemaint.dto;

public class DashboardSummaryDTO {

    private long totalVehicles;
    private long totalOwners;
    private long pendingOrders;
    private long inProcessOrders;
    private long completedOrders;

    public long getTotalVehicles() {
        return totalVehicles;
    }

    public void setTotalVehicles(long totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public long getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(long totalOwners) {
        this.totalOwners = totalOwners;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public long getInProcessOrders() {
        return inProcessOrders;
    }

    public void setInProcessOrders(long inProcessOrders) {
        this.inProcessOrders = inProcessOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }
}
