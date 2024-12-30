/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-26 18:17:51
 */

package com.example.demo.model.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameLanguageDTO {
    private String code;
    private String name;
    private String displayName;
    private boolean available;
}