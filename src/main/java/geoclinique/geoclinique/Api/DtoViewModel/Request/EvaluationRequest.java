package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class EvaluationRequest {
    private Long rendezvousId;
    private String date;
    private String message;
}
