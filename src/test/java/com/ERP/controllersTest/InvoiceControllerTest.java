package com.ERP.controllersTest;

import com.ERP.Reporting;
import com.ERP.config.TestSecurityConfig;
import com.ERP.controllers.ClientController;
import com.ERP.controllers.InvoiceController;
import com.ERP.controllers.InvoiceController;
import com.ERP.dtos.InvoiceDto;
import com.ERP.entities.Invoice;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.security.JwtHelper;
import com.ERP.services.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(InvoiceController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
//@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest
{
    private static final Logger logger = LoggerFactory.getLogger(InvoiceControllerTest.class);

    @MockBean
    InvoiceService invoiceService;

    @MockBean
    private JwtHelper jwtHelper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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

    public InvoiceControllerTest() throws IOException {
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
        // Setup mock JWT token
        when(jwtHelper.generateToken(Mockito.any())).thenReturn("mock-token");

        // Setup Security context
        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser
    public void testCreateInvoice() throws Exception {
        test=extent.createTest("Create Invoice Test");
        logger.info("Starting testCreateInvoice");
        try {
            InvoiceDto invoiceDto =objectMapper.convertValue(invoice, InvoiceDto.class);
            when(invoiceService.addInvoice(invoiceDto)).thenReturn(invoiceDto);
            mockMvc.perform(post("/invoice/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(invoice)))
                    .andExpect(status().isCreated());
            logger.info("testCreateInvoice passed");
        } catch (Throwable t) {
            logger.error("testCreateInvoice failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getInvoiceTest() throws Exception {
        test=extent.createTest("FindById Invoice Test");
        logger.info("Starting getInvoiceTest");
        try {
            InvoiceDto invoiceDto =objectMapper.convertValue(invoice, InvoiceDto.class);
            long invoiceId = 1L;
            invoiceDto.setInvoiceId(invoiceId);

            when(invoiceService.findInvoice(invoiceId)).thenReturn(invoiceDto);

            mockMvc.perform(get("/invoice/find/{invoiceId}", invoiceId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.invoiceId").value(1L));
            logger.info("getInvoiceTest passed");
        } catch (Throwable t) {
            logger.error("getInvoiceTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getAllInvoicesTest() throws Exception {
        test=extent.createTest("FindAll Invoices Test");
        logger.info("Starting getAllInvoicesTest");
        try {
            List<Invoice> invoices= Arrays.asList(invoice,invoice);
            List<InvoiceDto> invoiceDtos= Arrays.asList(objectMapper.convertValue(invoices,InvoiceDto[].class));
            invoiceService.addAllInvoice(invoiceDtos);
            when(invoiceService.findAllInvoice()).thenReturn(invoiceDtos);
            mockMvc.perform(get("/invoice/findAll")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            logger.info("getAllInvoicesTest passed");
        } catch (Throwable t) {
            logger.error("getAllInvoicesTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testUpdateInvoice() throws Exception {
        test=extent.createTest("Update Invoice Test");
        logger.info("Starting testUpdateInvoice");
        try {
            List<Invoice> invoices= Arrays.asList(invoice,invoice);
            List<InvoiceDto> invoiceDtos= (Arrays.asList(objectMapper.convertValue(invoices,InvoiceDto[].class)));
            invoiceService.addAllInvoice(invoiceDtos);

            InvoiceDto invoiceDto=invoiceDtos.get(0);
            invoiceDto.setInvoiceId(1L);

            when(invoiceService.updateInvoice(invoiceDto, invoiceDto.getInvoiceId())).thenReturn(invoiceDto);
            mockMvc.perform(put("/invoice/update/{invoiceId}", invoiceDto.getInvoiceId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invoiceDto)))
                    .andExpect(status().isOk());
            logger.info("testUpdateInvoice passed");
        } catch (Throwable t) {
            logger.error("testUpdateInvoice failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testDeleteInvoice() throws Exception {
        test=extent.createTest("Delete InvoiceById Test");
        logger.info("Starting testDeleteInvoice");
        try {
            InvoiceDto invoiceDto = objectMapper.convertValue(invoice, InvoiceDto.class);
            invoiceDto.setInvoiceId(1L);
            when(invoiceService.deleteInvoice(invoiceDto.getInvoiceId())).thenReturn(invoiceDto);
            mockMvc.perform(delete("/invoice/delete/{invoiceId}", invoiceDto.getInvoiceId()))
                    .andExpect(status().isOk());
            logger.info("testDeleteInvoice passed");
        } catch (Throwable t) {
            logger.error("testDeleteInvoice failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testAddAllInvoices() throws Exception {
        test=extent.createTest("AddAll Invoices Test");
        logger.info("Starting testAddAllInvoices");
        try {
            List<Invoice> invoicesToAdd = Arrays.asList(invoice,invoice);
            List<InvoiceDto> invoiceDtos= Arrays.asList(objectMapper.convertValue(invoicesToAdd,InvoiceDto[].class));
            when(invoiceService.addAllInvoice(invoiceDtos)).thenReturn(invoiceDtos);

            mockMvc.perform(post("/invoice/addAll")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invoicesToAdd)))
                    .andExpect(status().isOk());
            logger.info("testAddAllInvoices passed");
        } catch (Throwable t) {
            logger.error("testAddAllInvoices failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
