package com.yourpackage.name;

public class PerformanceModel {
    private String name;
    private String mobileNumber;
    private int ofd;
    private int delivered;
    private int ofp;
    private int ofpCompleted;
    private int totalOfdOfp;
    private int totalOfdOfpComplete;
    private String conversionRate;

    public PerformanceModel(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.ofd = 0;
        this.delivered = 0;
        this.ofp = 0;
        this.ofpCompleted = 0;
        this.totalOfdOfp = 0;
        this.totalOfdOfpComplete = 0;
        this.conversionRate = "0%";
    }

    public void addCounts(int ofd, int del, int ofp, int ofpComp) {
        this.ofd += ofd;
        this.delivered += del;
        this.ofp += ofp;
        this.ofpCompleted += ofpComp;
        
        this.totalOfdOfp = this.ofd + this.ofp;
        this.totalOfdOfpComplete = this.delivered + this.ofpCompleted;
        
        if (this.totalOfdOfp > 0) {
            double rate = ((double) this.totalOfdOfpComplete / this.totalOfdOfp) * 100.0;
            this.conversionRate = String.format(java.util.Locale.US, "%.1f%%", rate);
        } else {
            this.conversionRate = "0%";
        }
    }

    // Getters
    public String getName() { return name; }
    public String getMobileNumber() { return mobileNumber; }
    public int getOfd() { return ofd; }
    public int getDelivered() { return delivered; }
    public int getOfp() { return ofp; }
    public int getOfpCompleted() { return ofpCompleted; }
    public int getTotalOfdOfp() { return totalOfdOfp; }
    public int getTotalOfdOfpComplete() { return totalOfdOfpComplete; }
    public String getConversionRate() { return conversionRate; }
}
