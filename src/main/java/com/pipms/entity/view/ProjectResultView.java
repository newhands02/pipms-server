package com.pipms.entity.view;

import java.math.BigDecimal;

/**
 * @ClassName ProjectResultView
 * @Description TODO
 * @Author 661595
 * @Date 2021/8/416:28
 * @Version 1.0
 **/
public class ProjectResultView {
    private String section;
    private String unit;
    private BigDecimal nonAutoResult;
    private BigDecimal autoResult;
    private BigDecimal total;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getNonAutoResult() {
        return nonAutoResult;
    }

    public void setNonAutoResult(BigDecimal nonAutoResult) {
        this.nonAutoResult = nonAutoResult;
    }

    public BigDecimal getAutoResult() {
        return autoResult;
    }

    public void setAutoResult(BigDecimal autoResult) {
        this.autoResult = autoResult;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
