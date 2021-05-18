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
import javax.persistence.UniqueConstraint;
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
@Table (
        name = "`tb_user`",
        uniqueConstraints = {
                @UniqueConstraint (columnNames = {"`username`"})
        }
)
@EntityListeners (value = {AuditingEntityListener.class})
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Null (groups = {CreateValidationGroup.class})
    @NotNull (groups = {ModifyValidationGroup.class})
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`username`", columnDefinition = "varchar(100)", nullable = false)
    private String username;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`password`", columnDefinition = "varchar(500)", nullable = false)
    private String password;

    @Column (name = "`email`", columnDefinition = "varchar(500)", nullable = true)
    private String email;

    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    @Column (name = "`birth_date`", columnDefinition = "date", nullable = true)
    private LocalDate birthDate;

    @Column (name = "`department`", columnDefinition = "varchar(100)", nullable = false)
    private String department;

    @Column (name = "`team_name`", columnDefinition = "varchar(100)", nullable = false)
    private String teamName;

    @Column (name = "`position`", columnDefinition = "varchar(100)", nullable = false)
    private String position;

    @Column (name = "`status`", columnDefinition = "enum('T','F','D')", nullable = true)
    private String status;

    @CreatedDate
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @Column (name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = true)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @Column (name = "`last_modified_date`", columnDefinition = "datetime", nullable = true)
    private LocalDateTime lastModifiedDate;

    @LastModifiedDate
    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    @Column (name = "`last_modified_password_date`", columnDefinition = "date", nullable = true)
    private LocalDate lastModifiedPasswordDate;

}