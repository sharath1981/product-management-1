package com.neoage.product;

import com.neoage.constant.AppConstant;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = AppConstant.PRODUCT_KEYSPACE, value = AppConstant.PRODUCT)
public class Product {

    @PrimaryKey
    private UUID id;
    private String name;
    private String description;
    private Double price;
}
