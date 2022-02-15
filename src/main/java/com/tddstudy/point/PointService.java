package com.tddstudy.point;

import com.tddstudy.membership.repo.MembershipRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface PointService {

    int calculate(int price);
}
