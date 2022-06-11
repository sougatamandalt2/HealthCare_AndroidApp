package com.example.healthcare;

public class modelOrder {
    String patientname,relation,price,issue,orderid,date,Status;

    public modelOrder() {
    }

    public modelOrder(String patientname, String relation, String price, String issue, String order_id, String date, String status) {
        this.patientname = patientname;
        this.relation = relation;
        this.price = price;
        this.issue = issue;
        this.orderid = orderid;
        this.date = date;
        this.Status = Status;


    }

    public String getPatientname() {
        return patientname;
    }

    public String getRelation() {
        return relation;
    }

    public String getPrice() {
        return price;
    }

    public String getIssue() {
        return issue;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return Status;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.Status = Status;
    }
}
