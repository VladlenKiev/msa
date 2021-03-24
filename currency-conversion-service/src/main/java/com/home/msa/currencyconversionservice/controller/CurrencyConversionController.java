package com.home.msa.currencyconversionservice.controller;

import com.home.msa.currencyconversionservice.model.CurrencyConversion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {
    private final CurrencyExchangeProxy currencyExchangeProxy;

    public CurrencyConversionController(CurrencyExchangeProxy currencyExchangeProxy) {
        this.currencyExchangeProxy = currencyExchangeProxy;
    }

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable String quantity
                                                          ) {

        CurrencyConversion currencyConversionProxy = currencyExchangeProxy.retrieveExchangeValue(from, to);
        System.out.println("currencyConversionProxy = " + currencyConversionProxy);
        CurrencyConversion currencyConversion = new CurrencyConversion(currencyConversionProxy.getId(),
                currencyConversionProxy.getFrom(),
                currencyConversionProxy.getTo(),
                currencyConversionProxy.getConversionMultiply(),
                new BigDecimal(quantity),
                currencyConversionProxy.getConversionMultiply().multiply(new BigDecimal(quantity)),
                currencyConversionProxy.getEnvironment());
        return currencyConversion;
    }
}
