package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;
import lombok.extern.java.Log;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class FeedbackRequest {

    private String name;
    private String email;
    private String contenue;
    //private String date;
}

