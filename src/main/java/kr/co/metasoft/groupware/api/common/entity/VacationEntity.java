package kr.co.metasoft.groupware.api.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "`tb_vacation`")
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationEntity {


    @Null (groups = {CreateValidationGroup.class})
    @NotNull (groups = {ModifyValidationGroup.class})
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition="bigint(20)")
    private Long id;

    @Column(name = "`user_id`", columnDefinition="bigint(20)", nullable = true)
    private Long userId;

    @Column (name = "`taking_user`", columnDefinition = "varchar(100)", nullable = true)
    private String takingUser;

    @Column (name = "`department`", columnDefinition = "varchar(100)", nullable = true)
    private String department;

    @Column (name = "`emergency_num`", columnDefinition = "varchar(100)", nullable = true)
    private String emergencyNum;

    @Column (name = "`type`", columnDefinition = "varchar(10)", nullable = true)
    private String type;

    @Column (name = "`detail`", columnDefinition = "varchar(100)", nullable = true)
    private String detail;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column (name = "`sterm`", columnDefinition = "date", nullable = true)
    private LocalDate sterm;

    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    @Column (name = "`eterm`", columnDefinition = "date", nullable = true)
    private LocalDate eterm;

    @CreatedDate
    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    @Column (name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    @Column (name = "`last_modified_date`", columnDefinition = "datetime", nullable = false)
    private LocalDateTime lastModifiedDate;

    @Transient
    private String teamLeader;

    @Transient
    private String director;

    @Transient
    private String president;

    @Transient
    private String step;

}
