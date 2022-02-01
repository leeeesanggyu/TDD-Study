package com.tddstudy.membership;

import com.google.gson.Gson;
import com.tddstudy.membership.controller.MembershipController;
import com.tddstudy.membership.dto.MembershipReq;
import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.entity.MembershipKindType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.criteria.CriteriaBuilder;

import static com.tddstudy.membership.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    private MockMvc mockMvc;
    private Gson gson;

    @InjectMocks
    private MembershipController target;

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


}
