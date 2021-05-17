package kr.co.metasoft.groupware.api.common.entity.id;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RoleMenuEntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull (groups = {RemoveValidationGroup.class})
    private Long roleId;

    @NotNull (groups = {RemoveValidationGroup.class})
    private Long menuId;

}