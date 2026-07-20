package com.rawsur.apidgi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GlobalException  extends RuntimeException {

    private String code;
    private String message;

    public GlobalException( String msg ) {
        super( msg );
    }
}
