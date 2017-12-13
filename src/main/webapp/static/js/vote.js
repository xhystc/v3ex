

function dovote(type,id) {
   // alert("do vote:"+id);
    if(type==='question'){
        var voteParam = {
            questionId:id
        };
        $.post(path+'/vote/service/do_vote',voteParam,function (data) {
                if(data.code===0){
                    alert(data.hint);
                }else {
                    var $button = $('#vote-button-question-'+id);
                    $button.text('^ '+data.hint);
                    if(data.code>0){
                        $button.parent().addClass('voted');
                    }else {
                        $button.parent().removeClass('voted');
                    }
                }
        });
    }else if(type==='comment'){
        var voteParam = {
            commentId:id
        };
        $.post(path+'/vote/service/do_vote',voteParam,function (data) {
            console.log(data.hint);
            if(data.code===0){
                alert(data.hint);
            }else {
                var $agree = $('#comment-item-agree-'+id);
                $agree.text(data.hint);
                if(data.code >0){
                    $agree.addClass('voted');
                } else {
                    $agree.removeClass('voted');
                }

            }

        });
    }

}