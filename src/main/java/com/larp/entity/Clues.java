package com.larp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
public class Clues implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 游戏id
     */
    private Integer gameId;

    /**
     * 线索编号
     */
    private String code;

    /**
     * 线索来源
     */
    private String location;

    /**
     * 线索的描述
     */
    private String description;

    /**
     * 线索的图片
     */
    private String images;

    /**
     * 0 未获取的线索 1已获取的线索 2已公开的线索
     */
    private Integer status;

    /**
     * 这个线索给了哪个角色
     */
    private Integer roleId;

    /**
     * 第几轮线索
     */
    private Integer round;

    /**
     * 获取线索的时间
     */
    private Date pickTime;

    /**
     * 线索类型 'normal'普通线索,'special'特殊线索
     */
    private String clueType;
}
