package kr.co.metasoft.groupware.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.api.common.entity.RoleMenuEntity;
import kr.co.metasoft.groupware.api.common.entity.id.RoleMenuEntityId;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenuEntity, RoleMenuEntityId> {

}