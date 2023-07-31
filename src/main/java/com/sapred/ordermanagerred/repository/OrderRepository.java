package com.sapred.ordermanagerred.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.sapred.ordermanagerred.model.Order;
import org.bson.conversions.Bson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>  {
    Page<Order> findByCompanyId_IdAndOrderStatusAndEmployeeId(String companyId, String orderStatusId, String employee, Pageable pageable);




    //        @Query("{"
    //  + "?#{( #companyId == null || #companyId == '') ? {_id: {$exists: true}} : {'companyId.$id': #companyId}}, "
    //  + "?#{( #orderStatus == null || #orderStatus == '') ? {_id: {$exists: true}} : {'orderStatus': #orderStatus}}, "
    // + "?#{( #employee == null || #employee == '') ? {_id: {$exists: true}} : {'employeeId.$id': #employee}}, "
//                + "?#{if ( #totalAmount == null ) { _id: {$exists: true} } else { 'totalAmount': #totalAmount }"
//                + "?#{( #orderItemsList == null ) ? {_id: {$exists: true}} : {'orderItemsList': #orderItemsList}}, "
//                + "?#{( #creditCardNumber == null ) ? {_id: {$exists: true}} : {'creditCardNumber': #creditCardNumber}}, "
//                + "?#{( #ExpireOn == null ) ? {_id: {$exists: true}} : {'ExpireOn': #ExpireOn}}, "
//                + "?#{( #cvc == null ) ? {_id: {$exists: true}} : {'cvc': #cvc}}, "
//                + "?#{( #notificationFlag == null ) ? {_id: {$exists: true}} : {'notificationFlag': #notificationFlag}}"
//                + "}")
//    @Query("{"
//            + "?#{if ( #totalAmount == null )  _id: {$exists: true} else  'totalAmount': #totalAmount }"
//            + "}")
//    Page<Order> findByParams(
//            //String companyId, Order.StatusOptions orderStatus, String employee,
//            Optional<Integer> totalAmount,
////                 Optional<List<OrderItem>> orderItemsList,
////                                 Optional<Integer> creditCardNumber, Optional<Date> ExpireOn,
////                                 Optional<Integer> cvc, Optional<Boolean> notificationFlag,
//            Pageable pageable);

}
