package com.example.affiliatebatchprocessor.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String dni;

}
