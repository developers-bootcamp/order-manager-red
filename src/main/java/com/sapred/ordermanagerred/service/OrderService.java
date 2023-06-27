package com.sapred.ordermanagerred.service;


import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Value("${pageSize}")
    private int pageSize;
    public List<Orders> getOrders( String companyId, String statusId,int pageNumber) {

        Sort.Order sortOrder = Sort.Order.desc("auditData.updateDate");
        Sort sort = Sort.by(sortOrder);
//sort
        Pageable pageable = PageRequest.of(pageNumber, pageSize/* pageSize parameter omitted */,sort);

        Page<Orders> pageOrders= orderRepository.findByCompanyId_Id(companyId,pageable);
        return pageOrders.getContent();
//        return orderRepository.findAll();
    }
    // שימי לב: זו סתם פונקציה שמכניסה נתונים בשביל הבדיקה
    public void fill() {
        AuditData d = new AuditData("1", new Date(),new Date());
        List<Orders> orders=new ArrayList<Orders>();
        for (int i = 200; i <500 ; i++) {
            orders.add(new Orders(Integer.toString(i),"employee","customer",i*2,new Company("11","333",88,d),new AuditData("1", new Date(), new Date(i*1000)))) ;
        }
        orderRepository.saveAll(orders);

    }
}

