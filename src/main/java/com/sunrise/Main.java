package com.sunrise;

import com.sunrise.dao.BillDAO;

public class Main {
    public static void main(String[] args) {
        BillDAO billDAO = new BillDAO();

        String result = billDAO.generateBill(1);
        System.out.println(result);
    }
}