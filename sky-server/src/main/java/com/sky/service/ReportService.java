package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * @ClassName ReportService
 * @Author 26483
 * @Date 2023/11/25 15:17
 * @Version 1.0
 * @Description TODO
 */
public interface ReportService {

    /**
     * 统计指定事件区间内的营业额
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
