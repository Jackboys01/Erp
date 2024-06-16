package com.ERP.servicesTest;

import com.ERP.Reporting;
import com.ERP.dtos.InvoiceDto;
import com.ERP.entities.Invoice;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.InvoiceRepository;
import com.ERP.services.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class InvoiceServiceTest
{
    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceTest.class);

    private InvoiceRepository invoiceRepository;
    private ObjectMapper objectMapper;
    private InvoiceService invoiceService;
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

    @BeforeAll
    public static void reportSetUp()
    {
        Reporting.generateHtmlReport();
    }
    @BeforeEach
    void setUp() {
        invoiceRepository = Mockito.mock(InvoiceRepository.class);
        objectMapper = new ObjectMapper(); // Initialize objectMapper
        invoiceService = new InvoiceService(invoiceRepository, objectMapper);
        MockitoAnnotations.openMocks(this);
        invoice = new Invoice();
        invoice.setInvoiceId(1L);
        invoice.setAmount(amount);
        invoice.setPaymentStatus(paymentStatus);
        invoice.setInvoiceDate(invoiceDate);
    }

    InvoiceServiceTest() throws IOException {
    }
    @Test
    public void createInvoiceTest() {
        test=extent.createTest("Create Invoice Test");
        logger.info("Starting createInvoiceTest");
        try {
            // Mocking save method of invoiceRepository
            given(invoiceRepository.save(invoice)).willReturn(invoice);

            // Convert invoice1 to InvoiceDto using objectMapper
            InvoiceDto invoiceDto = objectMapper.convertValue(invoice, InvoiceDto.class);

            // Call the method under test
            InvoiceDto savedInvoice = invoiceService.addInvoice(invoiceDto);

            // Assert that the saved invoice is not null
            Assertions.assertThat(savedInvoice).isNotNull();
            logger.info("createInvoiceTest passed");
        } catch (Throwable t) {
            logger.error("createInvoiceTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void createAllInvoicesTest()
    {
        test=extent.createTest("CreateAll Invoices Test");
        logger.info("Starting createAllInvoicesTest");
        try {
            List<Invoice> invoices= new ArrayList<>();
            invoices.add(invoice);
            invoices.add(invoice);
            given(invoiceRepository.saveAll(invoices)).willReturn(invoices);
            List<InvoiceDto> invoiceDtos= Arrays.asList(objectMapper.convertValue(invoices,InvoiceDto[].class));
            List<InvoiceDto> savedInvoices= invoiceService.addAllInvoice(invoiceDtos) ;
            Assertions.assertThat(savedInvoices.get(0).getInvoiceId()).isEqualTo(invoice.getInvoiceId());
            Assertions.assertThat(savedInvoices.get(1).getInvoiceId()).isEqualTo(invoice.getInvoiceId());
            logger.info("createAllInvoicesTest passed");
        } catch (Throwable t) {
            logger.error("createAllInvoicesTest failed", t);
            throw t;
        }
    }

    @Test
    public void updateInvoiceTest() {
        test=extent.createTest("Update Invoice Test");
        logger.info("Starting updateInvoiceTest");
        try {
            // Mock data
            long invoiceId = 1L;
            Invoice existingInvoice = invoice;
            InvoiceDto invoiceDto= objectMapper.convertValue(invoice,InvoiceDto.class);

            // Mock behavior
            given(invoiceRepository.findById(invoiceId)).willReturn(java.util.Optional.of(existingInvoice));
            when(invoiceRepository.save(any(Invoice.class))).thenReturn(existingInvoice);

            // Call the method under test
            InvoiceDto updatedInvoice = invoiceService.updateInvoice(invoiceDto, invoiceId);

            // Assertions
            Assertions.assertThat(updatedInvoice).isNotNull();
            logger.info("updateInvoiceTest passed");
        } catch (Throwable t) {
            logger.error("updateInvoiceTest failed", t);
            throw t;
        }
    }

    @Test
    public void deleteInvoiceTest() {
        test=extent.createTest("DeleteById Invoice Test");
        logger.info("Starting deleteInvoiceTest");
        try {
            // Mock data
            long invoiceId = 1L;
            Invoice existingInvoice = invoice;
            // Mock behavior
            given(invoiceRepository.findById(invoiceId)).willReturn(java.util.Optional.of(existingInvoice));

            // Call the method under test
            InvoiceDto deletedInvoice = invoiceService.deleteInvoice(invoiceId);

            // Assertions
            Assertions.assertThat(deletedInvoice).isNotNull();
            logger.info("deleteInvoiceTest passed");
        } catch (Throwable t) {
            logger.error("deleteInvoiceTest failed", t);
            throw t;
        }
    }

    @Test
    public void findInvoiceTest() {
        test=extent.createTest("FindById Invoice Test");
        logger.info("Starting findInvoiceTest");
        try {
            // Mock data
            long invoiceId = 1L;
            Invoice existingInvoice = invoice;

            // Mock behavior
            given(invoiceRepository.findById(invoiceId)).willReturn(java.util.Optional.of(existingInvoice));

            // Call the method under test
            InvoiceDto foundInvoice = invoiceService.findInvoice(invoiceId);

            // Assertions
            Assertions.assertThat(foundInvoice).isNotNull();
            logger.info("findInvoiceTest passed");
        } catch (Throwable t) {
            logger.error("findInvoiceTest failed", t);
            throw t;
        }
    }

    @Test
    public void findAllInvoiceTest() {
        test=extent.createTest("FindAll Invoices Test");
        logger.info("Starting findAllInvoiceTest");
        try {
            // Mock data
            List<Invoice> invoiceList = Arrays.asList(invoice, invoice); // Add necessary fields for invoices

            // Mock behavior
            given(invoiceRepository.findAll()).willReturn(invoiceList);

            // Call the method under test
            List<InvoiceDto> foundInvoices = invoiceService.findAllInvoice();

            // Assertions
            Assertions.assertThat(foundInvoices).isNotNull();
            Assertions.assertThat(foundInvoices.size()).isEqualTo(invoiceList.size());
            logger.info("findAllInvoiceTest passed");
        } catch (Throwable t) {
            logger.error("findAllInvoiceTest failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
