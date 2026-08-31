package com.vehicleservice.repository;

import com.vehicleservice.model.ServiceDetail;
import java.util.List;

public interface ServiceDetailRepository {
    ServiceDetail save(ServiceDetail detail);
    List<ServiceDetail> findByRecordId(int recordId);
    List<ServiceDetail> findAll();
}
