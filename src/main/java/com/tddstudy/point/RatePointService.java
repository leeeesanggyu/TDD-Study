package com.tddstudy.point;

import org.springframework.stereotype.Service;

@Service
public class RatePointService implements PointService{

    final static int POINT_RATE = 1;

    @Override
    public int calculate(int price) {
        return (price / 100) * POINT_RATE;
    }
}
