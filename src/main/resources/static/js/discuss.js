$(function (){
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);
});

function setTop(){
    $.post(
        context_path + "/discuss/top",
        {
            id:$("#postId").val()
        },
        function (data){
            data = $.parseJSON(data);
            if (data.code == 0){
                $("#topBtn").text(data.type==1?'取消置顶':'置顶');
            }else{
                alert(data.msg)
            }
        }
    );
}

function setWonderful(){
    $.post(
        context_path + "/discuss/wonderful",
        {
            id:$("#postId").val()
        },
        function (data){
            data = $.parseJSON(data);
            if (data.code == 0){
                $("#wonderfulBtn").text(data.status==1?'取消加精':'加精');
            }else{
                alert(data.msg)
            }
        }
    );
}

function setDelete(){
    $.post(
        context_path + "/discuss/delete",
        {
            id:$("#postId").val()
        },
        function (data){
            data = $.parseJSON(data);
            if (data.code == 0){
              location.href = context_path + "/index";
            }else{
                alert(data.msg)
            }
        }
    );
}


function like(btn,entityType,entityId,userId,postId){
    $.post(
        context_path + "/like",
        {
            entityType: entityType,
            entityId: entityId,
            entityUserId: userId,
            postId: postId

        },
        function (data){
          data = $.parseJSON(data);
          if (data.code == 0){
            $(btn).children("i").text(data.likeCount);
            $(btn).children("b").text(data.likeStatus==1?"已赞":"赞");
          }else{
              alert(data.msg)
          }
        }
    );
}