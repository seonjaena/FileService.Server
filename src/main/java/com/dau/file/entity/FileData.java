package com.dau.file.entity;

import com.dau.file.entity.enums.STATUS;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "FILE_DATA")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
public class FileData {

    @Id
    @Column(name = "IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "STANDARD_TIME")
    private LocalDateTime standardTime;

    @Column(name = "JOIN_CNT")
    private Integer joinCnt;

    @Column(name = "QUIT_CNT")
    private Integer quitCnt;

    // 달러가 필요할 수 있기 때문에 BigDecimal 사용
    @Column(name = "PAYMENT_SUM")
    private BigDecimal paymentSum;

    // 달러가 필요할 수 있기 때문에 BigDecimal 사용
    @Column(name = "USE_SUM")
    private BigDecimal useSum;

    // 달러가 필요할 수 있기 때문에 BigDecimal 사용
    @Column(name = "SALES_SUM")
    private BigDecimal salesSum;

    @Column(name = "DATA_STATUS")
    @Convert(converter = STATUS.Converter.class)
    private STATUS status;

    public FileData(LocalDateTime standardTime, Integer joinCnt, Integer quitCnt, BigDecimal paymentSum, BigDecimal useSum, BigDecimal salesSum) {
        this.standardTime = standardTime;
        this.joinCnt = joinCnt;
        this.quitCnt = quitCnt;
        this.paymentSum = paymentSum;
        this.useSum = useSum;
        this.salesSum = salesSum;
    }

    public void modifyJoinCnt(Integer joinCnt) {
        this.joinCnt = joinCnt;
    }

    public void modifyQuitCnt(Integer quitCnt) {
        this.quitCnt = quitCnt;
    }

    public void modifyPaymentSum(BigDecimal paymentSum) {
        this.paymentSum = paymentSum;
    }

    public void modifyUseSum(BigDecimal useSum) {
        this.useSum = useSum;
    }

    public void modifySalesSum(BigDecimal salesSum) {
        this.salesSum = salesSum;
    }

    public void delete() {
        this.status = STATUS.DELETED;
    }

}
