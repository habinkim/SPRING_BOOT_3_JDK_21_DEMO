package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.base.ControllerBaseTest;
import com.habin.demo.dummy.DummyService;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class RefreshControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("Access Token 재발급, 성공")
    void refreshSuccess() throws NoSuchAlgorithmException {
        dummyService.initAndLogin();
        String refreshToken = dummyService.getDefaultRefreshToken();


//        AuthenticationPayloads.RefreshRequest request = new AuthenticationPayloads.RefreshRequest(refreshToken);
//
//        FieldDescriptor requestDescriptor = fieldWithPath("refresh_token").description("Refresh Token");
//        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFieldsV2,
//                fieldWithPath("data").description("Response Data"),
//                fieldWithPath("data.details").description("Token 상세 정보"),
//                fieldWithPath("data.details.access_token").description("Access Token"),
//                fieldWithPath("data.details.refresh_token").description("Refresh Token"),
//                fieldWithPath("data.details.token_type").description("Token Type"),
//                fieldWithPath("data.details.expires_in").description("유효시간"),
//                fieldWithPath("data.roles").description("Role")
//        );
//
//        ResultActions refreshResultActions = mockMvc.perform(
//                        post(Uris.REFRESH_V2)
//                                .contentType(APPLICATION_JSON)
//                                .content(toJson(request))
//                )
//                .andExpectAll(baseAssertionV2(RESPONSE.SUCCESS_CREATED))
//                .andExpect(jsonPath("$.data", notNullValue()))
//                .andExpect(jsonPath("$.data.details", notNullValue()))
//
//                .andExpect(jsonPath("$.data.details.token_type", allOf(notNullValue(), is("Bearer"))))
//                .andExpect(jsonPath("$.data.details.expires_in", notNullValue()))
//                .andExpect(jsonPath("$.data.details.access_token", notNullValue()))
//                .andExpect(jsonPath("$.data.details.refresh_token", notNullValue()))
//                .andExpect(jsonPath("$.data.roles", empty()));
//
//        assertFalse(jwtUtils.isAccessTokenExpired(getAccessToken(refreshResultActions)));
//
//        refreshResultActions
//                .andDo(restDocs.document(
//                                requestFields(requestDescriptor), responseFields(responseDescriptors),
//                                resource(
//                                        builder().
//                                                description("Access Token 재발급 V2").
//                                                requestFields(requestDescriptor).
//                                                responseFields(responseDescriptors)
//                                                .build()
//                                )
//                        )
//                );

    }

}
