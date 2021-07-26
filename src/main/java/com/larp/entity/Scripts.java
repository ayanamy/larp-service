package com.larp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author hippo
 * @since 2021-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Scripts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 游戏id
     */
    private Integer gameId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 第几幕的剧本
     */
    private Integer round;

    /**
     * 剧本内容
     */
    private String content;


}
