
//var indexPage = 1;
$(window).scroll(function(){

    var a=$(this).height();

    var c =$(document).height();

    var b =$(this).scrollTop();

    if(c===a+b	) {
        alert("到达底部了");
    }

});