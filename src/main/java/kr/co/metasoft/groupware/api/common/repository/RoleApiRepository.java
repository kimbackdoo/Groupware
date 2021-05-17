package kr.co.metasoft.groupware.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.api.common.entity.RoleApiEntity;
import kr.co.metasoft.groupware.api.common.entity.id.RoleApiEntityId;

@Repository
public interface RoleApiRepository extends JpaRepository<RoleApiEntity, RoleApiEntityId> {

}