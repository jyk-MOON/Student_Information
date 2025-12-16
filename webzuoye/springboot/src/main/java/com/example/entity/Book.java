package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Book {
    private Integer id;
    private String name;
    private String isbn;
    private String author;
    private String publisher;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    private Integer categoryId;
    private String categoryName;
    private BigDecimal price;
    private Integer total;
    private Integer available;
    private String cover;
    private String location;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}