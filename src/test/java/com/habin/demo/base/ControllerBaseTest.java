package com.habin.demo.base;

import com.habin.demo.base.doc.RestDocsConfig;
import com.habin.demo.base.docker.TestContainersConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@AutoConfigureMockMvc
@Import({RestDocsConfig.class, TestContainersConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerBaseTest extends AbstractIntegrationTest {

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        this.mockMvc = webAppContextSetup(context)
                .apply(documentationConfiguration(provider))
                .apply(springSecurity())
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter(UTF_8.name(), true))
                .build();
    }

    protected static final FieldDescriptor[] pageRequestFields = {
            fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지 번호").optional(),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 크기").optional()
    };

    protected static final FieldDescriptor[] pageResponseFields = {
            fieldWithPath("message").description("시스템 메시지"),
            fieldWithPath("result.content[]").description("오브젝트"),

            fieldWithPath("result.pageable").description("페이징 오브젝트"),
            fieldWithPath("result.pageable.offset").description("offset"),
            fieldWithPath("result.pageable.pageNumber").description("페이지 번호 (0부터 시작)"),
            fieldWithPath("result.pageable.pageSize").description("요청한 페이지 당 크기"),
            fieldWithPath("result.pageable.paged").description("페이징 여부"),
            fieldWithPath("result.pageable.unpaged").description("미 페이징 여부"),
            fieldWithPath("result.pageable.sort").description("정렬 정보"),
            fieldWithPath("result.pageable.sort.empty").description("정렬 정보 미존재 여부"),
            fieldWithPath("result.pageable.sort.unsorted").description("미정렬 여부"),
            fieldWithPath("result.pageable.sort.sorted").description("정렬 여부"),

            fieldWithPath("result.last").description("마지막 페이지 여부"),
            fieldWithPath("result.totalPages").description("페이지 갯수"),
            fieldWithPath("result.totalElements").description("총 요소 갯수"),
            fieldWithPath("result.first").description("첫 페이지 여부"),
            fieldWithPath("result.size").description("페이지 당 크기"),
            fieldWithPath("result.number").description("페이지 번호 (0부터 시작)"),
            fieldWithPath("result.sort.empty").description("정렬 정보 미존재 여부"),
            fieldWithPath("result.sort.unsorted").description("미정렬 여부"),
            fieldWithPath("result.sort.sorted").description("정렬 여부"),
            fieldWithPath("result.numberOfElements").description("현재 페이지 요소 갯수"),
            fieldWithPath("result.empty").description("현재 페이지 요소 미존재 여부")
    };

    protected static final HeaderDescriptor authorizationHeader = headerWithName(AUTHORIZATION).description("AccessToken \"Bearer \" prefix");

    protected String bearerToken(String tokenString) {
        return "Bearer " + tokenString;
    }

}
