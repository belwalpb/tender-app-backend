package com.tender.config;

import com.tender.constants.TenderConstants;
import com.tender.filter.AdminFilter;
import com.tender.filter.VendorFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilter(){
        FilterRegistrationBean<AdminFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AdminFilter());
        registrationBean.addUrlPatterns("/admin/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<VendorFilter> vendorFilter(){
        FilterRegistrationBean<VendorFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new VendorFilter());
        registrationBean.addUrlPatterns("/vendor/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
}
