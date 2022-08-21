package com.llp.kill.remote;

import com.llp.common.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    List<GoodsVo> listGoodsVoBetweenTime();
}
