package com.project.reportsystem;

import com.project.reportsystem.domain.*;
import com.project.reportsystem.entity.*;

import java.time.LocalDateTime;
import java.util.Collections;

public class MockData {
    private static final Long USER_ID = 4L;
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_PASSWORD = "userpass1";
    private static final String USER_NAME = "Name";
    private static final String USER_SURNAME = "Surname";
    private static final String USER_PATRONYMIC = "Patronymic";
    private static final Integer USER_INN_CODE = 12345678;
    private static final Role USER_ROLE = Role.INDIVIDUAL_TAXPAYER;

    private static final Long INSPECTOR_ID = 4L;
    private static final String INSPECTOR_EMAIL = "email@gmail.com";
    private static final String INSPECTOR_PASSWORD = "userpass1";
    private static final String INSPECTOR_NAME = "Name";
    private static final String INSPECTOR_SURNAME = "Surname";
    private static final String INSPECTOR_PATRONYMIC = "Patronymic";
    private static final Role INSPECTOR_ROLE = Role.INSPECTOR;

    private static final Long REPORT_ID = 4L;
    private static final String REPORT_FILE_LINK = "FILE_LINK";
    private static final LocalDateTime REPORT_CREATION_TIME = LocalDateTime.of(12, 12, 12, 12, 12);
    private static final ReportStatus REPORT_STATUS = ReportStatus.REJECTED;

    private static final Long ACTION_ID = 4L;
    private static final String ACTION_MESSAGE = "Message";
    private static final LocalDateTime ACTION_CREATION_TIME = LocalDateTime.of(12, 12, 12, 12, 12);
    private static final ActionType ACTION_TYPE = ActionType.REJECT;

    private static final Long REQUEST_ID = 4L;
    private static final String REQUEST_REASON = "Reason";

    public static final Inspector MOCK_INSPECTOR = initInspector();

    public static final InspectorEntity MOCK_INSPECTOR_ENTITY = initInspectorEntity();

    public static final User MOCK_USER = initUser();

    public static final UserEntity MOCK_USER_ENTITY = initUserEntity();

    public static final Report MOCK_REPORT = initReport();

    public static final ReportEntity MOCK_REPORT_ENTITY = initReportEntity();

    public static final Action MOCK_ACTION = initAction();

    public static final ActionEntity MOCK_ACTION_ENTITY = initActionEntity();

    public static final Request MOCK_REQUEST = initRequest();

    public static final RequestEntity MOCK_REQUEST_ENTITY = initRequestEntity();

    private static User initUser() {
        return User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .name(USER_NAME)
                .surname(USER_SURNAME)
                .patronymic(USER_PATRONYMIC)
                .identificationCode(USER_INN_CODE)
                .role(USER_ROLE)
                .inspector(MOCK_INSPECTOR)
                .build();
    }

    private static UserEntity initUserEntity() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(USER_ID);
        userEntity.setEmail(USER_EMAIL);
        userEntity.setPassword(USER_PASSWORD);
        userEntity.setName(USER_NAME);
        userEntity.setSurname(USER_SURNAME);
        userEntity.setPatronymic(USER_PATRONYMIC);
        userEntity.setIdentificationCode(USER_INN_CODE);
        userEntity.setRole(USER_ROLE);
        userEntity.setInspector(MOCK_INSPECTOR_ENTITY);
        userEntity.setReports(Collections.singletonList(MOCK_REPORT_ENTITY));
        userEntity.setRequest(Collections.singletonList(MOCK_REQUEST_ENTITY));

        return userEntity;
    }

    private static Inspector initInspector() {
        return Inspector.builder()
                .id(INSPECTOR_ID)
                .email(INSPECTOR_EMAIL)
                .password(INSPECTOR_PASSWORD)
                .name(INSPECTOR_NAME)
                .surname(INSPECTOR_SURNAME)
                .patronymic(INSPECTOR_PATRONYMIC)
                .role(INSPECTOR_ROLE)
                .build();
    }

    private static InspectorEntity initInspectorEntity() {
        final InspectorEntity inspectorEntity = new InspectorEntity();
        inspectorEntity.setId(INSPECTOR_ID);
        inspectorEntity.setEmail(INSPECTOR_EMAIL);
        inspectorEntity.setPassword(INSPECTOR_PASSWORD);
        inspectorEntity.setName(USER_NAME);
        inspectorEntity.setSurname(INSPECTOR_NAME);
        inspectorEntity.setPatronymic(INSPECTOR_PATRONYMIC);
        inspectorEntity.setRole(INSPECTOR_ROLE);

        return inspectorEntity;
    }

    private static Action initAction() {
        return Action.builder()
                .id(ACTION_ID)
                .actionType(ACTION_TYPE)
                .dateTime(ACTION_CREATION_TIME)
                .message(ACTION_MESSAGE)
                .inspector(MOCK_INSPECTOR)
                .build();
    }

    private static ActionEntity initActionEntity() {
        final ActionEntity actionEntity = new ActionEntity();
        actionEntity.setId(ACTION_ID);
        actionEntity.setAction(ACTION_TYPE);
        actionEntity.setDateTime(ACTION_CREATION_TIME);
        actionEntity.setInspectorEntity(MOCK_INSPECTOR_ENTITY);
        actionEntity.setMessage(ACTION_MESSAGE);
        actionEntity.setReportEntity(MOCK_REPORT_ENTITY);

        return actionEntity;
    }

    private static Report initReport() {
        return Report.builder()
                .id(REPORT_ID)
                .creationDate(REPORT_CREATION_TIME)
                .fileLink(REPORT_FILE_LINK)
                .status(REPORT_STATUS)
                .user(MOCK_USER)
                .build();
    }

    private static ReportEntity initReportEntity() {
        final ReportEntity reportEntity = new ReportEntity();
        reportEntity.setId(REPORT_ID);
        reportEntity.setCreationDate(REPORT_CREATION_TIME);
        reportEntity.setFileLink(REPORT_FILE_LINK);
        reportEntity.setStatus(REPORT_STATUS);
        reportEntity.setEntityUser(MOCK_USER_ENTITY);
        reportEntity.setActions(Collections.singletonList(MOCK_ACTION_ENTITY));

        return reportEntity;
    }

    private static Request initRequest() {
        return Request.builder()
                .id(REQUEST_ID)
                .reason(REQUEST_REASON)
                .user(MOCK_USER)
                .build();
    }

    private static RequestEntity initRequestEntity() {
        final RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(REQUEST_ID);
        requestEntity.setReason(REQUEST_REASON);
        requestEntity.setEntityUser(MOCK_USER_ENTITY);

        return requestEntity;
    }
}
