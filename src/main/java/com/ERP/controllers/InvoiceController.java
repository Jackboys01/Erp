package com.ERP.controllers;

import com.ERP.dtos.InvoiceDto;
import com.ERP.services.InvoiceService;
import com.ERP.utils.MyResponseGenerator;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
@Validated
public class InvoiceController
{
    InvoiceService invoiceService;
    private final Validator validator;

    public InvoiceController(InvoiceService invoiceService, LocalValidatorFactoryBean validatorFactory)
    {
        this.invoiceService=invoiceService;
        this.validator = validatorFactory.getValidator();
    }
    @PostMapping("/add")
    public ResponseEntity<Object> addInvoice(@Valid @RequestBody InvoiceDto invoiceDto)
    {
        InvoiceDto newInvoice=invoiceService.addInvoice(invoiceDto);
        if(invoiceDto!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED,true,"Invoice is added",newInvoice);
        }
        else
        {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Something went wrong",newInvoice);
        }
    }

    @PutMapping("/update/{invoiceId}")
    public ResponseEntity<Object> updateInvoice( @Valid @RequestBody InvoiceDto invoiceDto,@PathVariable Long invoiceId)
    {
        InvoiceDto invoiceDto1= invoiceService.updateInvoice(invoiceDto,invoiceId);
        if(invoiceDto1!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Invoice is updated successfully", invoiceDto1);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Something went wrong and Invoice is not updated successfully",invoiceDto1);
        }
    }

    @GetMapping("/find/{invoiceId}")
    public ResponseEntity<Object> findInvoice(@PathVariable long invoiceId)
    {
        InvoiceDto invoiceTofind =invoiceService.findInvoice(invoiceId);
        if(invoiceTofind!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Invoice is found", invoiceTofind);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND,false,"Invoice is not founc",invoiceTofind);
        }
    }

    @PostMapping("/addAll")
    public List<InvoiceDto> addAll(@Valid @RequestBody List< InvoiceDto> invoiceDtos)
    {
        return invoiceService.addAllInvoice(invoiceDtos);
    }

    @DeleteMapping("/delete/{invoiceId}")
    public ResponseEntity<Object> deleteInvoice(@PathVariable long invoiceId)
    {
        InvoiceDto invoiceDto= invoiceService.deleteInvoice(invoiceId);
        if(invoiceDto!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Invoice is Deleted Successfully",invoiceDto);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Invoice is not Deleted Successfully",invoiceDto);
        }
    }
    @GetMapping("/findAll")
    public List<InvoiceDto> findAll()
    {
        return invoiceService.findAllInvoice();
    }
}
