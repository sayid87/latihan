package com.latihan.latihan.responses;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    private T data;
    private String message;
    private int sukses;
    private ArrayList<String> errorsMsg;
}
