package com.project.reportsystem.service.mapper;

import com.project.reportsystem.domain.Request;
import com.project.reportsystem.entity.RequestEntity;
import org.junit.Test;

import static com.project.reportsystem.MockData.MOCK_REQUEST;
import static com.project.reportsystem.MockData.MOCK_REQUEST_ENTITY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RequestMapperTest {
    private static final Request REQUEST = MOCK_REQUEST;
    private static final RequestEntity REQUEST_ENTITY = MOCK_REQUEST_ENTITY;

    private final RequestMapper mapper = new RequestMapper();

    @Test
    void mapRequestToRequestEntityShouldReturnRequestEntity() {
        final RequestEntity actual = mapper.mapRequestToRequestEntity(REQUEST);

        assertThat(actual.getId(), is(MOCK_REQUEST.getId()));
        assertThat(actual.getReason(), is(MOCK_REQUEST.getReason()));
        assertThat(actual.getEntityUser().getId(), is(MOCK_REQUEST.getUser().getId()));
    }

    @Test
    void mapRequestEntityToRequestShouldReturnRequest() {
        final Request actual = mapper.mapRequestEntityToRequest(REQUEST_ENTITY);

        assertThat(actual.getId(), is(MOCK_REQUEST_ENTITY.getId()));
        assertThat(actual.getReason(), is(MOCK_REQUEST_ENTITY.getReason()));
        assertThat(actual.getUser().getId(), is(MOCK_REQUEST_ENTITY.getEntityUser().getId()));
    }
}