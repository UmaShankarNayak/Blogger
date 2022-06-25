package webclient.handler;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WebClientResponse {

    private String status;
    private String message;
}
