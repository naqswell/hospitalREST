package com.naqswell.hospital.services.ward

import com.naqswell.hospital.models.wards.WardEntity
import com.naqswell.hospital.repositories.PeopleWardsDAO
import com.naqswell.hospital.repositories.WardsDAO
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

@Service
class WardsServiceImpl(
        private val peopleWardsDAO: PeopleWardsDAO,
        private val wardsDAO: WardsDAO) : WardsService {

    override fun findAll(): List<WardEntity> {
        log.info("Find all wards")
        return wardsDAO.findByOrderById()
    }

    override fun findById(id: Int): WardEntity {
        log.info("Find ward with id=$id")
        return wardsDAO.findByIdOrNull(id) ?: throw WardNotFoundException(id)
    }

    override fun createRequest(request: SaveWardsRequest) {
        log.info("Create new diagnosis with name=${request.name}")
        wardsDAO.save(
                WardEntity(request.name, request.maxCount)
        )
    }

    override fun getWardsSortAllByDescAndMaxCountByAsc(): List<WardEntity> = wardsDAO.getWardsSortAllByDescAndMaxCountByAsc()

    override fun getPeoplesCountInWard(ward_name: String): Int = wardsDAO.getPeoplesCountInWard(ward_name)

    override fun update(id: Int, request: SaveWardsRequest) {
        log.info("Update diagnosis with id=$id")
        val ward = wardsDAO.findByIdOrNull(id) ?: throw WardNotFoundException(id)
        ward.name = request.name!!
        ward.maxCount = request.maxCount!!
        wardsDAO.save(ward)
    }

    override fun delete(id: Int) {
        log.info("Delete ward with id=$id")
        val wards = peopleWardsDAO.findByIdOrNull(id) ?: throw WardNotFoundException(id)
        peopleWardsDAO.delete(wards)
    }

    override fun moveFromWardToWard(id1: Int, id2: Int) {
        wardsDAO.moveFromWardToWard(id1, id2)
    }

    override fun getWardsWhenTakenPlacesFewerThenAvgD1D2(id1: Int, id2: Int): List<WardEntity> =
            wardsDAO.getWardsWhenTakenPlacesFewerThenAvgD1D2(id1, id2)

    override fun getWardsMetrics(id: Int): List<Int?> {
        return wardsDAO.getWardsMetrics(id)
    }

    companion object {
        private val log = LoggerFactory.logger(WardsServiceImpl::class.java)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class WardNotFoundException(id: Int) : RuntimeException("Ward with id=$id not found")