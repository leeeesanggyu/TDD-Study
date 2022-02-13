package com.tddstudy.membership;

import com.google.gson.Gson;
import com.tddstudy.membership.controller.MembershipController;
import com.tddstudy.membership.dto.MembershipDetailRes;
import com.tddstudy.membership.dto.MembershipReq;
import com.tddstudy.membership.dto.MembershipRes;
import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.util.MembershipKindType;
import com.tddstudy.membership.exception.MembershipErrorResult;
import com.tddstudy.membership.exception.MembershipException;
import com.tddstudy.membership.service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.tddstudy.membership.util.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    private MockMvc mockMvc;
    private Gson gson;

    @InjectMocks
    private MembershipController target;
    @Mock
    private MembershipService membershipService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target).build();
        gson = new Gson();
    }

    @Test
    public void mockmvcNull() {
        assertThat(mockMvc).isNotNull();
        assertThat(target).isNotNull();
    }

    @Test
    public void 멤버쉽가입실패_사용자식별값이헤더에없음() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipReq(10000, MembershipKindType.KAKAO)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultAction.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버쉽가입실패_포인트null() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "userid")
                        .content(gson.toJson(membershipReq(null, MembershipKindType.KAKAO)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultAction.andExpect(status().is4xxClientError());
    }

    @Test
    public void 멤버쉽가입실패_포인트음수() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "userid")
                        .content(gson.toJson(membershipReq(-10000, MembershipKindType.KAKAO)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultAction.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버쉽가입실패_멤버쉽종류null() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "userid")
                        .content(gson.toJson(membershipReq(10000, null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultAction.andExpect(status().isBadRequest());
    }

    private MembershipReq membershipReq(
            final Integer point,
            final MembershipKindType kind
    ) {
        return MembershipReq.builder()
                .point(point)
                .kind(kind)
                .build();
    }

    @Test
    public void 멤버십등록실패_MemberService에서에러Throw() throws Exception {
        // given
        final String url = "/api/v1/membership";

        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345", MembershipKindType.NAVER, 10000);


        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                    .header(USER_ID_HEADER, "12345")
                    .content(gson.toJson(membershipReq(10000, MembershipKindType.NAVER)))
                    .contentType(MediaType.APPLICATION_JSON) );

        // then
        resultActions.andExpect(status().isBadRequest());
//        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void 멤버쉽등록성공() throws Exception {
        // given
        final String url = "/api/v1/membership";

        doReturn(
                MembershipRes.builder()
                        .id(1L)
                        .kind(MembershipKindType.KAKAO)
                        .build()
        ).when(membershipService).addMembership("aaa", MembershipKindType.KAKAO, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "aaa")
                        .content(gson.toJson(membershipReq(10000, MembershipKindType.KAKAO)))
                        .contentType(MediaType.APPLICATION_JSON) );

        // then
        resultActions.andExpect(status().is2xxSuccessful());

        final MembershipRes res = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                MembershipRes.class
        );

        assertThat(res.getId()).isNotNull();
        assertThat(res.getKind()).isEqualTo(MembershipKindType.KAKAO);
    }

    @Test
    public void 멤버십목록조회성공() throws Exception {
        // given
        final String url = "/api/v1/membership/list";

        doReturn(Arrays.asList(
                MembershipDetailRes.builder().build(),
                MembershipDetailRes.builder().build(),
                MembershipDetailRes.builder().build()
        )).when(membershipService).getMembershipList("12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 멤버쉽조회실패_사용자식별값이헤더에없음() throws Exception {
        //given
        final String url = "/api/v1/membership/list";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );
        //then
        resultAction.andExpect(status().isBadRequest());
    }



}
