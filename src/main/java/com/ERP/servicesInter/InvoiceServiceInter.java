package com.ERP.servicesInter;

import com.ERP.dtos.InvoiceDto;

import java.util.List;

public interface InvoiceServiceInter
{
    InvoiceDto addInvoice(InvoiceDto invoiceDto);
    InvoiceDto updateInvoice(InvoiceDto invoiceDto,long invoiceId);
    InvoiceDto deleteInvoice(long invoiceId);
    InvoiceDto findInvoice(long invoiceId);
    List<InvoiceDto> addAllInvoice(List<InvoiceDto> invoiceDtos);
    List<InvoiceDto> findAllInvoice();
}
