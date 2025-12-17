package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Borrow {
    private Integer id;
    private Integer bookId;
    private String bookName;
    private Integer userId;
    private String userName;
    private Integer adminId;
    private String adminName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date borrowTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    private String status;
    private Integer overdueDays;
    private BigDecimal fine;
}