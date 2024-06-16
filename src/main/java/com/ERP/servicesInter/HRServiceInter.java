package com.ERP.servicesInter;

import com.ERP.dtos.HRDto;

import java.util.List;

public interface HRServiceInter {
    public List<HRDto> getAllHR();
    public HRDto getHRById(long hrId);
    public HRDto createHR(HRDto hrDto);
    public HRDto deleteHR(long hrId);
    public HRDto updateHR(long hrId, HRDto hrDto);
}
