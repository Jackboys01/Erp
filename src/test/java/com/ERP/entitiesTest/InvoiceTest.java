package com.ERP.entitiesTest;

import com.ERP.Reporting;
import com.ERP.entities.Invoice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;

@ExtendWith(MockitoExtension.class)
public class InvoiceTest
{
    private static final Logger logger = LoggerFactory.getLogger(InvoiceTest.class);
    Invoice invoice;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Invoice");

    String invoiceDateString = (String) dataMap.get("invoiceDate");
    Date invoiceDate = Date.valueOf(invoiceDateString);
    String paymentStatus = (String) dataMap.get("paymentStatus");
    Double amount;
    {
        Object amountObject = dataMap.get("amount");
        if (amountObject instanceof Integer) {
            amount = ((Integer) amountObject).doubleValue();
        } else if (amountObject instanceof Double) {
            amount = (Double) amountObject;
        } else {
            throw new ClassCastException("Unexpected type for amount");
        }
    }

    public InvoiceTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp()
    {
        Reporting.generateHtmlReport();
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invoice = new Invoice();
        invoice.setInvoiceId(1L);
        invoice.setAmount(amount);
        invoice.setPaymentStatus(paymentStatus);
        invoice.setInvoiceDate(invoiceDate);
    }
    @Test
    void testConstructor() {
        test=extent.createTest("Constructor Testing");
        logger.info("Testing constructor");
        Assertions.assertEquals(1L, invoice.getInvoiceId());
        Assertions.assertEquals(amount, invoice.getAmount());
        Assertions.assertEquals(paymentStatus, invoice.getPaymentStatus());
        Assertions.assertEquals(invoiceDate, invoice.getInvoiceDate());
    }

    @Test
    void testGetters() {
        test=extent.createTest("Getter Testing");
        logger.info("Testing getters");
        Assertions.assertEquals(1L, invoice.getInvoiceId());
        Assertions.assertEquals(amount, invoice.getAmount());
        Assertions.assertEquals(paymentStatus, invoice.getPaymentStatus());
        Assertions.assertEquals(invoiceDate, invoice.getInvoiceDate());
    }

    @Test
    void testSetters() {
        test=extent.createTest("Setter Testing");
        logger.info("Testing setters");
        invoice.setInvoiceId(2L);
        invoice.setAmount(10000);
        invoice.setPaymentStatus("done");
        invoice.setInvoiceDate(Date.valueOf("2023-06-01"));
        Assertions.assertEquals(2L, invoice.getInvoiceId());
        Assertions.assertEquals(10000, invoice.getAmount());
        Assertions.assertEquals("done", invoice.getPaymentStatus());
        Assertions.assertEquals(Date.valueOf("2023-06-01"), invoice.getInvoiceDate());
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
