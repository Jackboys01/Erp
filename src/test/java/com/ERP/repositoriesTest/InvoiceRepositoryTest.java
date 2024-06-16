package com.ERP.repositoriesTest;

import com.ERP.Reporting;
import com.ERP.entities.Invoice;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.InvoiceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;

@SpringBootTest
public class InvoiceRepositoryTest
{
    private static final Logger logger = LoggerFactory.getLogger(InvoiceRepositoryTest.class);
    @Autowired
    private InvoiceRepository invoiceRepository;

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

    public InvoiceRepositoryTest() throws IOException {
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
    public void saveInvoiceTest() {
        test=extent.createTest("Save Invoice Test");
        logger.info("Starting saveInvoiceTest");
        try {
            invoiceRepository.save(invoice);
            Assertions.assertThat(invoice.getInvoiceId()).isGreaterThan(0);
            Assertions.assertThat(invoice.getAmount()).isEqualTo(amount);
            logger.info("saveInvoiceTest passed");
        } catch (Throwable t) {
            logger.error("saveInvoiceTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void findInvoiceById() {
        test=extent.createTest("FindById Invoice Test");
        logger.info("Starting findInvoiceById");
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            Invoice invoiceToFind = invoices.get(0);
            Invoice invoice = invoiceRepository.findById(invoiceToFind.getInvoiceId()).get();
            Assertions.assertThat(invoice.getInvoiceId()).isEqualTo(invoiceToFind.getInvoiceId());
            logger.info("findInvoiceById passed");
        } catch (Throwable t) {
            logger.error("findInvoiceById failed", t);
            throw t;
        }
    }

    @Test
    public void findAllInvoices() {
        test = extent.createTest("FindAll Invoices Test");
        logger.info("Starting findAllInvoices");
        try {
            addAll(); // Add some invoices before finding all
            List<Invoice> invoices = invoiceRepository.findAll();
            Assertions.assertThat(invoices.size()).isGreaterThan(0);
            logger.info("findAllInvoices passed");
        } catch (Throwable t) {
            logger.error("findAllInvoices failed", t);
            throw t;
        }
    }

    @Test
    public void updateInvoice() {
        test=extent.createTest("Update Invoice Test");
        logger.info("Starting updateInvoice");
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            Invoice invoiceToFind = invoices.get(0);
            Invoice invoice = invoiceRepository.findById(invoiceToFind.getInvoiceId()).get();
            invoice.setPaymentStatus("done...");
            Invoice savedInvoice = invoiceRepository.save(invoice);
            Assertions.assertThat(savedInvoice.getPaymentStatus()).isEqualTo("done...");
            logger.info("updateInvoice passed");
        } catch (Throwable t) {
            logger.error("updateInvoice failed", t);
            throw t;
        }
    }

    @Test
    public void deleteInvoice() {
        test=extent.createTest("DeleteById Invoice Test");
        logger.info("Starting deleteInvoice");
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            Invoice invoiceToDelete = invoices.get(0);
            invoiceRepository.delete(invoiceToDelete);
            Invoice deletedInvoice = null;
            Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceToDelete.getInvoiceId());
            if (optionalInvoice.isPresent()) {
                deletedInvoice = optionalInvoice.get();
            }
            Assertions.assertThat(deletedInvoice).isNull();
            logger.info("deleteInvoice passed");
        } catch (Throwable t) {
            logger.error("deleteInvoice failed", t);
            throw t;
        }
    }

@Test
public void addAll() {
    test = extent.createTest("AddAll Invoices Test");
    logger.info("Starting addAll");
    try {
        List<Invoice> invoicesToAdd = new ArrayList<>();
        Invoice secondInvoice = new Invoice();
        secondInvoice.setAmount(amount);
        secondInvoice.setPaymentStatus(paymentStatus);
        secondInvoice.setInvoiceDate(invoiceDate);

        invoicesToAdd.add(invoice);
        invoicesToAdd.add(secondInvoice);

        List<Invoice> addedInvoices = invoiceRepository.saveAll(invoicesToAdd);
        Assertions.assertThat(addedInvoices).hasSize(2);
        Assertions.assertThat(addedInvoices.get(0).getAmount()).isEqualTo(amount);
        Assertions.assertThat(addedInvoices.get(1).getAmount()).isEqualTo(amount);
        logger.info("addAll passed");
    } catch (Throwable t) {
        logger.error("addAll failed", t);
        throw t;
    }
}

    @Test
    public void findByNameTest() {
        test=extent.createTest("FindByName Invoice Test");
        logger.info("Starting findByNameTest");
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            Invoice invoiceToFind = invoices.get(0);
            Invoice invoice = invoiceRepository.findById(invoiceToFind.getInvoiceId()).get();
            Assertions.assertThat(invoice.getAmount()).isEqualTo(invoiceToFind.getAmount());
            logger.info("findByNameTest passed");
        } catch (Throwable t) {
            logger.error("findByNameTest failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
