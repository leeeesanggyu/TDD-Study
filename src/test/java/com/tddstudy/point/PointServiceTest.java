package com.tddstudy.point;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @InjectMocks
    private RatePointService ratePointService;

    @Test
    public void PointService_Null() {
        assertThat(ratePointService).isNotNull();
    }

    @Test
    public void _10000원적립금1퍼() {
        final int price = 10000;

        final int result = ratePointService.calculate(price);

        assertThat(result).isEqualTo(100);
    }

    @Test
    public void _20000원적립금1퍼() {
        final int price = 20000;

        final int result = ratePointService.calculate(price);

        assertThat(result).isEqualTo(200);
    }

    @Test
    public void _30000원적립금1퍼() {
        final int price = 30000;

        final int result = ratePointService.calculate(price);

        assertThat(result).isEqualTo(300);
    }


}
