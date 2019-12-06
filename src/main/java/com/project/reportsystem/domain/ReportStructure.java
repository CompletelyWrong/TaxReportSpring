package com.project.reportsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportStructure {
    @NotEmpty(message = "Please provide fullName")
    private String fullName;

    @NotEmpty(message = "Please provide type")
    private String type;

    @NotEmpty(message = "Please provide innCode")
    private String innCode;

    @NotEmpty(message = "Please provide periodStart")
    private String periodStart;

    @NotEmpty(message = "Please provide periodEnd")
    private String periodEnd;

    @NotEmpty(message = "Please provide incomeCode")
    private String incomeCode;

    @NotEmpty(message = "Please provide incomeValue")
    private String incomeValue;

    @NotEmpty(message = "Please provide outcomeCode")
    private String outcomeCode;

    @NotEmpty(message = "Please provide outcomeValue")
    private String outcomeValue;

    @NotEmpty(message = "Please provide percentCode")
    private String percentCode;

    @NotEmpty(message = "Please provide percentValue")
    private String percentValue;

    @NotEmpty(message = "Please provide clearCode")
    private String clearCode;

    @NotEmpty(message = "Please provide clearValue")
    private String clearValue;
}
