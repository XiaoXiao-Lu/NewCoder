package com.kinnon.controller;

import com.alibaba.fastjson.JSONObject;
import com.kinnon.domain.Message;
import com.kinnon.domain.Page;
import com.kinnon.domain.User;
import com.kinnon.service.MessageService;
import com.kinnon.service.UserService;
import com.kinnon.util.HostHolder;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author Kinnon
 * @create 2022-08-09 1:21
 */
@Controller
public class MessageController implements NewCoderConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userservice;


//    @GetMapping("/conversations")
//    public String getMessagePage() {
//        return "/site/letter";
//    }

    @GetMapping("/letters/list")
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/letters/list");
        page.setRows(messageService.getConversationCount(hostHolder.getUser().getId()));
        List<Message> conversationList = messageService.selectConversations(hostHolder.getUser().getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversationsList = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                int conversationUnreadCount = messageService.getLetterUnreadCount(hostHolder.getUser().getId(), message.getConversationId());
                map.put("unreadCount", conversationUnreadCount);
                int letterCount = messageService.getLetterCount(message.getConversationId());
                map.put("letterCount", letterCount);
                int targrtId = user.getId() == message.getFromId()? message.getToId() : message.getFromId();
                map.put("fromUser",userservice.getById(targrtId));
                conversationsList.add(map);
            }
        }
        int letterUnreadCount = messageService.getLetterUnreadCount(hostHolder.getUser().getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        model.addAttribute("conversations", conversationsList);
        int noticeUnreadCount = messageService.getNoticeUnreadCount(user.getId(),null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);


        return "/site/letter";
    }

    private List<Integer> ids(List<Message> letters){
        ArrayList<Integer> ids = new ArrayList<>();
        if (letters != null) {
            for (Message letter : letters) {
                int id = hostHolder.getUser().getId();
                if (letter.getStatus()==0 &&  id== letter.getToId() ){
                    ids.add(letter.getId());
                }
            }
        }

        return ids;
    }

    @GetMapping("/letters/detail/{conversationId}/{fromUserId}")
    public String getLetterDetail(Model model, @PathVariable("conversationId")
            String conversationId,@PathVariable("fromUserId") int fromUserId,Page page) {
        page.setLimit(5);
        page.setRows(messageService.getLetterCount(conversationId));
        page.setPath("/letters/detail/" + conversationId + "/" + fromUserId);
        List<Message> letterList = messageService.selectLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> lettersList = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userservice.getById(message.getFromId()));
                lettersList.add(map);
            }
        }
        List<Integer> ids = ids(letterList);
        if (ids.size() > 0) {
            messageService.updateMessageStatus(ids,NewCoderConstant.READ);
        }
        model.addAttribute("letters", lettersList);
        model.addAttribute("fromUser", userservice.getById(fromUserId));
        return "/site/letter-detail";
    }

    @PostMapping("/letters/send")
    @ResponseBody
    public String sendMessage(String toName, String content) {

        User toUser = userservice.findUserByName(toName);
        if (toUser == null) {
            return NewCoderUtil.getJSONString(1, "用户不存在");
        }
        if(toUser.getUsername().equals(hostHolder.getUser().getUsername())){
            return NewCoderUtil.getJSONString(1,"不能给自己发消息");
        }
        User user = hostHolder.getUser();
        Message message = new Message();
        message.setContent(content);
        message.setConversationId(user.getId() < toUser.getId() ?
                user.getId()+"_"+toUser.getId(): toUser.getId() +"_" + user.getId());
        message.setFromId(user.getId());
        message.setToId(toUser.getId());
        message.setStatus(UNREAD);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return NewCoderUtil.getJSONString(0);
    }

    @GetMapping("/notice/list")
    public String getNoticeList(Model model){
        User user = hostHolder.getUser();

        //评论通知
        Message latestComment = messageService.getLatestMessage(user.getId(), NewCoderConstant.TOPIC_COMMENT);
        Map<String,Object> messageVO = new HashMap<>();
        if (latestComment != null){
            messageVO.put("message",latestComment);
            String content = HtmlUtils.htmlUnescape(latestComment.getContent());
            Map<String,Object> data =  JSONObject.parseObject(content,HashMap.class);

            messageVO.put("user",userservice.getById((Integer)data.get("userId")));
            messageVO.put("entityId",data.get("entityId"));
            messageVO.put("entityType",data.get("entityType"));
            messageVO.put("postId",data.get("postId"));

            int count = messageService.getNoticeCount(user.getId(), NewCoderConstant.TOPIC_COMMENT);
            messageVO.put("count",count);
            int unreadCount = messageService.getNoticeUnreadCount(user.getId(), NewCoderConstant.TOPIC_COMMENT);
            messageVO.put("unreadCount",unreadCount);
            model.addAttribute("commentNotice",messageVO);
        }

        //点赞通知
         latestComment = messageService.getLatestMessage(user.getId(), NewCoderConstant.TOPIC_LIKE);
         messageVO = new HashMap<>();
        if (latestComment != null){
            messageVO.put("message",latestComment);
            String content = HtmlUtils.htmlUnescape(latestComment.getContent());
            Map<String,Object> data =  JSONObject.parseObject(content,HashMap.class);

            messageVO.put("user",userservice.getById((Integer)data.get("userId")));
            messageVO.put("entityId",data.get("entityId"));
            messageVO.put("entityType",data.get("entityType"));
            messageVO.put("postId",data.get("postId"));


            int count = messageService.getNoticeCount(user.getId(), NewCoderConstant.TOPIC_LIKE);
            messageVO.put("count",count);
            int unreadCount = messageService.getNoticeUnreadCount(user.getId(), NewCoderConstant.TOPIC_LIKE);
            messageVO.put("unreadCount",unreadCount);
            model.addAttribute("likeNotice",messageVO);
        }
        //关注通知
        latestComment = messageService.getLatestMessage(user.getId(), NewCoderConstant.TOPIC_FOLLOW);
        messageVO = new HashMap<>();
        if (latestComment != null){
            messageVO.put("message",latestComment);
            String content = HtmlUtils.htmlUnescape(latestComment.getContent());
            Map<String,Object> data =  JSONObject.parseObject(content,HashMap.class);

            messageVO.put("user",userservice.getById((Integer)data.get("userId")));
            messageVO.put("entityId",data.get("entityId"));
            messageVO.put("entityType",data.get("entityType"));



            int count = messageService.getNoticeCount(user.getId(), NewCoderConstant.TOPIC_FOLLOW);
            messageVO.put("count",count);
            int unreadCount = messageService.getNoticeUnreadCount(user.getId(), NewCoderConstant.TOPIC_FOLLOW);
            messageVO.put("unreadCount",unreadCount);
            model.addAttribute("followNotice",messageVO);
        }
        int letterUnreadCount = messageService.getLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        int noticeUnreadCount = messageService.getNoticeUnreadCount(user.getId(),null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);

        return "/site/notice";

    }


    @RequestMapping("/notice/detail/{topic}")
    public String getNoticeDetail(Model model,@PathVariable("topic") String topic,Page page){
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/"+topic);
        page.setRows(messageService.getNoticeCount(user.getId(),topic));

        List<Message> notices = messageService.getNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String,Object>> noticeVos = new ArrayList<>();
        if (notices != null){
            for (Message notice : notices){
                Map<String,Object> noticeVo = new HashMap<>();
                noticeVo.put("notice",notice);
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String,Object> data =  JSONObject.parseObject(content,HashMap.class);
                noticeVo.put("user",userservice.getById((Integer)data.get("userId")));
                noticeVo.put("entityId",data.get("entityId"));
                noticeVo.put("entityType",data.get("entityType"));
                noticeVo.put("postId",data.get("postId"));
                noticeVo.put("fromUser",userservice.getById(notice.getFromId()));
                noticeVos.add(noticeVo);
            }
        }
        model.addAttribute("notices",noticeVos);
        //设置已读
        List<Integer> ids = ids(notices);
        if (!ids.isEmpty()){
            messageService.updateMessageStatus(ids,READ);
        }

        return "/site/notice-detail";

    }


}
