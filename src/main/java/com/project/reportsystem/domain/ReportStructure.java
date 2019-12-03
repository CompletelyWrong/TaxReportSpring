package com.project.reportsystem.domain;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportStructure {
    @Setter(AccessLevel.PUBLIC)
    private String fullName;
    @Setter(AccessLevel.PUBLIC)
    private String type;
    @Setter(AccessLevel.PUBLIC)
    private String innCode;
    @Setter(AccessLevel.PUBLIC)
    private String periodStart;
    @Setter(AccessLevel.PUBLIC)
    private String periodEnd;
    @Setter(AccessLevel.PUBLIC)
    private String incomeCode;
    @Setter(AccessLevel.PUBLIC)
    private String incomeValue;
    @Setter(AccessLevel.PUBLIC)
    private String outcomeCode;
    @Setter(AccessLevel.PUBLIC)
    private String outcomeValue;
    @Setter(AccessLevel.PUBLIC)
    private String percentCode;
    @Setter(AccessLevel.PUBLIC)
    private String percentValue;
    @Setter(AccessLevel.PUBLIC)
    private String clearCode;
    @Setter(AccessLevel.PUBLIC)
    private String clearValue;
}
