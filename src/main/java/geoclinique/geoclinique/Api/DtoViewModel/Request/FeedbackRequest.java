package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;
import lombok.extern.java.Log;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class FeedbackRequest {

    @NotBlank
    @Size(min = 3)
    private String name;
    @NotBlank
    @Email
    private String email;
    @Size(min = 10)
    private String feedback;
}

