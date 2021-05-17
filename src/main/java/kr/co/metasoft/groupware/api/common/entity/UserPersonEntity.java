package kr.co.metasoft.groupware.api.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table (name = "`tb_user_person`")
@EntityListeners (value = {AuditingEntityListener.class})
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPersonEntity {

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Id
    @Column (name = "`user_id`", columnDefinition = "bigint(20)")
    private Long userId;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`person_id`", columnDefinition = "bigint(20)")
    private Long personId;

    @ManyToOne
    @NotFound (action = NotFoundAction.IGNORE)
    @JoinColumn (name = "`user_id`", referencedColumnName = "`id`", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @NotFound (action = NotFoundAction.IGNORE)
    @JoinColumn (name = "`person_id`", referencedColumnName = "`id`", insertable = false, updatable = false)
    private PersonEntity person;

}