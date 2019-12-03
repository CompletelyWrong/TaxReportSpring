package com.project.reportsystem.domain;

import com.project.reportsystem.entity.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    private Long id;

    private Inspector inspector;

    private Report report;

    private LocalDateTime dateTime;

    private String message;

    private ActionType action;
}
