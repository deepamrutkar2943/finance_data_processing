package com.example.fdp.mapper;

import com.example.fdp.dto.response.RecordResponse;
import com.example.fdp.model.FinancialRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinancialRecordMapper {

    @Mapping(source = "createdBy.name", target = "createdByName")
    RecordResponse toResponse(FinancialRecord financialRecord);
}