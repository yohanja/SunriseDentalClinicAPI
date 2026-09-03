package com.sunrise.dao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillDAOTest {

    @Test
    void calculateTotal_addsConsultationFeeToTreatmentCost() {
        BillDAO billDAO = new BillDAO();

        double total = billDAO.calculateTotal("Cleaning");

        assertEquals(4000.00, total, 0.01);
    }

    @Test
    void getPriceForTreatment_returnsCorrectPriceForKnownTreatment() {
        BillDAO billDAO = new BillDAO();

        assertEquals(3000.00, billDAO.getPriceForTreatment("Cleaning"), 0.01);
        assertEquals(5000.00, billDAO.getPriceForTreatment("Filling"), 0.01);
        assertEquals(4000.00, billDAO.getPriceForTreatment("Extraction"), 0.01);
    }

    @Test
    void getPriceForTreatment_returnsDefaultPriceForUnknownTreatment() {
        BillDAO billDAO = new BillDAO();

        assertEquals(2000.00, billDAO.getPriceForTreatment("Something Unusual"), 0.01);
    }
}