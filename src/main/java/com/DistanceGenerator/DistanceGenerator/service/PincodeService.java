package com.DistanceGenerator.DistanceGenerator.service;

import com.DistanceGenerator.DistanceGenerator.entity.PincodeInfo;
import com.DistanceGenerator.DistanceGenerator.repository.PincodeInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PincodeService {

    @Autowired
    private PincodeInfoRepository pincodeInfoRepository;

    public PincodeInfo savePincodeInfo(String pincode, String latitude, String longitude, String polygon) {
        PincodeInfo pincodeInfo = new PincodeInfo();
        pincodeInfo.setPincode(pincode);
        pincodeInfo.setLatitude(latitude);
        pincodeInfo.setLongitude(longitude);
        pincodeInfo.setPolygon(polygon);

        return pincodeInfoRepository.save(pincodeInfo);
    }
}
