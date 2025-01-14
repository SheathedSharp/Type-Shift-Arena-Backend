/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-26 23:21:31
 */

package com.example.demo.model.dto.game;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameTextMetadataDTO {
    private List<String> authors;
    private List<String> sourceTitles;
    private int totalTexts;
}