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
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 描述
     */
    private String description;

    /**
     * 开始时间
     */
    private Date createTime;

    /**
     * 状态 -1为已玩过的 0为还未玩过 1为当前在玩的
     */
    private Integer status;

    /**
     * 最大人数
     */
    private Integer maxUser;

    /**
     * 最小人数
     */
    private Integer minUser;

    /**
     *  当前是第几幕
     */
    private Integer round;

    /**
     *  当前是否开启了线索 1开启 0未开启
     */
    private Integer cluesEnable;

    /**
     * 每轮最多获取线索数量
     */
    private Integer maxClues;

    /**
     * 主持人
     */
    private String dm;

    /**
     *  共有几幕
     */
    private Integer roundTotal;

}
