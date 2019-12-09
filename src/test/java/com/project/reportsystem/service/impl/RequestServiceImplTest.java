package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Request;
import com.project.reportsystem.entity.RequestEntity;
import com.project.reportsystem.repository.RequestRepository;
import com.project.reportsystem.service.mapper.RequestMapper;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.project.reportsystem.MockData.MOCK_REQUEST;
import static com.project.reportsystem.MockData.MOCK_REQUEST_ENTITY;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RequestServiceImpl.class)
public class RequestServiceImplTest {
    private static final Request REQUEST = MOCK_REQUEST;
    private static final RequestEntity REQUEST_ENTITY = MOCK_REQUEST_ENTITY;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private RequestMapper requestMapper;

    @MockBean
    private RequestRepository requestRepository;

    @Autowired
    private RequestServiceImpl requestService;

    @After
    public void resetMocks() {
        reset(requestMapper, requestRepository);
    }

    @Test
    public void createRequestShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Request is null");
        requestService.createRequest(null);
    }

    @Test
    public void deleteRequestByIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Request id is null");
        requestService.deleteRequestById(null);
    }

    @Test
    public void findAllShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Pageable is null");
        requestService.findAll(null);
    }

    @Test
    public void createRequestShouldReturnRequest() {
        when(requestMapper.mapRequestToRequestEntity(any(Request.class)))
                .thenReturn(REQUEST_ENTITY);

        Request actual = requestService.createRequest(REQUEST);
        verify(requestRepository).save(any(RequestEntity.class));

        assertThat(actual, is(REQUEST));
    }

    @Test
    public void deleteRequestByIdShouldReturnRequest() {
        when(requestMapper.mapRequestToRequestEntity(any(Request.class)))
                .thenReturn(REQUEST_ENTITY);

        requestService.deleteRequestById(REQUEST.getId());
        verify(requestRepository).deleteById(anyLong());

    }

    @Test
    public void findAllShouldReturnPage() {
        when(requestMapper.mapRequestEntityToRequest(any(RequestEntity.class))).thenReturn(REQUEST);
        when(requestRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(singletonList(REQUEST_ENTITY)));

        Page<Request> actual = requestService.findAll(PageRequest.of(0, 10));

        assertThat(actual.getContent(), hasItem(REQUEST));
    }
}