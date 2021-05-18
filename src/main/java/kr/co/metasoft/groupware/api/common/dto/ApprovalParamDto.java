package kr.co.metasoft.groupware.api.common.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalParamDto {

    private Long id;

    private Long vacationId;

    private Long step;

    private String director;

    private String president;

    private LocalDate approvalDate;



}
