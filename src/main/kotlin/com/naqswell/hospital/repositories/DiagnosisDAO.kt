package com.naqswell.hospital.repositories

import com.naqswell.hospital.models.diagnosis.DiagnosisEntity
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.data.repository.CrudRepository

interface DiagnosisDAO : CrudRepository<DiagnosisEntity, Int> {

    fun findByOrderById(): List<DiagnosisEntity>

    @Procedure("delete_top_diagnosis")
    fun deleteTopDiagnosis()
}