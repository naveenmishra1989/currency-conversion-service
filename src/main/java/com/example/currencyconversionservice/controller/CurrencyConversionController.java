package com.example.currencyconversionservice.controller;

import com.example.currencyconversionservice.bean.CurrencyConversionBean;
import com.example.currencyconversionservice.proxy.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;


    /**
     * http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/23
     * @param from
     * @param to
     * @param quantity
     * @return CurrencyConversionBean
     */
    @GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){
      // final  CurrencyConversionBean bean = new CurrencyConversionBean();
        //Feign - rest client invoking external rest services
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from",from);
        uriVariables.put("to",to);
       ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8002/currency-exchange/from/{from}/to/{to}/",CurrencyConversionBean.class,uriVariables);
        CurrencyConversionBean bean = responseEntity.getBody();
        return  new CurrencyConversionBean(bean.getId(),from,to,bean.getConversionMultiple(),quantity,quantity.multiply(bean.getConversionMultiple()),bean.getPort());
    }

    /**
     * http://localhost:8100/currency-conversion-feign/from/USD/to/INR/quantity/23
     * @param from
     * @param to
     * @param quantity
     * @return
     */
    @GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){
     CurrencyConversionBean bean = currencyExchangeProxy.getExchangeValue(from, to);
        return  new CurrencyConversionBean(bean.getId(),from,to,bean.getConversionMultiple(),quantity,quantity.multiply(bean.getConversionMultiple()),bean.getPort());
    }
}
