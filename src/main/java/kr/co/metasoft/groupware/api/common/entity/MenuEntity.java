package kr.co.metasoft.groupware.api.common.entity;

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
        name = "`tb_menu`",
        uniqueConstraints = {
                @UniqueConstraint (columnNames = {"`parent_id`", "`ranking`"})
        }
)
@EntityListeners (value = {AuditingEntityListener.class})
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuEntity {

    @Null (groups = {CreateValidationGroup.class})
    @NotNull (groups = {ModifyValidationGroup.class})
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @Column (name = "`parent_id`", columnDefinition = "bigint(20)", nullable = true)
    private Long parentId;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`name`", columnDefinition = "varchar(100)", nullable = false)
    private String name;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`description`", columnDefinition = "varchar(500)", nullable = false)
    private String description;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`path`", columnDefinition = "varchar(500)", nullable = false)
    private String path;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`ranking`", columnDefinition = "int(11)", nullable = false)
    private Integer ranking;

    @NotNull (groups = {CreateValidationGroup.class, ModifyValidationGroup.class})
    @Column (name = "`show`", columnDefinition = "enum('T','F')", nullable = false)
    private String show;

    @Column (name = "`icon`", columnDefinition = "varchar(100)", nullable = true)
    private String icon;

    @CreatedDate
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @Column (name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @Column (name = "`last_modified_date`", columnDefinition = "datetime", nullable = false)
    private LocalDateTime lastModifiedDate;

}