package ni.edu.uam.sistema_de_registro_de_colaboradores.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

public class Collaborator {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String position;
    private String workArea;
    private LocalDate hireDate;
    private String contractType;
    private String benefits;



    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getHireDateText(){
        return hireDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
