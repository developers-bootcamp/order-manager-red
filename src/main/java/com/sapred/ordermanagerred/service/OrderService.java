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
    public List<Orders> getOrders( String companyId, String statusId,int pageNumber,String userId) {

        Sort.Order sortOrder = Sort.Order.asc("auditData.updateDate");
        Sort sort = Sort.by(sortOrder);
//sort
        Pageable pageable = PageRequest.of(pageNumber, pageSize/* pageSize parameter omitted */,sort);

        Page<Orders> pageOrders= orderRepository.findByCompanyId_IdAndOrderStatusIdAndEmployee(companyId,statusId,userId,pageable);
        return pageOrders.getContent();
//        return orderRepository.findAll();
    }
    // שימי לב: זו סתם פונקציה שמכניסה נתונים בשביל הבדיקה
    public void fill() {
        AuditData d = new AuditData("1", new Date(),new Date());
        List<Orders> orders=new ArrayList<Orders>();
        for (int i = 200; i <500 ; i++) {
            if(i%3==0)
                orders.add(new Orders(Integer.toString(i),"employee","customer",i*2,new Company("11","333",88,d),new AuditData("1", new Date(), new Date(i*1000)),"1")) ;
            else if(i%3==1)
                orders.add(new Orders(Integer.toString(i),"custumer","customer",i*2,new Company("22","333",88,d),new AuditData("1", new Date(), new Date(i*1000)),"2")) ;
            else
                orders.add(new Orders(Integer.toString(i),"111","customer",i*2,new Company("11","333",88,d),new AuditData("1", new Date(), new Date(i*i)),"3")) ;
        }
        orderRepository.saveAll(orders);

    }
}

