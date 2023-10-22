package guru.springframework.sfgjms.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelloWorldMessage implements Serializable {
    private static final long serialVersionUID = 4846468244109199986L;

    public UUID id;
    public String message;
}
