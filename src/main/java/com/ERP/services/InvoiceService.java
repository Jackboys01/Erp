package com.ERP.services;

import com.ERP.dtos.InvoiceDto;
import com.ERP.entities.Invoice;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.InvoiceRepository;
import com.ERP.repositories.InvoiceRepository;
import com.ERP.servicesInter.InvoiceServiceInter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class InvoiceService implements InvoiceServiceInter
{
    private InvoiceRepository invoiceRepository;
    private ObjectMapper objectMapper;

    public InvoiceService(InvoiceRepository invoiceRepository, ObjectMapper objectMapper)
    {
        this.invoiceRepository=invoiceRepository;
        this.objectMapper=objectMapper;
    }

    @Override
    public InvoiceDto addInvoice(InvoiceDto invoiceDto) {
        try {
            Invoice newInvoice = objectMapper.convertValue(invoiceDto, Invoice.class);
            invoiceRepository.save(newInvoice);
            return objectMapper.convertValue(newInvoice, InvoiceDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding invoice: " + e.getMessage());
        }
    }

    @Override
    public InvoiceDto updateInvoice(InvoiceDto invoiceDto, long invoiceId) {
        try {
            //get the invoice which we need to update
            Invoice invoice= invoiceRepository.findById(invoiceId).orElseThrow(()-> new IdNotFoundException("Invoice not found with id: "+invoiceId));

            // Update invoice fields if they are not null or empty in invoiceDto
            if (Objects.nonNull(invoiceDto.getAmount())) {
                invoice.setAmount(invoiceDto.getAmount());
            }
            if (Objects.nonNull(invoiceDto.getInvoiceDate()) ) {
                invoice.setInvoiceDate(invoiceDto.getInvoiceDate());
            }
            if (Objects.nonNull(invoiceDto.getPaymentStatus()) && !invoiceDto.getPaymentStatus().isEmpty()) {
                invoice.setPaymentStatus(invoiceDto.getPaymentStatus());
            }
            invoiceRepository.save(invoice);
            return objectMapper.convertValue(invoice, InvoiceDto.class);
        }
        catch (Exception e) {
            throw new IdNotFoundException("Error updating invoice: " + e.getMessage());
        }
    }

    @Override
    public InvoiceDto deleteInvoice(long invoiceId) {
        try {
            Invoice invoiceToDelete = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IdNotFoundException("Invoice not found with id: " + invoiceId));
            invoiceRepository.deleteById(invoiceId);
            return objectMapper.convertValue(invoiceToDelete, InvoiceDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting invoice: " + e.getMessage());
        }
    }

    @Override
    public InvoiceDto findInvoice(long invoiceId) {
        try {
            Invoice invoiceToSearch = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new IdNotFoundException("Invoice not found with id: " + invoiceId));
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.convertValue(invoiceToSearch, InvoiceDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding invoice: " + e.getMessage());
        }
    }

    @Override
    public List<InvoiceDto> addAllInvoice(List<InvoiceDto> invoiceDtos) {
        try {
            List<Invoice> invoiceList = Arrays.asList(objectMapper.convertValue(invoiceDtos, Invoice[].class));
            invoiceRepository.saveAll(invoiceList);
            return Arrays.asList(objectMapper.convertValue(invoiceDtos, InvoiceDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding all invoices: " + e.getMessage());
        }
    }

    @Override
    public List<InvoiceDto> findAllInvoice() {
        try {
            List<Invoice> invoiceList = invoiceRepository.findAll();
            return Arrays.asList(objectMapper.convertValue(invoiceList, InvoiceDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all invoices: " + e.getMessage());
        }
    }
}
