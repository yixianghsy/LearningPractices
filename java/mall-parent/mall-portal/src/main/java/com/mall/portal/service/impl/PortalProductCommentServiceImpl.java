package com.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.mall.api.CommonResult;
import com.mall.mansger.mapper.PmsCommentMapper;
import com.mall.mansger.mapper.PmsCommentReplayMapper;
import com.mall.mansger.model.PmsComment;
import com.mall.mansger.model.PmsCommentReplay;
import com.mall.portal.dao.PortalProductCommentDao;
import com.mall.portal.domain.PmsCommentParam;
import com.mall.portal.service.PortalProductCommentService;
import com.mall.portal.service.UmsMemberService;
import com.mall.sso.model.UmsMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ,;,,;
 * ,;;'(    社
 * __      ,;;' ' \   会
 * /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 * ,;' \    /-.,,(   ) \    码
 * ) /       ) / )|    农
 * ||        ||  \)
 * (_\       (_\
 *
 * @author ：杨过
 * @date ：Created in 2020/2/9
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
@Service
public class PortalProductCommentServiceImpl implements PortalProductCommentService {
    @Autowired
    private PortalProductCommentDao productCommentDao;

    @Autowired
    private UmsMemberService memberService;

    @Autowired
    private PmsCommentMapper pmsMapper;

    @Autowired
    private PmsCommentReplayMapper replayMapper;

    /**
     * 获取评论列表
     * @param productId
     * @return
     */
    @Override
    public CommonResult<List<PmsCommentParam>> getCommentList(Long productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return CommonResult.success(productCommentDao.getCommentList(productId));
    }

    /**
     * 用户评价
     * @param pmsComment
     * @return
     */
    @Override
    public Integer insertProductComment(PmsComment pmsComment) {
        UmsMember member = memberService.getCurrentMember();
        //判断一下当前用户是否购买过当前评论的商品
        Integer status = productCommentDao.selectUserOrder(member.getId(), pmsComment.getProductId());
        if(status > 0){
            pmsComment.setCreateTime(new Date());
            pmsComment.setShowStatus(0);
            pmsComment.setMemberNickName(member.getNickname());
            pmsComment.setMemberIcon(member.getIcon());
            return pmsMapper.insert(pmsComment);
        }
        return -1;
    }

    /**
     * 用户评价回复
     * @param reply
     * @return
     */
    @Override
    public Integer insertCommentReply(PmsCommentReplay reply) {
        UmsMember member = memberService.getCurrentMember();
        reply.setCreateTime(new Date());
        reply.setMemberNickName(member.getNickname());
        reply.setMemberIcon(member.getIcon());
        reply.setType(0);
        return replayMapper.insert(reply);
    }
}

