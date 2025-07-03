package com.xins.batch.bean.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * (UsersOrders)实体类
 *
 * @author makejava
 * @since 2025-06-30 10:40:12
 */
@Data
public class UsersOrders implements Serializable {
    @Serial
    private static final long serialVersionUID = 460400711791007459L;

    private Long orderId;

    private Integer userId;

    private Date orderDate;

    private Integer productId;

    private Integer quantity;

    private Double unitPrice;

    private Double totalAmount;

    private String paymentMethod;

    private String orderStatus;

    private String shippingAddress;

    private Date createdAt;

    private Date updatedAt;

}

