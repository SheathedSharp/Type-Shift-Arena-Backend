/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-26 23:15:40
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
public class GameCategoryDTO {
    private String code;
    private String displayName;
    private boolean available;
}