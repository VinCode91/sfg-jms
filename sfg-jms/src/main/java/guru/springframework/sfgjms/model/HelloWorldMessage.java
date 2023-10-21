package guru.springframework.sfgjms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class HelloWorldMessage implements Serializable {
    private static final long serialVersionUID = 4846468244109199986L;

    public UUID id;
    public String message;
}
