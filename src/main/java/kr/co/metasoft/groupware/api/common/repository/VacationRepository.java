package kr.co.metasoft.groupware.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.api.common.entity.VacationEntity;

@Repository
public interface VacationRepository extends JpaRepository<VacationEntity, Long>{

}
